package castendyck.sourcecode.sourecodefilecollecting;

import castendyck.javasourcefile.JavaSourceFile;

import java.nio.file.Path;
import java.util.List;

public class Given {
    public static When given(TestClassesCollectorTestExamples.TestClassesCollectorTestExample testClassesCollectorTestExample) {
        final TestClassesCollector testClassesCollector = testClassesCollectorTestExample.getTestClassesCollector();
        final Path startingPath = testClassesCollectorTestExample.getStartingPath();
        final List<JavaSourceFile> expectedTestClasses = testClassesCollectorTestExample.getExpectedTestClasses();
        return new When(testClassesCollector, startingPath, expectedTestClasses);
    }
}
