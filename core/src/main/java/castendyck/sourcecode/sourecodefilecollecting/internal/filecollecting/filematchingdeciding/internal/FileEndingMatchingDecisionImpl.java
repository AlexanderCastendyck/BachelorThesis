package castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.filematchingdeciding.internal;

import castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.filematchingdeciding.FileMatchingDecision;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileEndingMatchingDecisionImpl implements FileMatchingDecision {
    private final String fileEnding;

    private FileEndingMatchingDecisionImpl(String fileEnding) {
        this.fileEnding = fileEnding;
    }

    public static FileEndingMatchingDecisionImpl MatcherForFilesEndingWith(String fileEnding){
        return new FileEndingMatchingDecisionImpl(fileEnding);
    }

    @Override
    public boolean shouldBeCollected(Path file) {
        if(!Files.isRegularFile(file)){
            return false;
        }

        final String fileName = file.getFileName().toString();
        final boolean shouldBeCollected = fileName.endsWith(fileEnding);
        return shouldBeCollected;
    }
}
