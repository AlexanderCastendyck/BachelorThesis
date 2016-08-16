package castendyck.functionname.internal;

import de.castendyck.enforcing.NotNullConstraintEnforcer;
import de.castendyck.enforcing.TypePatternEnforcer;
import castendyck.functionname.FunctionName;

import java.util.regex.Pattern;

public class FunctionNameImpl implements FunctionName {
    private static final Pattern CLASS_FILE_PATTERN = Pattern.compile("([a-zA-Z_$][\\w_$]*|<init>)");
    private final String path;

    private FunctionNameImpl(String path) {
        this.path = path;
    }

    public static FunctionName parse(String input) {
        NotNullConstraintEnforcer.ensureNotNullOrEmptyString(input);
        TypePatternEnforcer.ensureInputMatchesPattern(input, CLASS_FILE_PATTERN);

        final FunctionName functionName = new FunctionNameImpl(input);
        return functionName;
    }

    @Override public String asString() {
        return this.path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        final FunctionNameImpl that = (FunctionNameImpl) o;

        return this.path.equals(that.path);

    }

    @Override
    public int hashCode() {
        return this.path.hashCode();
    }

    @Override
    public String toString() {
        return this.path;
    }
}
