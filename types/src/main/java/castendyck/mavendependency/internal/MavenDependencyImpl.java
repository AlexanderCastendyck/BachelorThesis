package castendyck.mavendependency.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.mavendependency.MavenDependency;
import castendyck.scope.MavenScope;

import static de.castendyck.enforcing.NotNullConstraintEnforcer.ensureNotNull;

public class MavenDependencyImpl implements MavenDependency {
    private final ArtifactIdentifier artifactIdentifier;
    private final MavenScope scope;

    public MavenDependencyImpl(ArtifactIdentifier artifactIdentifier, MavenScope scope) {
        ensureNotNull(artifactIdentifier);
        this.artifactIdentifier = artifactIdentifier;
        ensureNotNull(scope);
        this.scope = scope;
    }

    @Override public ArtifactIdentifier getArtifactIdentifier() {
        return this.artifactIdentifier;
    }

    @Override
    public MavenScope getScope() {
        return scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MavenDependencyImpl that = (MavenDependencyImpl) o;

        if (!artifactIdentifier.equals(that.artifactIdentifier)) return false;
        return scope.equals(that.scope);

    }

    @Override
    public int hashCode() {
        int result = artifactIdentifier.hashCode();
        result = 31 * result + scope.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MavenDependencyImpl{" +
                "artifactIdentifier=" + artifactIdentifier +
                ", scope=" + scope +
                '}';
    }
}
