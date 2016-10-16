package demo;

import sequence.Futures;
import sequence.Optionals;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static demo.FuturesDemo.*;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static sequence.Futures.futures;
import static sequence.Optionals.optionals;

class OptionalFuture {
    static void run() {

        out.println("\n## Future Optional \n");

        List<CompletableFuture<Optional<Integer>>> allPresent = asList(async(of(1)), async(of(2)), async(of(3)));
        out.println(show(allPresent.stream().collect(futures(toList()))));               // CompletableFuture[[Optional[1], Optional[2], Optional[3]]]
        out.println(show(allPresent.stream().collect(futures(optionals(toList())))));    // CompletableFuture[Optional[[1, 2, 3]]]

        out.println();
        List<CompletableFuture<Optional<Integer>>> someEmpty = asList(async(of(1)), async(empty()), async(of(3)));
        out.println(show(someEmpty.stream().collect(futures(toList()))));                //CompletableFuture[[Optional[1], Optional.empty, Optional[3]]]
        out.println(show(someEmpty.stream().collect(futures(optionals(toList())))));     //CompletableFuture[Optional.empty]

        out.println();
        List<CompletableFuture<Optional<Integer>>> someFailed = asList(async(of(1)), failed(), async(of(3)));
        out.println(show(someFailed.stream().collect(futures(toList()))));               //CompletableFuture.failed
        out.println(show(someFailed.stream().collect(futures(optionals(toList())))));      //CompletableFuture.failed

        out.println();
        List<Optional<CompletableFuture<Integer>>> allWillPresent = asList(of(async(1)), of(async(2)), of(async(3)));
        out.println(showl(allWillPresent.stream().collect(optionals(toList()))));          //Optional[CompletableFuture[1], CompletableFuture[2], CompletableFuture[3]]
        out.println(showc(allWillPresent.stream().collect(optionals(futures(toList()))))); //Optional[CompletableFuture[[1, 2, 3]]]

        out.println();
        List<Optional<CompletableFuture<Integer>>> someWillfiail = asList(of(async(1)), of(failed()), of(async(3)));
        out.println(showl(someWillfiail.stream().collect(optionals(toList()))));          //Optional[CompletableFuture[1], CompletableFuture.failed, CompletableFuture[3]]
        out.println(showc(someWillfiail.stream().collect(optionals(futures(toList()))))); //Optional[CompletableFuture.failed]

        out.println();
        List<Optional<CompletableFuture<Integer>>> someWillempty = asList(of(async(1)), empty(), of(async(3)));
        out.println(someWillempty.stream().collect(optionals(toList())));                 //Optional.empty
        out.println(someWillempty.stream().collect(optionals(futures(toList()))));        //Optional.empty
    }

    private static <T> String showc(Optional<CompletableFuture<List<T>>> collect) {
        return collect.map(FuturesDemo::show).toString();
    }

    private static <T> String showl(Optional<List<CompletableFuture<T>>> collect) {
        return collect.map(OptionalFuture::showAll).toString();
    }

    private static <T> String showAll(List<CompletableFuture<T>> fs) {
        return fs.stream().map(FuturesDemo::show).collect(joining(", "));
    }
}
