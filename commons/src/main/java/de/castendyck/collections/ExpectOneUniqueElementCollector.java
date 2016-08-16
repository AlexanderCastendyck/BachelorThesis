package de.castendyck.collections;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ExpectOneUniqueElementCollector<T> implements Collector<T, List<T>, T> {
    private ExpectOneUniqueElementCollector() {
    }

    public static <T> ExpectOneUniqueElementCollector<T> expectOneUniqueElement() {
        return new ExpectOneUniqueElementCollector<>();
    }

    @Override
    public Supplier<List<T>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Function<List<T>, T> finisher() {
        return ts -> {
            if(ts.size() == 0){
                throw new EmptyCollectionException("Expected one element, but collection was empty");
            }
            if (ts.size() == 1) {
                return ts.get(0);
            } else {
                final Object listAsString = ts.stream()
                        .map(T::toString)
                        .collect(Collectors.joining(","));
                throw new OneUniqueElementExpectedException("Expected one element, but got: " + listAsString, ts);
            }
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
