package demo;

import sequence.Optionals;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static sequence.Futures.futures;
import static sequence.Futures.sequence;

class FuturesDemo {

    static void run() {
        out.println("\n## CompletableFuture\n");
        out.println(show(sequence(async(1), async(2), async(3))));                        // CompletableFuture[[1, 2, 3]]
        out.println(show(sequence(async(1), failed(), async(3))));                        // CompletableFuture.failed
        out.println(show(sequence(async(1), async(2), async(3)).thenApply(l -> sum(l)))); // CompletableFuture[6]


        List<CompletableFuture<Optional<Integer>>> allPresent = asList(async(of(1)), async(of(2)), async(of(3)));
        out.println(show(allPresent.stream().collect(futures((toList())))));                     // CompletableFuture[[Optional[1], Optional[2], Optional[3]]]
        out.println(show(allPresent.stream().collect(futures(Optionals.optionals(toList()))))); // CompletableFuture[Optional[[1, 2, 3]]]

        List<CompletableFuture<Optional<Integer>>> someEmpty = asList(async(of(1)), async(empty()), async(of(3)));
        out.println(show(someEmpty.stream().collect(futures((toList())))));                     //CompletableFuture[[Optional[1], Optional.empty, Optional[3]]]
        out.println(show(someEmpty.stream().collect(futures(Optionals.optionals(toList()))))); //CompletableFuture[Optional.empty]
    }

    private static Integer sum(List<Integer> integers) {
        return integers.stream().mapToInt(i -> i).sum();
    }

    static <T> String show(CompletableFuture<T> future) {
        try {
            return String.format("CompletableFuture[%s]", future.get(1, TimeUnit.SECONDS));
        } catch (Throwable e) {
            return "CompletableFuture.failed";
        }
    }

    static <T> CompletableFuture<T> async(T t) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return t;
        });
    }

    static <T> CompletableFuture<T> failed() {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(new Exception("Failed"));
        return future;
    }
}
