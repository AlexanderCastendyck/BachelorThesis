package castendyck.callgraph.functiondata.internal.classpathtoartifactmapstoring;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierForJavaApi;
import castendyck.classpath.ClassPath;
import castendyck.classpath.ClassPathFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static castendyck.artifactidentifier.ArtifactIdentifierBuilder.anArtifactIdentifier;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ClassPathsToArtifactIdentifierStorageTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ClassPathsToArtifactIdentifierStorage storage;
    private ArtifactIdentifier defaultArtifactIdentifier;

    @Before
    public void setUp() throws Exception {
        storage = new ClassPathsToArtifactIdentifierStorage();
        defaultArtifactIdentifier = anArtifactIdentifier()
                .withGroupId("someGroup")
                .withArtifactId("artifactId")
                .withVersion("1.0")
                .build();
    }

    @Test
    public void storeAndGetArtifactIdentifier_withOneClassPath() throws Exception {
        final List<ClassPath> classPaths = classPathsFor("classPath");

        storage.storeInformationForArtifactIdentifier(classPaths, defaultArtifactIdentifier);

        assertThat(storedArtifactIdentifierForClassPath("classPath"), equalTo(defaultArtifactIdentifier));
    }

    @Test
    public void storeAndGetArtifactIdentifier_threeClassPath() throws Exception {
        final List<ClassPath> classPaths = classPathsFor("classPath1", "classPath2", "classPath3");

        storage.storeInformationForArtifactIdentifier(classPaths, defaultArtifactIdentifier);


        assertThat(storedArtifactIdentifierForClassPath("classPath1"), equalTo(defaultArtifactIdentifier));
        assertThat(storedArtifactIdentifierForClassPath("classPath2"), equalTo(defaultArtifactIdentifier));
        assertThat(storedArtifactIdentifierForClassPath("classPath3"), equalTo(defaultArtifactIdentifier));
    }

    @Test
    public void storeAndGetArtifactIdentifier_withTwoDifferentArtifactIdentifier() throws Exception {
        final List<ClassPath> classPathsA = classPathsFor("classPathA");
        storage.storeInformationForArtifactIdentifier(classPathsA, defaultArtifactIdentifier);

        final List<ClassPath> classPathsB = classPathsFor("classPathB");
        final ArtifactIdentifier differentArtifactIdentifier = anArtifactIdentifier()
                .withGroupId("differentGroup")
                .withArtifactId("differentArtifact")
                .withVersion("1.0")
                .build();
        storage.storeInformationForArtifactIdentifier(classPathsB, differentArtifactIdentifier);


        assertThat(storedArtifactIdentifierForClassPath("classPathA"), equalTo(defaultArtifactIdentifier));
        assertThat(storedArtifactIdentifierForClassPath("classPathB"), equalTo(differentArtifactIdentifier));
    }

    @Test
    public void storeAndGetArtifactIdentifier_ClassWithinStandardJavaApiAreStoredUnderJavaApiArtifactIdentifier() throws Exception {
        final List<ClassPath> classPaths = classPathsFor("classPath1", "java/lang/String");

        storage.storeInformationForArtifactIdentifier(classPaths, defaultArtifactIdentifier);

        assertThat(storedArtifactIdentifierForClassPath("classPath1"), equalTo(defaultArtifactIdentifier));
        assertThat(storedArtifactIdentifierForClassPath("java/lang/String"), equalTo(ArtifactIdentifierForJavaApi.createNew()));
    }

    @Test
    public void getArtifactIdentifier_failsForNotExistingArtifactIdentifier() throws Exception {
        final ClassPath classPath = ClassPathFactory.createNew("notExisting/");
        expectedException.expect(NoArtifactIdentifierRegisteredForThisClassPathException.class);
        storage.getArtifactIdentifierFor(classPath);

    }

    private ArtifactIdentifier storedArtifactIdentifierForClassPath(String path) throws NoArtifactIdentifierRegisteredForThisClassPathException {
        final ClassPath classPath = ClassPathFactory.createNew(path);
        final ArtifactIdentifier artifactIdentifierFor = storage.getArtifactIdentifierFor(classPath);
        return artifactIdentifierFor;
    }

    private List<ClassPath> classPathsFor(String... paths) {
        final List<ClassPath> classPaths = Arrays.stream(paths)
                .map(ClassPathFactory::createNew)
                .collect(Collectors.toList());
        return classPaths;
    }
}