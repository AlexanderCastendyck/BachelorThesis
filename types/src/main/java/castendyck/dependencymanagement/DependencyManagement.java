package castendyck.dependencymanagement;

import castendyck.artifactidentifier.ArtifactIdentifier;
import de.castendyck.enforcing.NotNullConstraintEnforcer;

import java.util.List;
import java.util.stream.Collectors;

public class DependencyManagement {
    private final List<ArtifactIdentifier> managedDependencies;

    DependencyManagement(List<ArtifactIdentifier> managedDependencies){
        NotNullConstraintEnforcer.ensureNotNull(managedDependencies);
        this.managedDependencies = managedDependencies;
    }

    public boolean containsVersionFor(String groupId, String artifactId){
        final List<ArtifactIdentifier> matchedDependencies = findMatchingDependencies(groupId, artifactId);
        return matchedDependencies.size() >= 1;
    }

    public String getVersionOf(String groupId, String artifactId){
        final List<ArtifactIdentifier> matchedDependencies = findMatchingDependencies(groupId, artifactId);
        final ArtifactIdentifier matchingDependency = matchedDependencies.get(0);
        final String version = matchingDependency.getVersion();
        return version;
    }

    private List<ArtifactIdentifier> findMatchingDependencies(String groupId, String artifactId) {
        return managedDependencies.stream()
                .filter(a -> a.getGroupId().equals(groupId) && a.getArtifactId().equals(artifactId))
                .collect(Collectors.toList());
    }
}
