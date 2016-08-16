package castendyck.dependencygraphing.dependencyregistry.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.dependencygraphing.dependencyregistry.NotRegisteredArtifactException;
import de.castendyck.enforcing.NotNullConstraintEnforcer;

import java.util.List;
import java.util.Map;

public class DependencyRegistryImpl implements DependencyRegistry {
    private final Map<ArtifactIdentifier, List<ArtifactIdentifier>> artifactToDependendingArtifactsMap;

    public DependencyRegistryImpl(Map<ArtifactIdentifier, List<ArtifactIdentifier>> artifactToDependendingArtifactsMap) {
        NotNullConstraintEnforcer.ensureNotNull(artifactToDependendingArtifactsMap);
        this.artifactToDependendingArtifactsMap = artifactToDependendingArtifactsMap;
    }


    @Override
    public List<ArtifactIdentifier> getArtifactsThatDependOn(ArtifactIdentifier artifactIdentifier) throws NotRegisteredArtifactException {
        if(artifactIsNotStored(artifactIdentifier)){
            final String message = "Dependent Artifacts for " + artifactIdentifier + " could not be retrieved, because artifact was not stored";
            throw new NotRegisteredArtifactException(message);
        }

        final List<ArtifactIdentifier> artifactIdentifiers = artifactToDependendingArtifactsMap.get(artifactIdentifier);
        return artifactIdentifiers;
    }

    private boolean artifactIsNotStored(ArtifactIdentifier artifactIdentifier) {
        return !artifactToDependendingArtifactsMap.containsKey(artifactIdentifier);
    }
}
