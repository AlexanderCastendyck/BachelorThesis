package castendyck.classfile;

import castendyck.parameterizedtest.AbstractParameterizedTest;
import de.castendyck.enforcing.InvalidTypeValueException;
import de.castendyck.enforcing.ValueMustNotBeNullException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ClassFileParsingTest extends AbstractParameterizedTest<String, String> {

    public ClassFileParsingTest(String input, String expectedResult, Class<? extends Throwable>  expectedExceptionClass) {
        super(input, expectedResult, expectedExceptionClass);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"someClass.class", "someClass.class", null},
                {"directory/someClass.class", "directory/someClass.class", null},
                {"directory/directory/someClass.class", "directory/directory/someClass.class", null},
                {"class$innerClass.class", "class$innerClass.class", null},
                {"java/lang/String", "java/lang/String", null},
                {"directory/", AbstractParameterizedTest.NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"README", AbstractParameterizedTest.NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"META-INF/MANIFEST.MF", AbstractParameterizedTest.NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"nope.xml", AbstractParameterizedTest.NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {"/nope", AbstractParameterizedTest.NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {null, AbstractParameterizedTest.NO_EXPECTATION_NECESSARY, ValueMustNotBeNullException.class}
        });
    }

    @Override
    public String act(String input) throws Exception {
        final ClassFile classFile = ClassFileFactory.createNew(input);
        return classFile.asString();
    }

}