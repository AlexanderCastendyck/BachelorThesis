package castendyck.classidentifier;

import de.castendyck.enforcing.InvalidTypeValueException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;


public class ClassIdentifierTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void constructor_canOnlyBeCalledWithValidClassFile() throws Exception {
        String className = "someClass.class";

        final ClassIdentifier classIdentifier = new ClassIdentifier(className);

        final String resultedClassName = classIdentifier.getClassName();
        Assert.assertThat(resultedClassName, equalTo(className));
    }

    @Test
    public void constructor_canNotBeCalledWithDirectory() throws Exception {
        String className = "aDirectory/";

        expectedException.expect(InvalidTypeValueException.class);
        new ClassIdentifier(className);
    }

    @Test
    public void constructor_canNotBeCalledWithAbitraryLocation() throws Exception {
        String className = "randomLocation";

        expectedException.expect(InvalidTypeValueException.class);
        new ClassIdentifier(className);
    }

    @Test
    public void getClassName_returnsCorrectClassName() throws Exception {
        String className = "package1/package2/someClass.class";

        final ClassIdentifier classIdentifier = new ClassIdentifier(className);

        final String resultedClassName = classIdentifier.getClassName();
        Assert.assertThat(resultedClassName, equalTo("someClass.class"));
    }

    @Test
    public void equals_returnsTrueForSamePaths() throws Exception {
        String classNameA = "package1/package2/someClass.class";
        String classNameB = "package1/package2/someClass.class";

        final ClassIdentifier classIdentifierA = new ClassIdentifier(classNameA);
        final ClassIdentifier classIdentifierB = new ClassIdentifier(classNameB);

        final boolean comparison = classIdentifierA.equals(classIdentifierB);
        Assert.assertThat(comparison, is(true));
    }
}