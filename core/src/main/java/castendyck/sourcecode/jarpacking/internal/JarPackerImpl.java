package castendyck.sourcecode.jarpacking.internal;

import de.castendyck.file.FileUtils;
import castendyck.sourcecode.jarpacking.JarPacker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JarPackerImpl implements JarPacker {
    private Path startDirectory;

    @Override
    public Path packDirectory(Path outputDirectory, String fileName, Path targetDirectory) throws IOException {
        startDirectory = outputDirectory;
        final JarFileBuilder jarFileBuilder = JarFileBuilder.aJarFile(fileName, targetDirectory);
        collectClassFilesOfDirectory(outputDirectory, jarFileBuilder);
        return jarFileBuilder.build();
    }

    private void collectClassFilesOfDirectory(Path directory, JarFileBuilder jarFileBuilder) throws IOException {
        final List<Path> paths = FileUtils.listDirectory(directory);
        for (Path path : paths) {
            if(Files.isDirectory(path)){
                collectClassFilesOfDirectory(path, jarFileBuilder);
            }
            if(isClassFile(path)){
                final String fullyQualifiedName = determineFullyQualifiedName(path);
                jarFileBuilder.containingAClassFile(path.toFile(), fullyQualifiedName);
            }
        }
    }

    private String determineFullyQualifiedName(Path path) {
        final Path relativeDifference = startDirectory.relativize(path);
        final String relativeDifferenceString = relativeDifference.toString();
        return relativeDifferenceString;
    }

    private boolean isClassFile(Path path) {
        return path.toString().endsWith(".class");
    }
}
