package castendyck.callgraph.sourcecoderegistering.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.sourcecoderegistering.NoJarForThisArtifactIdentifierFoundException;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import de.castendyck.enforcing.NotNullConstraintEnforcer;

import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

public class SourceCodeRegistryImpl implements SourceCodeRegistry {
    private final Map<ArtifactIdentifier, JarFile> storedJars;

    public SourceCodeRegistryImpl(Map<ArtifactIdentifier, JarFile> storedJars) {
        NotNullConstraintEnforcer.ensureNotNull(storedJars);
        this.storedJars = storedJars;
    }

    @Override
    public JarFile getJarFile(ArtifactIdentifier artifactIdentifier) throws NoJarForThisArtifactIdentifierFoundException {
        if (storedJars.containsKey(artifactIdentifier)) {
            return storedJars.get(artifactIdentifier);
        } else {
            throw new NoJarForThisArtifactIdentifierFoundException("No Jar was registered for " + artifactIdentifier);
        }
    }

    @Override
    public Set<ArtifactIdentifier> getAllStoredArtifactIdentifier() {
        final Set<ArtifactIdentifier> artifactIdentifiers = storedJars.keySet();
        return artifactIdentifiers;
    }

}
