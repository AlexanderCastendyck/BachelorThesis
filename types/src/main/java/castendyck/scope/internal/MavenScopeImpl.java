package castendyck.scope.internal;

import castendyck.scope.MavenScope;

import java.util.regex.Pattern;

import static de.castendyck.enforcing.NotNullConstraintEnforcer.ensureNotNullOrEmptyString;
import static de.castendyck.enforcing.TypePatternEnforcer.ensureInputMatchesPattern;

public class MavenScopeImpl implements MavenScope {
    private static final Pattern MAVEN_SCOPE_PATTERN = Pattern.compile("(compile|provided|runtime|test|system|import|none)");

    private final String value;

    private MavenScopeImpl(String input) {
        this.value = input;
    }

    public static MavenScopeImpl parse(String input) {
        ensureNotNullOrEmptyString(input);
        final String lowerCaseInput = input.toLowerCase();
        ensureInputMatchesPattern(lowerCaseInput, MAVEN_SCOPE_PATTERN);
        return new MavenScopeImpl(input);
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MavenScopeImpl that = (MavenScopeImpl) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "MavenScopeImpl{" +
                "value='" + value + '\'' +
                '}';
    }
}
