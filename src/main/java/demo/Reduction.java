package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import static java.util.Collections.emptyList;

public class Reduction {

    <T> Optional<List<T>> sequence(List<Optional<T>> optionals) {
        return optionals.stream().reduce(
                pure(emptyList()),
                lift(add()),
                lift(addAll()));
    }

    // Lifts function f into Optional context
    <A, B, C> BiFunction<Optional<A>, Optional<B>, Optional<C>>
    lift(BiFunction<A, B, C> f) {
        return (oa, ob) -> oa.flatMap(a -> ob.map(b -> f.apply(a, b)));
    }

    <A> BinaryOperator<Optional<A>> lift(BinaryOperator<A> f) {
        return (oa, ob) -> oa.flatMap(a -> ob.map(b -> f.apply(a, b)));
    }

    <T> Optional<T> pure(T value) {
        return Optional.of(value);
    }

    <T> BiFunction<List<T>, T, List<T>> add() {
        return (ts, t) -> {
            ArrayList<T> result = new ArrayList<>(ts);
            result.add(t);
            return result;
        };
    }

    <T> BinaryOperator<List<T>> addAll() {
        return (ts, ts2) -> {
            ArrayList<T> result = new ArrayList<>(ts);
            result.addAll(ts2);
            return result;
        };
    }
}
