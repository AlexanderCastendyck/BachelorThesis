package castendyck.sourcecode.sourecodefilecollecting;

import castendyck.javasourcefile.JavaSourceFile;

import java.nio.file.Path;
import java.util.List;

public class When {
    private final TestClassesCollector testClassesCollector;
    private final Path startingPath;
    private final List<JavaSourceFile> expectedTestClasses;

    public When(TestClassesCollector testClassesCollector, Path startingPath, List<JavaSourceFile> expectedTestClasses) {
        this.testClassesCollector = testClassesCollector;
        this.startingPath = startingPath;
        this.expectedTestClasses = expectedTestClasses;
    }

    public Then whenTestFilesAreCollected() {
        final List<JavaSourceFile> foundTestClasses = testClassesCollector.collectFilesFromMavenTestRepository(startingPath);
        return new Then(foundTestClasses, expectedTestClasses);
    }
}
