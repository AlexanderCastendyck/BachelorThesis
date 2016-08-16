package de.castendyck.file;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FileUtils {
    private static final Logger logger = Logger.getLogger(FileUtils.class);

    public static void createThisAndParentDirectories(Path path) throws IOException {
        final Path parent = path.getParent();
        if (!Files.exists(parent)) {
            createThisAndParentDirectories(parent);
        }
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

    public static Path createOrOverrideFile(Path filePath) throws IOException {
        final Path parent = filePath.getParent();
        createThisAndParentDirectories(parent);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
        Files.createFile(filePath);
        return filePath;
    }

    public static void deleteThisPathAndChildren(Path path) throws IOException {
        final List<Path> subDirectories = Files.list(path)
                .filter(Files::isDirectory)
                .collect(toList());
        for (Path subDirectory : subDirectories) {
            deleteThisPathAndChildren(subDirectory);
        }
        final List<Path> allDirsAndFilesOfPath = Files.list(path)
                .collect(toList());

        for (Path pathEntry : allDirsAndFilesOfPath) {
            Files.deleteIfExists(pathEntry);
        }
        Files.deleteIfExists(path);
    }

    public static List<Path> listDirectory(Path path) throws IOException {
        if(Files.exists(path)) {
            final List<Path> directoryContent = Files.list(path)
                    .collect(toList());
            return directoryContent;
        }else{
            logger.warn("Listing files of not existing directory: "+path);
            return new ArrayList<>();
        }

    }
}
