package sequence;

import java.util.function.*;
import java.util.stream.Collector;

import static java.util.stream.Collector.of;

class Collectors {

    /**
     * Accepts accumulator function instead of accumulator consumer
     *
     * @see Collector#of(Supplier, BiConsumer, BinaryOperator, Function, Collector.Characteristics...)
     * @see Collectors#accumulator(Collector)
     * @see Collectors#combiner(Collector)
     */
    static <T, A, R> Collector<T, ?, R> collector(
            Supplier<A> supplier,
            BiFunction<A, T, A> accumulator,
            BiFunction<A, A, A> combiner,
            Function<A, R> finisher) {
        return of(lift(supplier),
                toBox(accumulator),
                lift(combiner),
                fromBox(finisher));
    }

    /**
     * Creates a pure combiner that does not mutate it's parameters
     */
    static <B, A> BiFunction<A, A, A> combiner(Collector<B, A, ?> collector) {
        BinaryOperator<A> combiner = collector.combiner();
        Supplier<A> zero = collector.supplier();
        return (a, b) -> combiner.apply(combiner.apply(zero.get(), a), b);
    }

    /**
     * Creates a pure accumulator that does not mutate it's parameters
     */
    static <A, B> BiFunction<A, B, A> accumulator(Collector<B, A, ?> collector) {
        BinaryOperator<A> append = collector.combiner();
        Supplier<A> zero = collector.supplier();
        BiConsumer<A, B> accumulator = collector.accumulator();
        return (a, b) -> {
            A copy = append.apply(zero.get(), a);
            accumulator.accept(copy, b);
            return copy;
        };
    }

    private static <A> Supplier<Box<A>> lift(Supplier<A> a) {
        return () -> new Box<>(a.get());
    }

    private static <A> BinaryOperator<Box<A>> lift(BiFunction<A, A, A> f) {
        return (l, r) -> new Box<>(f.apply(l.value, r.value));
    }

    private static <A, B> Function<Box<A>, B> fromBox(Function<A, B> f) {
        return ma -> f.apply(ma.value);
    }

    private static <A, B> BiConsumer<Box<A>, B> toBox(BiFunction<A, B, A> f) {
        return (ma, b) -> ma.value = f.apply(ma.value, b);
    }

    private static class Box<T> {
        T value;

        Box(T value) {
            this.value = value;
        }
    }

}
