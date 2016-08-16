package castendyck.maven.pomfile;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.dependencymanagement.DependencyManagement;
import castendyck.mavendependency.MavenDependency;
import castendyck.mavenproperty.MavenProperties;
import castendyck.pomfilelocator.PomFileLocationException;

import java.nio.file.Path;
import java.util.List;

public interface PomFile {

    ArtifactIdentifier getArtifactIdentifier();

    List<MavenDependency> getDependencies();

    boolean isMultiModuleProject() throws PomFileLocationException, PomFileCreationException;

    List<PomFile> getModules() throws PomFileLocationException, PomFileCreationException;

    MavenProperties getProperties();

    boolean hasParent();

    PomFile getParent();

    DependencyManagement getDependencyManagement();

    String getPackaging();

    Path getOutputDirectory();

    Path getBuildDirectory();
}
