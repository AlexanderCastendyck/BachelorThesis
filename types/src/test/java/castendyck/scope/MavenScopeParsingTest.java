package castendyck.scope;

import castendyck.parameterizedtest.AbstractParameterizedTest;
import de.castendyck.enforcing.InvalidTypeValueException;
import de.castendyck.enforcing.ValueMustNotBeNullException;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class MavenScopeParsingTest extends AbstractParameterizedTest<String, String> {

    public MavenScopeParsingTest(String input, String expectedResult, Class<? extends Throwable>  expectedExceptionClass) {
        super(input, expectedResult, expectedExceptionClass);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"compile", "compile", null},
                {"provided", "provided", null},
                {"runtime", "runtime", null},
                {"test", "test", null},
                {"system", "system", null},
                {"import", "import", null},
                {"Compile", "Compile", null},
                {"PROVIDED", "PROVIDED", null},
                {"runTiME", "runTiME", null},
                {"somethingElse", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"CompileAndTest", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"123", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"", NO_EXPECTATION_NECESSARY, ValueMustNotBeNullException.class},
                {null, NO_EXPECTATION_NECESSARY, ValueMustNotBeNullException.class}
        });
    }

    @Override
    public String act(String input) throws Exception {
        final MavenScope mavenScope = MavenScopeFactory.createNew(input);
        return mavenScope.asString();
    }
}