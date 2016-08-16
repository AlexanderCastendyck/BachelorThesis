package castendyck.classfile;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;


public class ClassFileTest {

    @Test
    public void getClassName_returnsCorrectClassName() throws Exception {
        ClassFile classFile = ClassFileFactory.createNew("package/name.class");

        final String className = classFile.getClassName();

        Assert.assertThat(className, equalTo("name.class"));
    }

    @Test
    public void getClassName_returnsCorrectClassNameWithinSeveralPackages() throws Exception {
        ClassFile classFile = ClassFileFactory.createNew("package1/package2/package3/name.class");

        final String className = classFile.getClassName();

        Assert.assertThat(className, equalTo("name.class"));
    }

    @Test
    public void getClassName_returnsCorrectClassNameWhenClassOnTopLevelDirectory() throws Exception {
        ClassFile classFile = ClassFileFactory.createNew("name.class");

        final String className = classFile.getClassName();

        Assert.assertThat(className, equalTo("name.class"));
    }
    @Test
    public void getPackagePart_returnsCorrectPackage() throws Exception {
        ClassFile classFile = ClassFileFactory.createNew("package/name.class");

        final String packagePart = classFile.getPackagePart();

        Assert.assertThat(packagePart, equalTo("package/"));
    }

    @Test
    public void getPackagePart_returnsCorrectPackages() throws Exception {
        ClassFile classFile = ClassFileFactory.createNew("package1/package2/name.class");

        final String packagePart = classFile.getPackagePart();

        Assert.assertThat(packagePart, equalTo("package1/package2/"));
    }

    @Test
    public void getPackagePart_returnsEmptyPackageForClassOnTopLevelDirectory() throws Exception {
        ClassFile classFile = ClassFileFactory.createNew("name.class");

        final String packagePart = classFile.getPackagePart();

        Assert.assertThat(packagePart, equalTo(""));
    }
}