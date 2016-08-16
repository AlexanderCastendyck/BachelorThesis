package castendyck.maventestpathidentifier;

import de.castendyck.enforcing.InvalidTypeValueException;
import de.castendyck.enforcing.ValueMustNotBeNullException;
import castendyck.parameterizedtest.AbstractParameterizedTest;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class MavenTestPathIdentifierParsingTest extends AbstractParameterizedTest<String, String> {

    public MavenTestPathIdentifierParsingTest(String input, String expectedResult, Class<? extends Throwable> expectedExceptionClass) {
        super(input, expectedResult, expectedExceptionClass);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"src/test/java/", "src/test/java/", null},
                {"src/testIT/java/", "src/testIT/java/", null},
                {"src/test_IT/java/", "src/test_IT/java/", null},
                {"src/test-IT/java/", "src/test-IT/java/", null},
                {"test/", "test/", null},
                {"test2/", "test2/", null},
                {"notEndingWithSlash", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"/noAbsolutePathsAllowed", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"/andDefinitelyNotBoth", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"", NO_EXPECTATION_NECESSARY, ValueMustNotBeNullException.class},
                {null, NO_EXPECTATION_NECESSARY, ValueMustNotBeNullException.class}
        });
    }

    @Override
    public String act(String input) throws Exception {
        final MavenTestPathIdentifier mavenTestPathIdentifier = MavenTestPathIdentifier.parse(input);
        final String value = mavenTestPathIdentifier.asValue();
        return value;
    }

}