package sequence;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Arrays.stream;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;
import static sequence.Collectors.accumulator;
import static sequence.Collectors.collector;
import static sequence.Collectors.combiner;

public class Futures {

    @SafeVarargs public static <T> CompletableFuture<List<T>> sequence(CompletableFuture<T>... futures) {
        return stream(futures).collect(futures(toList()));
    }

    public static <T, A, R> Collector<CompletableFuture<T>, ?, CompletableFuture<R>> futures(Collector<T, A, R> downstream) {
        return collector(
                lift(downstream.supplier()),
                lift(accumulator(downstream)),
                lift(combiner(downstream)),
                lift(downstream.finisher()));
    }

    private static <A> Supplier<CompletableFuture<A>> lift(Supplier<A> t) {
        return () -> completedFuture(t.get());
    }

    private static <A, B, C> BiFunction<CompletableFuture<A>, CompletableFuture<B>, CompletableFuture<C>> lift(BiFunction<A, B, C> f) {
        return (fa, fb) -> fa.thenCombine(fb, f);
    }

    private static <A, B> Function<CompletableFuture<A>, CompletableFuture<B>> lift(Function<A, B> f) {
        return fa -> fa.thenApply(f);
    }
}


