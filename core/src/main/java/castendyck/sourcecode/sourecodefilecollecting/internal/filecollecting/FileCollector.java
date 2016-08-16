package castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting;

import castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.directorytraversedeciding.DirectoryTraverseDecision;
import castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.filematchingdeciding.FileMatchingDecision;

import java.nio.file.Path;
import java.util.List;

public interface FileCollector {
    List<Path> collectFilesFrom(Path path, DirectoryTraverseDecision traverseDecision, FileMatchingDecision fileMatchingDecision);
}
