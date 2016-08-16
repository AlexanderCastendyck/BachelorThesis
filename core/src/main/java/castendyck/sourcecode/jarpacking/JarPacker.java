package castendyck.sourcecode.jarpacking;

import java.io.IOException;
import java.nio.file.Path;

public interface JarPacker {
    Path packDirectory(Path outputDirectory, String fileName, Path targetDirectory) throws IOException;
}
