package castendyck.dependencymanagement;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.mavendependency.MavenDependencyBuilder;

import java.util.ArrayList;
import java.util.List;

public class DependencyManagementBuilder {
    private final List<ArtifactIdentifier> managedDependencies = new ArrayList<>();

    public static DependencyManagementBuilder aDependencyManagement(){
        return new DependencyManagementBuilder();
    }

    public DependencyManagementBuilder containing(ArtifactIdentifier artifactIdentifier){
        managedDependencies.add(artifactIdentifier);
        return this;
    }
    public DependencyManagement build(){
        return new DependencyManagement(managedDependencies);
    }

    public DependencyManagementBuilder withNoManagedDependencies() {
        managedDependencies.clear();
        return this;
    }
}
