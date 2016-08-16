package castendyck.sourcecode.sourecodefilecollecting.internal;

import castendyck.javasourcefile.JavaSourceFile;
import castendyck.javasourcefile.JavaSoureFileFactory;
import castendyck.sourcecode.sourecodefilecollecting.TestClassesCollector;
import castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.directorytraversedeciding.internal.AllDirectoriesUnderGivenDirectoryTraverseDecisionImpl;
import castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.filematchingdeciding.internal.FileEndingMatchingDecisionImpl;
import castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.internal.FileCollectorImpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.directorytraversedeciding.internal.AllDirectoriesUnderGivenDirectoryTraverseDecisionImpl.ATraverserForDirectoriesUnder;
import static castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.filematchingdeciding.internal.FileEndingMatchingDecisionImpl.MatcherForFilesEndingWith;

public class TestClassesCollectorImpl implements TestClassesCollector {
    private final Path testPath;

    public TestClassesCollectorImpl() {
        this.testPath = Paths.get("src/test/java/");
    }
    public TestClassesCollectorImpl(Path testPath) {
        this.testPath = testPath;
    }

    @Override
    public List<JavaSourceFile> collectFilesFromMavenTestRepository(Path startingPath) {
        final FileCollectorImpl fileCollector = new FileCollectorImpl();
        final AllDirectoriesUnderGivenDirectoryTraverseDecisionImpl traverseDecision = ATraverserForDirectoriesUnder(testPath);
        final FileEndingMatchingDecisionImpl fileMatchingDecision = MatcherForFilesEndingWith(".java");
        final List<Path> collectedFiles = fileCollector.collectFilesFrom(startingPath, traverseDecision, fileMatchingDecision);

        final List<JavaSourceFile> javaSourceFiles = collectedFiles.stream()
                .map(JavaSoureFileFactory::createNew)
                .collect(Collectors.toList());

        return javaSourceFiles;
    }
}
