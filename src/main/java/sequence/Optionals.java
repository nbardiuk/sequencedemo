package sequence;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static sequence.Collectors.accumulator;
import static sequence.Collectors.collector;
import static sequence.Collectors.combiner;

public class Optionals {
    @SafeVarargs public static <T> Optional<List<T>> sequence(Optional<T>... optionals) {
        return stream(optionals).collect(optionals(toList()));
    }

    public static <T, A, R> Collector<Optional<T>, ?, Optional<R>> optionals(Collector<T, A, R> downstream) {
        return collector(
                lift(downstream.supplier()),
                lift(accumulator(downstream)),
                lift(combiner(downstream)),
                lift(downstream.finisher()));
    }

    private static <A> Supplier<Optional<A>> lift(Supplier<A> a) {
        return () -> ofNullable(a.get());
    }

    private static <A, B, C> BiFunction<Optional<A>, Optional<B>, Optional<C>> lift(BiFunction<A, B, C> f) {
        return (oa, ob) -> oa.flatMap(a -> ob.map(b -> f.apply(a, b)));
    }

    private static <A, B> Function<Optional<A>, Optional<B>> lift(Function<A, B> f) {
        return a -> a.map(f);
    }
}
