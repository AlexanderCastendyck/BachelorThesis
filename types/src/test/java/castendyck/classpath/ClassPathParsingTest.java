package castendyck.classpath;


import castendyck.parameterizedtest.AbstractParameterizedTest;
import de.castendyck.enforcing.InvalidTypeValueException;
import de.castendyck.enforcing.ValueMustNotBeNullException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ClassPathParsingTest extends AbstractParameterizedTest<String, String> {

    public ClassPathParsingTest(String input, String expectedResult, Class<? extends Throwable>  expectedExceptionClass) {
        super(input, expectedResult, expectedExceptionClass);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"someClass.class", "someClass.class", null},
                {"directory/", "directory/", null},
                {"directory/someClass.class", "directory/someClass.class", null},
                {"directory/directory/", "directory/directory/", null},
                {"directory/directory/someClass.class", "directory/directory/someClass.class", null},
                {"java/lang/String", "java/lang/String", null},
                {"README", "README", null},
                {"META-INF/", "META-INF/", null},
                {"META-INF/MANIFEST.MF", "META-INF/MANIFEST.MF", null},
                {"class$innerClass.class", "class$innerClass.class", null},
                {"nope.xml", "nope.xml", null},
                {"META-INF/maven/com.google.collections/google-collections/pom.xml", "META-INF/maven/com.google.collections/google-collections/pom.xml", null},
                {"META-INF/maven/com.google.collections/google-collections/pom.properties", "META-INF/maven/com.google.collections/google-collections/pom.properties", null},
                {"sth/../here", "sth/../here", null},
                {"/nope", NO_EXPECTATION_NECESSARY, InvalidTypeValueException.class},
                {null, NO_EXPECTATION_NECESSARY, ValueMustNotBeNullException.class}
        });
    }

    @Override
    public String act(String input) throws Exception {
        return ClassPathFactory.createNew(input).asString();
    }
}