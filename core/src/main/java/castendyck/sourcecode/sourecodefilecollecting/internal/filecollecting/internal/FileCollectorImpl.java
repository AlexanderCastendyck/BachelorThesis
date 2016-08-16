package castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.internal;

import castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.FileCollector;
import castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.directorytraversedeciding.DirectoryTraverseDecision;
import castendyck.sourcecode.sourecodefilecollecting.internal.filecollecting.filematchingdeciding.FileMatchingDecision;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileCollectorImpl implements FileCollector {
    private static Logger logger = Logger.getLogger(FileCollectorImpl.class);

    @Override
    public List<Path> collectFilesFrom(Path path, DirectoryTraverseDecision traverseDecision, FileMatchingDecision fileMatchingDecision) {
        final List<Path> filesInDirectory = getFilesIn(path);
        final List<Path> collectedFiles = filesInDirectory.stream()
                .filter(fileMatchingDecision::shouldBeCollected)
                .collect(Collectors.toList());

        final List<Path> subDirectories = getSubDirectoriesOf(path);
        for (Path subDirectory : subDirectories) {
            if (traverseDecision.shouldBeTraversed(subDirectory)) {
                final List<Path> filesFromSubDirectory = collectFilesFrom(subDirectory, traverseDecision, fileMatchingDecision);
                collectedFiles.addAll(filesFromSubDirectory);
            }
        }
        return collectedFiles;
    }

    private List<Path> getFilesIn(Path path) {
        try {
            return Files.list(path)
                    .filter(file -> !Files.isDirectory(file))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.debug("IOException was thrown at " + FileCollectorImpl.class + ": " + e.getMessage());
            logger.error("Failed to load Files of " + path + ". Ignoring them when collecting sourcecode files");
            return new ArrayList<>();
        }
    }

    private List<Path> getSubDirectoriesOf(Path path) {
        try {
            final List<Path> subDirectories = Files.list(path)
                    .filter(file -> Files.isDirectory(file))
                    .collect(Collectors.toList());
            return subDirectories;
        } catch (IOException e) {
            logger.debug("IOException was thrown at " + FileCollectorImpl.class + ": " + e.getMessage());
            logger.error("Failed to load Subdirectories of " + path + ". Ignoring them when collecting sourcecode files");
            return new ArrayList<>();
        }
    }
}
