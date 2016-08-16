package castendyck.sourcecode.localrepositorywithsourcecode;

import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileCreationException;
import castendyck.pomfilelocator.PomFileLocationException;
import castendyck.repository.LocalRepository;

import java.io.IOException;

public interface LocalRepositoryWithSourceCodeCreator {

    LocalRepository extendLocalRepositoryWithSourceCode(LocalRepository localRepository, PomFile parentPom) throws PomFileLocationException, PomFileCreationException, IOException;
}
