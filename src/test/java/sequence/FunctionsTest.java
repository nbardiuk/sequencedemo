package sequence;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static sequence.Functions.sequence;
import static sequence.Functions.functions;

public class FunctionsTest {
    @Test public void shouldSequenceFunctions() throws Exception {
        Function<Integer, List<Integer>> result = sequence(i -> i * 10, Math::abs, i -> i % 2);
        assertThat(result.apply(4), is(asList(40, 4, 0)));
        assertThat(result.apply(-2), is(asList(-20, 2, 0)));

        Function<String, List<Integer>> results = sequence(String::length, Integer::valueOf, s -> 10);
        assertThat(results.apply("4"), is(asList(1, 4, 10)));
        assertThat(results.apply("-2"), is(asList(2, -2, 10)));
    }

    @Test public void shouldPerformDownstreamCollection() throws Exception {
        Function<Integer, Map<Boolean, List<Integer>>> result = Stream.<Function<Integer, Integer>>of(i -> i + 1, i -> i * 2, i -> i * 2 + 1)
                .collect(functions(groupingBy(i -> i % 2 == 0)));

        assertThat(result.apply(4), is(map(
                true, asList(8),
                false, asList(5, 9))));
        assertThat(result.apply(-2), is(map(
                true, asList(-4),
                false, asList(-1, -3))));
    }

    <K, V> Map<K, V> map(K k1, V v1, K k2, V v2) {
        HashMap<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }
}