package castendyck.callgraph.sourcecoderegistering;

import castendyck.artifactidentifier.ArtifactIdentifier;

import java.util.Set;
import java.util.jar.JarFile;

public interface SourceCodeRegistry {
    JarFile getJarFile(ArtifactIdentifier artifactIdentifier) throws NoJarForThisArtifactIdentifierFoundException;
    Set<ArtifactIdentifier> getAllStoredArtifactIdentifier();
}
