package sequence;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static sequence.Collectors.accumulator;
import static sequence.Collectors.collector;
import static sequence.Collectors.combiner;

public class Lists {
    @SafeVarargs public static <T> List<List<T>> sequence(List<T>... lists) {
        return stream(lists).collect(lists(toList()));
    }

    public static <T, A, R> Collector<List<T>, ?, List<R>> lists(Collector<T, A, R> downstream) {
        return collector(
                lift(downstream.supplier()),
                lift(accumulator(downstream)),
                lift(combiner(downstream)),
                lift(downstream.finisher()));
    }

    private static <A> Supplier<List<A>> lift(Supplier<A> a) {
        return () -> singletonList(a.get());
    }

    private static <A, B, C> BiFunction<List<A>, List<B>, List<C>> lift(BiFunction<A, B, C> f) {
        return (as, bs) -> as.stream().flatMap(a -> bs.stream().map(b -> f.apply(a, b))).collect(toList());
    }

    private static <A, B> Function<List<A>, List<B>> lift(Function<A, B> f) {
        return a -> a.stream().map(f).collect(toList());
    }
}
