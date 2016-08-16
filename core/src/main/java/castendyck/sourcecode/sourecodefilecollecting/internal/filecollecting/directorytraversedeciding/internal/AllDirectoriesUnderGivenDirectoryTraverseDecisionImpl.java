package castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.directorytraversedeciding.internal;

import castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.directorytraversedeciding.DirectoryTraverseDecision;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AllDirectoriesUnderGivenDirectoryTraverseDecisionImpl implements DirectoryTraverseDecision {
    private final Path rootDirectory;

    private AllDirectoriesUnderGivenDirectoryTraverseDecisionImpl(Path rootDirectory) {
        final String currentPathsString = System.getProperty("user.dir");
        final Path currentPath = Paths.get(currentPathsString);
        final Path absolutePath = currentPath.resolve(rootDirectory).toAbsolutePath();
        this.rootDirectory = absolutePath;
    }

    public static AllDirectoriesUnderGivenDirectoryTraverseDecisionImpl ATraverserForDirectoriesUnder(String directory){
        final Path rootDirectory = Paths.get(directory);
        return new AllDirectoriesUnderGivenDirectoryTraverseDecisionImpl(rootDirectory);
    }
    public static AllDirectoriesUnderGivenDirectoryTraverseDecisionImpl ATraverserForDirectoriesUnder(Path rootDirectory){
        return new AllDirectoriesUnderGivenDirectoryTraverseDecisionImpl(rootDirectory);
    }

    @Override
    public boolean shouldBeTraversed(Path directory) {
        final Path absolutePathToMatch = directory.toAbsolutePath();
        final boolean isParentOf = rootDirectory.startsWith(absolutePathToMatch);
        final boolean isSame = rootDirectory.equals(absolutePathToMatch);
        final boolean isSubPathsOfRootDirectory = absolutePathToMatch.startsWith(rootDirectory);
        return isParentOf || isSame || isSubPathsOfRootDirectory;
    }
}
