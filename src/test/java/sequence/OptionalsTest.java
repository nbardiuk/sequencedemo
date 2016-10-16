package sequence;

import org.junit.Test;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static sequence.Optionals.optionals;
import static sequence.Optionals.sequence;

public class OptionalsTest {
    @Test public void shouldCollectPresentOptionals() throws Exception {
        Stream<Optional<Integer>> stream = Stream.of(of(1), of(2), of(3), of(0));

        Optional<Map<Boolean, Set<Integer>>> map = stream.collect(optionals(groupingBy(i -> i % 2 == 0, toSet())));

        Map<Boolean, Set<Integer>> expected = new HashMap<>();
        expected.put(true, new HashSet<>(asList(0, 2)));
        expected.put(false, new HashSet<>(asList(1, 3)));
        assertThat(map, is(of(expected)));
    }


    @Test public void shouldCollectToEmptyIfOneEmpty() throws Exception {
        assertThat(sequence(of(1), empty(), of(3)), is(empty()));
        assertThat(sequence(empty()), is(empty()));
    }

    @Test public void shouldCollectAll() throws Exception {
        assertThat(sequence(of(1), of(2), of(3)), is(of(asList(1, 2, 3))));
        assertThat(sequence(of(1)), is(of(asList(1))));
        assertThat(sequence(), is(of(emptyList())));
    }
}