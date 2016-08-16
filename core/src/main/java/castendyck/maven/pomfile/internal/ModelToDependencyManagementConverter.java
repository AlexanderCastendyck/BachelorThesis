package castendyck.maven.pomfile.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.dependencymanagement.DependencyManagement;
import castendyck.dependencymanagement.DependencyManagementBuilder;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

import java.util.List;
import java.util.function.Predicate;

import static castendyck.dependencymanagement.DependencyManagementBuilder.aDependencyManagement;

public class ModelToDependencyManagementConverter {
    public static DependencyManagement convertToDependencyManagement(Model model) {
        final org.apache.maven.model.DependencyManagement mavenDependencyManagement = model.getDependencyManagement();
        if (mavenDependencyManagement == null) {
            final DependencyManagement dependencyManagement = aDependencyManagement()
                    .withNoManagedDependencies()
                    .build();
            return dependencyManagement;

        } else {
            final DependencyManagementBuilder dependencyManagementBuilder = aDependencyManagement();
            final List<Dependency> managedDependencies = mavenDependencyManagement.getDependencies();
            managedDependencies.stream()
                    .filter(isVersionSet())
                    .map(ModelToDependencyManagementConverter::convertToArtifact)
                    .forEach(dependencyManagementBuilder::containing);

            final DependencyManagement dependencyManagement = dependencyManagementBuilder.build();
            return dependencyManagement;

        }
    }

    private static Predicate<Dependency> isVersionSet() {
        return d -> d.getVersion() != null && !d.getVersion().isEmpty();
    }

    private static ArtifactIdentifier convertToArtifact(Dependency dependency){
        final String groupId = dependency.getGroupId();
        final String artifactId = dependency.getArtifactId();
        final String version = dependency.getVersion();
        final ArtifactIdentifier artifactIdentifier = new ArtifactIdentifierBuilder().withGroupId(groupId)
                .withArtifactId(artifactId)
                .withVersion(version)
                .build();
        return artifactIdentifier;
    }

}
