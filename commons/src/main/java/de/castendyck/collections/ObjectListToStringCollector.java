package de.castendyck.collections;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ObjectListToStringCollector<R> implements Collector<R, StringBuilder , String> {

    public static String collectToString(Collection<?> collection){
        final String string = collection.stream()
                .collect(collectToString());
        return string;
    }

    public static ObjectListToStringCollector<Object> collectToString(){
        return new ObjectListToStringCollector<>();
    }

    @Override
    public Supplier<StringBuilder> supplier() {
        return () -> new StringBuilder().append("(");
    }

    @Override
    public BiConsumer<StringBuilder, R> accumulator() {
        return (strb, r) -> strb.append(", ").append(r.toString());
    }

    @Override
    public BinaryOperator<StringBuilder> combiner() {
        return null;
    }

    @Override
    public Function<StringBuilder, String> finisher() {
         return (strb) -> strb.append(")").toString().replaceFirst(", ","");
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
