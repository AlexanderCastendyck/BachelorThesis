package castendyck.maventestpathidentifier;

import java.util.regex.Pattern;

import static de.castendyck.enforcing.NotNullConstraintEnforcer.ensureNotNullOrEmptyString;
import static de.castendyck.enforcing.TypePatternEnforcer.ensureInputMatchesPattern;

public class MavenTestPathIdentifier {
    private final static Pattern PATTERN = Pattern.compile("(([\\w_ \\.-]+)/)+");
    private final String value;

    private MavenTestPathIdentifier(String value) {
        this.value = value;
    }

    public static MavenTestPathIdentifier parse(String input) {
        ensureNotNullOrEmptyString(input);
        ensureInputMatchesPattern(input, PATTERN);
        return new MavenTestPathIdentifier(input);
    }

    public String asValue() {
        return value;
    }
}
