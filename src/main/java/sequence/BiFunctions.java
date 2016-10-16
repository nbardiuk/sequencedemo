package sequence;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static sequence.Collectors.accumulator;
import static sequence.Collectors.collector;
import static sequence.Collectors.combiner;

public class BiFunctions {

    @SafeVarargs public static <X, Y, T> BiFunction<X, Y, List<T>> sequence(BiFunction<X, Y, T>... functions) {
        return stream(functions).collect(functions(toList()));
    }

    public static <X, Y, T, A, R> Collector<BiFunction<X, Y, T>, ?, BiFunction<X, Y, R>> functions(Collector<T, A, R> downstream) {
        return collector(
                lift(downstream.supplier()),
                lift(accumulator(downstream)),
                lift(combiner(downstream)),
                lift(downstream.finisher()));
    }

    private static <X, Y, A> Supplier<BiFunction<X, Y, A>> lift(Supplier<A> a) {
        return () -> (x, y) -> a.get();
    }

    private static <X, Y, A, B, C> BiFunction<BiFunction<X, Y, A>, BiFunction<X, Y, B>, BiFunction<X, Y, C>> lift(BiFunction<A, B, C> f) {
        return (fa, fb) -> (x, y) -> f.apply(fa.apply(x, y), fb.apply(x, y));
    }

    private static <X, Y, A, B> Function<BiFunction<X, Y, A>, BiFunction<X, Y, B>> lift(Function<A, B> f) {
        return fa -> (x, y) -> f.apply(fa.apply(x, y));
    }

}
