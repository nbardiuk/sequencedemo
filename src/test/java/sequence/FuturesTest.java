package sequence;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static sequence.Futures.sequence;
import static sequence.Futures.futures;

public class FuturesTest {
    @Test public void shouldCollectWithDownstream() throws Exception {
        Stream<CompletableFuture<Integer>> stream = Stream.of(later(1), later(2), later(3), later(0));

        CompletableFuture<Map<Boolean, Set<Integer>>> result = stream.collect(futures(groupingBy(i -> i % 2 == 0, toSet())));

        Map<Boolean, Set<Integer>> expected = new HashMap<>();
        expected.put(true, new HashSet<>(asList(0, 2)));
        expected.put(false, new HashSet<>(asList(1, 3)));
        assertCompletes(result, is(expected));
    }

    @Test public void shouldCollectToFailedIfOneFailed() throws Exception {
        assertFailed(sequence(later(1), failed(), later(3)));
        assertFailed(sequence(failed()));
    }

    @Test public void shouldCollectToList() throws Exception {
        assertCompletes(sequence(later(1), later(2), later(3)), is(asList(1, 2, 3)));
        assertCompletes(sequence(later(1)), is(asList(1)));
        assertCompletes(sequence(), is(emptyList()));
    }

    private <T> void assertCompletes(CompletableFuture<T> sequence, Matcher<T> matcher) throws InterruptedException, ExecutionException, TimeoutException {
        assertThat(sequence.get(1, SECONDS), matcher);
    }


    private <T> CompletableFuture<T> later(T t) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return t;
        });
    }

    private <T> CompletableFuture<T> failed() {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(new Exception("Failed"));
        return future;
    }

    private <T> void assertFailed(CompletableFuture<T> result) {
        try {
            result.get(1, SECONDS);
            fail("Should throw exception");
        } catch (Throwable throwable) {
            assertThat(throwable.getCause().getMessage(), is("Failed"));
        }
    }
}