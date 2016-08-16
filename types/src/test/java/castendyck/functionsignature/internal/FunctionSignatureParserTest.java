package castendyck.functionsignature.internal;

import de.castendyck.enforcing.InvalidTypeValueException;
import de.castendyck.enforcing.ValueMustNotBeNullException;
import castendyck.parameterizedtest.AbstractParameterizedTest;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class FunctionSignatureParserTest extends AbstractParameterizedTest<String, String> {

    public FunctionSignatureParserTest(String input, String expectedResult, Class<? extends Throwable> expectedExceptionClass) {
        super(input, expectedResult, expectedExceptionClass);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"()V", "()V", null},
                {"(I)V", "(I)V", null},
                {"()D", "()D", null},
                {"(Ljava/lang/String;[[J)I", "(Ljava/lang/String;[[J)I", null},
                {"(Lsome/arbitrary/Object;)Lsome/different/Object;", "(Lsome/arbitrary/Object;)Lsome/different/Object;", null},
                {"([[[[J[[D)[[[[[[[Z", "([[[[J[[D)[[[[[[[Z", null},
                {"notASignature", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"()", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"(()", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"", NO_EXPECTATION_NECESSARY, ValueMustNotBeNullException.class},
                {null, NO_EXPECTATION_NECESSARY, ValueMustNotBeNullException.class}
        });
    }

    @Override
    public String act(String input) throws Exception {
        final FunctionSignatureImpl functionSignature = FunctionSignatureImpl.parse(input);
        final String value = functionSignature.asValue();
        return value;
    }
}