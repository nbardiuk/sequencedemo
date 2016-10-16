package sequence;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static sequence.BiFunctions.sequence;
import static sequence.BiFunctions.functions;

public class BiFunctionsTest {
    @Test public void shouldSequenceFunctions() throws Exception {
        BiFunction<Integer, Integer, List<Integer>> result = sequence(Integer::min, Integer::max, Integer::sum);
        assertThat(result.apply(4, 10), is(asList(4, 10, 14)));
        assertThat(result.apply(-10, -2), is(asList(-10, -2, -12)));

        BiFunction<String, String, List<String>> results = sequence(String::concat, (s, z) -> s + ", " + z, (s, z) -> z + " | " + s);
        assertThat(results.apply("S", "Z"), is(asList("SZ", "S, Z", "Z | S")));
        assertThat(results.apply("a", ""), is(asList("a", "a, ", " | a")));
    }

    @Test public void shouldPerformDownstreamCollection() throws Exception {
        BiFunction<Integer, Integer, Map<Boolean, List<Integer>>> result = Stream.<BiFunction<Integer, Integer, Integer>>of((a, b) -> a + b, (a, b) -> a, (a, b) -> b)
                .collect(functions(groupingBy(i -> i % 2 == 0)));

        assertThat(result.apply(1, 3), is(map(
                true, asList(4),
                false, asList(1, 3))));
        assertThat(result.apply(-2, -5), is(map(
                true, asList(-2),
                false, asList(-7, -5))));
    }

    <K, V> Map<K, V> map(K k1, V v1, K k2, V v2) {
        HashMap<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }
}