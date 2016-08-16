package castendyck.sourcecode.localrepositorywithsourcecode.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.repository.LocalRepository;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LocalRepositoryWithSourceCodeBuilder {
    private final Map<ArtifactIdentifier, Path> sourceCodeJars = new HashMap<>();
    private LocalRepository localRepository;

    public static LocalRepositoryWithSourceCodeBuilder aLocalRepositoryWithSourceCode(){
        return new LocalRepositoryWithSourceCodeBuilder();
    }

    public LocalRepositoryWithSourceCodeBuilder withASourceCodeJar(ArtifactIdentifier artifactIdentifier, Path pathToJar){
        sourceCodeJars.put(artifactIdentifier, pathToJar);
        return this;
    }
    public LocalRepositoryWithSourceCodeBuilder withLocalRepository(LocalRepository localRepository){
        this.localRepository = localRepository;
        return this;
    }

    public LocalRepositoryWithSourceCode build(){
        return new LocalRepositoryWithSourceCode(localRepository, sourceCodeJars);
    }
}
