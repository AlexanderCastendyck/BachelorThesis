package castendyck.dependencygraphing.artifactsindgraphcollecting;

import castendyck.artifactidentifier.ArtifactIdentifier;

public interface ArtifactMatcher {

    boolean matches(ArtifactIdentifier artifactIdentifier);
}
