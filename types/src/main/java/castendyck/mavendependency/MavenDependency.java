package castendyck.mavendependency;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.scope.MavenScope;

public interface MavenDependency {

    ArtifactIdentifier getArtifactIdentifier();

    MavenScope getScope();
}
