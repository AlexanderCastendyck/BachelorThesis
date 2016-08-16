package castendyck.functionname;

import castendyck.parameterizedtest.AbstractParameterizedTest;
import de.castendyck.enforcing.InvalidTypeValueException;
import de.castendyck.enforcing.ValueMustNotBeNullException;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class FunctionNameParsingTest extends AbstractParameterizedTest<String, String> {

    public FunctionNameParsingTest(String input, String expectedResult, Class<? extends Throwable>  expectedExceptionClass) {
        super(input, expectedResult, expectedExceptionClass);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"function", "function", null},
                {"FUNCTION", "FUNCTION", null},
                {"function_too", "function_too", null},
                {"function123", "function123", null},
                {"function$too", "function$too", null},
                {"$function", "$function", null},
                {"<init>", "<init>", null},
                {"someClass.class", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"directory/", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"123", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"here.there", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"", NO_EXPECTATION_NECESSARY, ValueMustNotBeNullException.class},
                {null, NO_EXPECTATION_NECESSARY, ValueMustNotBeNullException.class}
        });
    }

    @Override
    public String act(String input) throws Exception {
        final FunctionName functionName = FunctionNameFactory.createNew(input);
        return functionName.asString();
    }

}