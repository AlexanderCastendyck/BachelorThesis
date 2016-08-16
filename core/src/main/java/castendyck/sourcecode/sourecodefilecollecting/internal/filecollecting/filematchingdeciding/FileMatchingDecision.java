package castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.filematchingdeciding;

import java.nio.file.Path;

public interface FileMatchingDecision {
    boolean shouldBeCollected(Path file);
}
