package castendyck.pomfilelocator;

import java.nio.file.Path;

public interface PomFileLocator {

    Path locateInDirectory(Path directory) throws PomFileLocationException;
}
