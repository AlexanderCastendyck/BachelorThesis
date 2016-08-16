package de.castendyck.collections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static de.castendyck.collections.ObjectListToStringCollector.collectToString;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class ObjectListToStringCollectorTest {

    @Test
    public void testForEmptyList() throws Exception {
        final List<String> list = new ArrayList<>();

        final String collectedString = act(list);

        assertThat(collectedString, equalTo("()"));
    }

    @Test
    public void testForOneElementalStringList() throws Exception {
        final List<String> list = asList(
                "Hello"
        );

        final String collectedString = act(list);

        assertThat(collectedString, equalTo("(Hello)"));
    }

    @Test
    public void testForOneElementalObjectList() throws Exception {
        final List<Object> list = asList(
                new ToStringTestObject("Object1")
        );

        final String collectedString = act(list);

        assertThat(collectedString, equalTo("(Object1)"));
    }

    @Test
    public void testForThreeElementalStringList() throws Exception {
        final List<String> list = asList(
                "One",
                "Two",
                "Three"
        );

        final String collectedString = act(list);

        assertThat(collectedString, equalTo("(One, Two, Three)"));
    }

    @Test
    public void testForThreeElementalObjectList() throws Exception {
        final List<Object> list = asList(
                new ToStringTestObject("A"),
                new ToStringTestObject("B"),
                new ToStringTestObject("C")
        );

        final String collectedString = act(list);

        assertThat(collectedString, equalTo("(A, B, C)"));
    }

    private String act(List<?> list) {
        return list.stream()
                .collect(collectToString());
    }

    private class ToStringTestObject{
        private final String value;

        private ToStringTestObject(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}