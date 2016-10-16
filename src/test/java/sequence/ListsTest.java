package sequence;

import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.of;
import static java.util.stream.Collectors.maxBy;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static sequence.Lists.lists;
import static sequence.Lists.sequence;

public class ListsTest {
    @Test
    public void shouldCartesianProductLists() throws Exception {
        assertThat(sequence(asList(1, 2), asList(10, 20), asList(100, 200)),
                is(asList(
                        asList(1, 10, 100),
                        asList(1, 10, 200),
                        asList(1, 20, 100),
                        asList(1, 20, 200),
                        asList(2, 10, 100),
                        asList(2, 10, 200),
                        asList(2, 20, 100),
                        asList(2, 20, 200))));

        assertThat(sequence(asList(1, 2), asList(10, 20)),
                is(asList(
                        asList(1, 10),
                        asList(1, 20),
                        asList(2, 10),
                        asList(2, 20))));

        assertThat(sequence(asList(1), asList(10, 20)),
                is(asList(
                        asList(1, 10),
                        asList(1, 20))));

        assertThat(sequence(asList(1, 2), asList(10)),
                is(asList(
                        asList(1, 10),
                        asList(2, 10))));
    }

    @Test
    public void shouldBeEmptyIfOneIsEmpty() throws Exception {
        assertThat(sequence(asList(1, 2), asList(3, 4), emptyList()), is(emptyList()));
        assertThat(sequence(emptyList(), asList(1, 2)), is(emptyList()));
    }

    @Test
    public void shouldPerformDownstreamCollection() throws Exception {
        List<Optional<Integer>> result = Stream.of(asList(1, 2), asList(10, 20))
                .collect(lists(maxBy(Integer::compare)));
        assertThat(result, is(asList(of(10), of(20), of(10), of(20))));
    }
}