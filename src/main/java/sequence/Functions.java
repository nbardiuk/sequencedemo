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

public class Functions {

    @SafeVarargs public static <X, T> Function<X, List<T>> sequence(Function<X, T>... functions) {
        return stream(functions).collect(functions(toList()));
    }

    public static <X, T, A, R> Collector<Function<X, T>, ?, Function<X, R>> functions(Collector<T, A, R> downstream) {
        return collector(
                lift(downstream.supplier()),
                lift(accumulator(downstream)),
                lift(combiner(downstream)),
                lift(downstream.finisher()));
    }

    private static <X, A> Supplier<Function<X, A>> lift(Supplier<A> a) {
        return () -> x -> a.get();
    }

    private static <X, A, B, C> BiFunction<Function<X, A>, Function<X, B>, Function<X, C>> lift(BiFunction<A, B, C> f) {
        return (fa, fb) -> x -> f.apply(fa.apply(x), fb.apply(x));
    }

    private static <X, A, B> Function<Function<X, A>, Function<X, B>> lift(Function<A, B> f) {
        return fa -> x -> f.apply(fa.apply(x));
    }

}
