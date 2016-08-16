package castendyck.mavendependency;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.mavendependency.internal.MavenDependencyImpl;
import castendyck.scope.MavenScope;
import castendyck.scope.MavenScopeFactory;

public class MavenDependencyBuilder {
    private ArtifactIdentifier artifactIdentifier;
    private MavenScope scope;

    private MavenDependencyBuilder() {
    }

    public static MavenDependencyBuilder aMavenDependency() {
        return new MavenDependencyBuilder();
    }

    public MavenDependencyBuilder forArtifact(ArtifactIdentifier artifactIdentifier) {
        this.artifactIdentifier = artifactIdentifier;
        return this;
    }

    public MavenDependencyBuilder withScope(String scope) {
        if (scope == null) {
            this.scope = MavenScopeFactory.createNew(MavenScope.NONE);
        } else {
            this.scope = MavenScopeFactory.createNew(scope);
        }
        return this;
    }

    public MavenDependency build() {
        return new MavenDependencyImpl(artifactIdentifier, scope);
    }

}
