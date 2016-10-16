package demo;

import java.util.Optional;

import static java.lang.System.out;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static sequence.Optionals.sequence;

class OptionalDemo {

    static void run() {
        out.println("\n## Optional\n");
        out.println(sequence(parse("1"), parse("2"), parse("3"))); // Optional[[1, 2, 3]]
        out.println(sequence(parse("1"), parse("X"), parse("3"))); // Optional.empty
    }

    static Optional<Integer> parse(String number) {
        try {
            return ofNullable(Integer.parseInt(number));
        } catch (NumberFormatException ex) {
            return empty();
        }
    }
}
