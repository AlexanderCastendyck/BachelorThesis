package castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.directorytraversedeciding;

import java.nio.file.Path;

public interface DirectoryTraverseDecision {
    boolean shouldBeTraversed(Path directory);
}
