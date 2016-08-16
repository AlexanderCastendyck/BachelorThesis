package castendyck.pomfilelocator.internal;

import castendyck.pomfilelocator.PomFileLocationException;
import castendyck.pomfilelocator.PomFileLocator;
import de.castendyck.collections.ExpectOneUniqueElementCollector;
import de.castendyck.collections.OneUniqueElementExpectedException;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

public class PomFileLocatorImpl implements PomFileLocator {

    @Override
    public Path locateInDirectory(Path directory) throws PomFileLocationException {
        final File[] files = listFiles(directory);
        final File pomFile = findPomFile(files);
        final Path path = pomFile.toPath();
        return path;

    }

    private File findPomFile(File[] files) throws PomFileLocationException {
        try {
            final File pomFile = Arrays.stream(files)
                    .filter(File::isFile)
                    .filter(f -> f.getName().equals("pom.xml"))
                    .collect(ExpectOneUniqueElementCollector.expectOneUniqueElement());
            return pomFile;

        } catch (OneUniqueElementExpectedException e) {
            throw new PomFileLocationException(e);
        }
    }

    private File[] listFiles(Path directory) throws PomFileLocationException {
        final File[] files = directory.toFile().listFiles();
        if (files == null) {
            throw new PomFileLocationException();
        }
        return files;
    }
}
