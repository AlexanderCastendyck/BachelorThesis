package castendyck.classpath;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;


public class ClassPathTest {

    @Test
    public void getClassName_returnsCorrectClassName() throws Exception {
        ClassPath classPath = ClassPathFactory.createNew("package/name.class");

        final String className = classPath.getClassName();

        Assert.assertThat(className, equalTo("name.class"));
    }

    @Test
    public void getClassName_returnsCorrectClassNameWithinSeveralPackages() throws Exception {
        ClassPath classPath = ClassPathFactory.createNew("package1/package2/package3/name.class");

        final String className = classPath.getClassName();

        Assert.assertThat(className, equalTo("name.class"));
    }

    @Test
    public void getClassName_returnsCorrectClassNameWhenClassOnTopLevelDirectory() throws Exception {
        ClassPath classPath = ClassPathFactory.createNew("name.class");

        final String className = classPath.getClassName();

        Assert.assertThat(className, equalTo("name.class"));
    }

    @Test
    public void getPackagePart_returnsCorrectPackage() throws Exception {
        ClassPath classPath = ClassPathFactory.createNew("package/name.class");

        final String packagePart = classPath.getPackagePart();

        Assert.assertThat(packagePart, equalTo("package/"));
    }

    @Test
    public void getPackagePart_returnsCorrectPackages() throws Exception {
        ClassPath classPath = ClassPathFactory.createNew("package1/package2/name.class");

        final String packagePart = classPath.getPackagePart();

        Assert.assertThat(packagePart, equalTo("package1/package2/"));
    }

    @Test
    public void getPackagePart_returnsEmptyPackageForClassOnTopLevelDirectory() throws Exception {
        ClassPath classPath = ClassPathFactory.createNew("name.class");

        final String packagePart = classPath.getPackagePart();

        Assert.assertThat(packagePart, equalTo(""));
    }

    @Test
    public void isClassFile_returnsTrueForClassFile() throws Exception {
        ClassPath classPath = ClassPathFactory.createNew("name.class");

        final boolean isClassFile = classPath.isClassFile();

        Assert.assertThat(isClassFile, is(true));
    }

    @Test
    public void isClassFile_returnsFalseForDirectoryClassFile() throws Exception {
        ClassPath classPath = ClassPathFactory.createNew("name/");

        final boolean isClassFile = classPath.isClassFile();

        Assert.assertThat(isClassFile, is(false));
    }

    @Test
    public void isClassFile_returnsFalseForAbitraryClassFile() throws Exception {
        ClassPath classPath = ClassPathFactory.createNew("name");

        final boolean isClassFile = classPath.isClassFile();

        Assert.assertThat(isClassFile, is(false));
    }

    @Test
    public void equals_comparesClassPaths() throws Exception {
        ClassPath classPath1 = ClassPathFactory.createNew("package/name.class");
        ClassPath classPath2 = ClassPathFactory.createNew("package/name.class");

        final boolean comparison = classPath1.equals(classPath2);
        Assert.assertThat(comparison, is(true));
    }
}