package castendyck.cwe.internal;

import castendyck.cwe.CweType;
import castendyck.parameterizedtest.AbstractParameterizedTest;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class CweTextToTypeParserTest extends AbstractParameterizedTest<String, CweType> {

    public CweTextToTypeParserTest(String input, CweType expectedResult, Class<? extends Throwable> expectedExceptionClass) {
        super(input, expectedResult, expectedExceptionClass);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"CWE-399 Resource Management Errors", CweType.CWE_399, null},
                {"CWE-79 Improper Neutralization of Input During Web Page Generation ('Cross-site Scripting')", CweType.CWE_79, null},
                {"CWE-119 Improper Restriction of Operations within the Bounds of a Memory Buffer", CweType.CWE_119, null},
                {"CWE-20 Improper Input Validation", CweType.CWE_20, null},
                {"CWE-246", CweType.CWE_246, null},
                {"CWE-123: with double point", CweType.CWE_123, null},
                {"CWE-42: with very uncommon signs (){}[]#+'*-\"§$%&/=?_<>", CweType.CWE_42, null},
                {"CWE-0: minimum", CweType.CWE_0, null},
                {"  CWE-21: whitespaces ", CweType.CWE_21, null},
                {"CWE-33 : extra whitespace before double point", CweType.CWE_33, null},
                {"CWE-500: current maximum", CweType.CWE_500, null},
                {"definitely not a CWE", CweType.CWE_UNKNOWN, null},
                {"CWE 45", CweType.CWE_UNKNOWN, null},
                {"CWE_31337: over maximum", CweType.CWE_UNKNOWN, null},
                {null, CweType.CWE_UNKNOWN, null}
        });
    }

    @Override
    public CweType act(String input) throws Exception {
        final CweType cweType = CweTextToTypeParser.parse(input);
        return cweType;
    }


}