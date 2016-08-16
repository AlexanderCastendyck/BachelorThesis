package castendyck.dependency;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.dependency.internal.DependencyImpl;

public class DependencyFactory {
    public static Dependency dependencyFor(ArtifactIdentifier artifactIdentifier){
        return DependencyImpl.forArtifact(artifactIdentifier);
    }
    public static Dependency fromAetherDependency(org.eclipse.aether.graph.Dependency aetherDependency){
        return DependencyImpl.forAetherDependency(aetherDependency);
    }
}
