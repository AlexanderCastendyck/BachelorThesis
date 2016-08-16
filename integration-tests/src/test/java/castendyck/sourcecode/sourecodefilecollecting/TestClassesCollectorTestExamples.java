package castendyck.sourcecode.sourecodefilecollecting;

import castendyck.directorybuilding.DirectoryBuilder;
import castendyck.filebuilding.FileBuilder;
import castendyck.javasourcefile.JavaSourceFile;
import castendyck.javasourcefile.JavaSoureFileFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestClassesCollectorTestExamples {

    public static TestClassesCollectorTestExample anEmptyDirectory() throws Exception {
        final TestClassesCollector testClassesCollector = TestClassesCollectorFactory.newInstanceForDefaultMavenTestPath();
        final Path startingPath = DirectoryBuilder.aDirectory("target/TestClassesCollectorTestExamples/anEmptyDirectory/")
                .build();

        final List<JavaSourceFile> expectedTestClasses = new ArrayList<>();
        return new TestClassesCollectorTestExample(testClassesCollector, startingPath, expectedTestClasses);
    }

    public static TestClassesCollectorTestExample directoryTreeWithOneFileInTestDirectory() throws Exception {
        final Path startingPath = DirectoryBuilder.aDirectory("target/TestClassesCollectorTestExamples/directoryTreeWithOneFileInTestDirectory/")
                .with(DirectoryBuilder.aSubDirectory("src/test/java/")
                        .with(FileBuilder.aFile("testFile.java")))
                .build();

        final Path customTestRootPath = Paths.get("target/TestClassesCollectorTestExamples/directoryTreeWithOneFileInTestDirectory/src/test/java/");
        final TestClassesCollector testClassesCollector = TestClassesCollectorFactory.newInstanceWithCustomTestPath(customTestRootPath);
        final List<JavaSourceFile> expectedTestClasses = Arrays.asList(
                aJavaFile(startingPath, "src/test/java/testFile.java")
        );
        return new TestClassesCollectorTestExample(testClassesCollector, startingPath, expectedTestClasses);
    }

    public static TestClassesCollectorTestExample directoryTreeWithNoFilesInTestDirectoryButSeveralFilesInSourceDirectory() throws Exception {
        final String rootDir = "target/TestClassesCollectorTestExamples/directoryTreeWithNoFilesInTestDirectoryButSeveralFilesInSourceDirectory/";
        final Path startingPath = DirectoryBuilder.aDirectory(rootDir)
                .with(DirectoryBuilder.aSubDirectory("src/test/java/")
                        .withNoFiles())
                .with(DirectoryBuilder.aSubDirectory("src/main/java/")
                        .with(FileBuilder.aFile("sourceFile1.java"))
                        .with(FileBuilder.aFile("sourceFile2.java")))
                .build();

        final Path customTestRootPath = Paths.get(rootDir+ "src/test/java/");
        final TestClassesCollector testClassesCollector = TestClassesCollectorFactory.newInstanceWithCustomTestPath(customTestRootPath);
        final List<JavaSourceFile> expectedTestClasses = new ArrayList<>();
        return new TestClassesCollectorTestExample(testClassesCollector, startingPath, expectedTestClasses);
    }

    public static TestClassesCollectorTestExample directoryTreeSeveralTestFilesInSeveralSubDirectories() throws Exception {
        final String rootDir = "target/TestClassesCollectorTestExamples/directoryTreeSeveralTestFilesInSeveralSubDirectories/";
        final Path startingPath = DirectoryBuilder.aDirectory(rootDir)
                .with(DirectoryBuilder.aDirectory("src/test/java/")
                        .with(FileBuilder.aFile("testFile.java"))
                        .with(DirectoryBuilder.aSubDirectory("subdir")
                                .with(FileBuilder.aFile("subdir_testFile.java"))
                                .with(DirectoryBuilder.aSubDirectory("subsubdir")
                                        .with(FileBuilder.aFile("subsubdir_testFile.java")))))
                .build();

        final Path customTestRootPath = Paths.get(rootDir+"src/test/java/");
        final TestClassesCollector testClassesCollector = TestClassesCollectorFactory.newInstanceWithCustomTestPath(customTestRootPath);
        final List<JavaSourceFile> expectedTestClasses = Arrays.asList(
                aJavaFile(startingPath, "src/test/java/testFile.java"),
                aJavaFile(startingPath, "src/test/java/subdir/subdir_testFile.java"),
                aJavaFile(startingPath, "src/test/java/subdir/subsubdir/subsubdir_testFile.java")
        );
        return new TestClassesCollectorTestExample(testClassesCollector, startingPath, expectedTestClasses);
    }

    public static TestClassesCollectorTestExample directoryWithTestAndNoneJavaFiles() throws Exception {
        final String rootDir = "target/TestClassesCollectorTestExamples/directoryWithTestAndNoneJavaFiles/";
        final Path startingPath = DirectoryBuilder.aDirectory(rootDir)
                .with(DirectoryBuilder.aSubDirectory("src/test/java/")
                        .with(FileBuilder.aFile("testFile.java"))
                        .with(FileBuilder.aFile("pomFile.pom"))
                        .with(FileBuilder.aFile("textFile.java.txt")))
                .build();

        final Path customTestRootPath = Paths.get(rootDir+"src/test/java/");
        final TestClassesCollector testClassesCollector = TestClassesCollectorFactory.newInstanceWithCustomTestPath(customTestRootPath);
        final List<JavaSourceFile> expectedTestClasses = Arrays.asList(
                aJavaFile(startingPath, "src/test/java/testFile.java")
        );
        return new TestClassesCollectorTestExample(testClassesCollector, startingPath, expectedTestClasses);
    }

    private static JavaSourceFile aJavaFile(Path startingPath, String relativePathString) {
        final Path absoluteFilePaths = startingPath.toAbsolutePath()
                .resolve(relativePathString);
        return JavaSoureFileFactory.createNew(absoluteFilePaths);
    }


    static class TestClassesCollectorTestExample {
        private final TestClassesCollector testClassesCollector;
        private final Path startingPath;
        private final List<JavaSourceFile> expectedTestClasses;

        TestClassesCollectorTestExample(TestClassesCollector testClassesCollector, Path startingPath, List<JavaSourceFile> expectedTestClasses) {
            this.testClassesCollector = testClassesCollector;
            this.startingPath = startingPath;
            this.expectedTestClasses = expectedTestClasses;
        }

        public TestClassesCollector getTestClassesCollector() {
            return testClassesCollector;
        }

        public Path getStartingPath() {
            return startingPath;
        }

        public List<JavaSourceFile> getExpectedTestClasses() {
            return expectedTestClasses;
        }
    }
}
