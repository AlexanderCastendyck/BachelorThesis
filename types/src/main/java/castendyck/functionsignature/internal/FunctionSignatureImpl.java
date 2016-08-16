package castendyck.functionsignature.internal;

import castendyck.functionsignature.FunctionSignature;

import java.util.regex.Pattern;

import static de.castendyck.enforcing.NotNullConstraintEnforcer.ensureNotNullOrEmptyString;
import static de.castendyck.enforcing.TypePatternEnforcer.ensureInputMatchesPattern;


public class FunctionSignatureImpl implements FunctionSignature {
    private final static Pattern PATTERN = Pattern.compile("\\([\\w/;\\[]*\\)[\\w/;\\[]+");
    private final String value;

    private FunctionSignatureImpl(String value) {
        this.value = value;
    }

    public static FunctionSignatureImpl parse(String input) {
        ensureNotNullOrEmptyString(input);
        ensureInputMatchesPattern(input, PATTERN);
        return new FunctionSignatureImpl(input);
    }

    @Override
    public String asValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionSignatureImpl that = (FunctionSignatureImpl) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "FunctionSignatureImpl{" +
                "value='" + value + '\'' +
                '}';
    }
}
