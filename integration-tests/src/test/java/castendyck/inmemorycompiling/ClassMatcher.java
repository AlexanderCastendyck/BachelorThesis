package castendyck.inmemorycompiling;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.List;

class ClassMatcher extends TypeSafeDiagnosingMatcher<java.lang.Class> {
    private final String expectedClassName;
    private final List<String> expectedMethods;

    private ClassMatcher(String name) {
        this.expectedClassName = name;
        this.expectedMethods = new ArrayList<>();
    }

    public static ClassMatcher isAClassNamed(String name) {
        return new ClassMatcher(name);
    }

    public ClassMatcher containingMethods(String... methodNames) {
        for (String methodName : methodNames) {
            containingMethod(methodName);
        }
        return this;
    }

    public ClassMatcher containingMethod(String methodName) {
        expectedMethods.add(methodName);
        return this;
    }

    @Override
    protected boolean matchesSafely(java.lang.Class aClass, Description description) {
        final String nameOfClass = aClass.getName();
        if (!nameOfClass.equals(expectedClassName)) {
            return false;
        }
        for (String expectedMethod : expectedMethods) {
            try {
                aClass.getMethod(expectedMethod);
            } catch (NoSuchMethodException e) {
                final String message = "Method " + expectedMethod + "not found in Class named " + expectedClassName;
                throw new AssertionError(message, e);
            }
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {

    }

}
