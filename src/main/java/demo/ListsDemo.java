package demo;

import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static sequence.Lists.sequence;

class ListsDemo {
    static void run() {
        out.println("\n## List\n");
        out.println(sequence(asList(1, 2), asList(10, 20), asList(100)));  // [[1, 10, 100], [1, 20, 100], [2, 10, 100], [2, 20, 100]]
        out.println(sequence(asList(1, 2), emptyList(), asList(10, 20)));  // []
        out.println(sequence(asList("J", "Q", "K", "A"), asList("Clubs", "Diamonds", "Hearts", "Spades"))); // [[J, Clubs], [J, Diamonds], [J, Hearts], [J, Spades], [Q, Clubs], [Q, Diamonds], [Q, Hearts], [Q, Spades], [K, Clubs], [K, Diamonds], [K, Hearts], [K, Spades], [A, Clubs], [A, Diamonds], [A, Hearts], [A, Spades]]
    }


}
