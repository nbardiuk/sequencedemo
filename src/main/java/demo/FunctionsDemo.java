package demo;

import static demo.FunctionsDemo.Person.person;
import static java.lang.System.out;
import static sequence.BiFunctions.sequence;
import static sequence.Functions.sequence;

class FunctionsDemo {
    static void run() {
        out.println("\n## Functions\n");
        out.println(sequence(Person::name, Person::surname).apply(person("John", "Doe")));     // [John, Doe]
        out.println(sequence(Integer::sum, Integer::max, Integer::min).apply(100, 200));  // [300, 200, 100]
    }

    interface Person {
        static Person person(String name, String surname) {
            return new Person() {
                @Override public String name() {
                    return name;
                }

                @Override public String surname() {
                    return surname;
                }
            };
        }

        String name();

        String surname();
    }
}
