package castendyck.maven.pomfile;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.mavendependency.MavenDependency;
import castendyck.temporarypomfile.TemporaryPomFileBuilder;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static castendyck.temporarypomfile.DependencyManagementTestBuilder.aDependencyManagement;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class PomFileGetDependenciesTestIT {

    @Test
    public void getDependencies_returnsEmptyDependenciesWhenNoDependencyTag() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withNoDependencies()
                .inDirectory("target/pomFileTests/getDependencies_returnsEmptyDependenciesWhenNoDependencyTag/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final List<MavenDependency> dependencies = pomFile.getDependencies();
        assertThat(dependencies, hasSize(0));
    }

    @Test
    public void getDependencies_WithOneDependency() throws Exception {
        final ArtifactIdentifier dependencyArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "dependency", "1.0");
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withDependency(dependencyArtifact)
                .inDirectory("target/pomFileTests/getDependencies_WithOneDependency/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final List<MavenDependency> dependencies = pomFile.getDependencies();
        assertThat(dependencies, hasSize(1));
        final MavenDependency dependency = dependencies.get(0);
        assertThat(dependency.getArtifactIdentifier(), equalTo(dependencyArtifact));
    }

    @Test
    public void getDependencies_WithThreeDependency() throws Exception {
        final ArtifactIdentifier dependencyArtifact1 = ArtifactIdentifierBuilder.buildShort("someGroup", "dependency_A", "1.0");
        final ArtifactIdentifier dependencyArtifact2 = ArtifactIdentifierBuilder.buildShort("someGroup", "dependency_B", "1.0");
        final ArtifactIdentifier dependencyArtifact3 = ArtifactIdentifierBuilder.buildShort("someGroup", "dependency_C", "1.0");
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withMavenDependencies(
                        dependencyArtifact1,
                        dependencyArtifact2,
                        dependencyArtifact3)
                .inDirectory("target/pomFileTests/getDependencies_WithThreeDependency/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final List<MavenDependency> dependencies = pomFile.getDependencies();
        assertThat(dependencies, hasSize(3));
        final MavenDependency dependency1 = dependencies.get(0);
        assertThat(dependency1.getArtifactIdentifier(), equalTo(dependencyArtifact1));
        final MavenDependency dependency2 = dependencies.get(1);
        assertThat(dependency2.getArtifactIdentifier(), equalTo(dependencyArtifact2));
        final MavenDependency dependency3 = dependencies.get(2);
        assertThat(dependency3.getArtifactIdentifier(), equalTo(dependencyArtifact3));
    }

    @Test
    public void getDependencies_resolvesVersionAgainstDependencyManagement() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withDependency("someGroup", "dependency", "")
                .with(aDependencyManagement()
                        .withAManagedDependency("someGroup", "dependency", "2.3"))
                .inDirectory("target/pomFileTests/getDependencies_resolvesVersionAgainstDependencyManagement/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final List<MavenDependency> dependencies = pomFile.getDependencies();
        assertThat(dependencies, hasSize(1));
        final MavenDependency dependency = dependencies.get(0);
        final ArtifactIdentifier expectedArtifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "dependency", "2.3");
        assertThat(dependency.getArtifactIdentifier(), equalTo(expectedArtifactIdentifier));
    }

    @Test
    public void getDependencies_resolvesPropertyInGroupId() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withAProperty("realGroup", "groupViaProperty")
                .withDependency("${realGroup}", "someArtifact", "1.0")
                .inDirectory("target/pomFileTests/getDependencies_resolvesPropertyInGroupId/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final List<MavenDependency> dependencies = pomFile.getDependencies();
        assertThat(dependencies, hasSize(1));
        final MavenDependency dependency = dependencies.get(0);
        final ArtifactIdentifier expectedArtifactIdentifier = ArtifactIdentifierBuilder.buildShort("groupViaProperty", "someArtifact", "1.0");
        assertThat(dependency.getArtifactIdentifier(), equalTo(expectedArtifactIdentifier));
    }

    @Test
    public void getDependencies_resolvesPropertyInArtifactId() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withAProperty("realArtifact", "artifactViaProperty")
                .withDependency("someGroup", "${realArtifact}", "1.0")
                .inDirectory("target/pomFileTests/getDependencies_resolvesPropertyInArtifactId/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final List<MavenDependency> dependencies = pomFile.getDependencies();
        assertThat(dependencies, hasSize(1));
        final MavenDependency dependency = dependencies.get(0);
        final ArtifactIdentifier expectedArtifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "artifactViaProperty", "1.0");
        assertThat(dependency.getArtifactIdentifier(), equalTo(expectedArtifactIdentifier));
    }

    @Test
    public void getDependencies_resolvesPropertyInVersion() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withAProperty("realVersion", "4.4.4")
                .withDependency("someGroup", "dependency", "${realVersion}")
                .inDirectory("target/pomFileTests/getDependencies_resolvesPropertyInArtifactId/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final List<MavenDependency> dependencies = pomFile.getDependencies();
        assertThat(dependencies, hasSize(1));
        final MavenDependency dependency = dependencies.get(0);
        final ArtifactIdentifier expectedArtifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "dependency", "4.4.4");
        assertThat(dependency.getArtifactIdentifier(), equalTo(expectedArtifactIdentifier));
    }

    @Test
    public void getDependencies_versionGetsResolvedAgainstDependencyManagementAndProperties() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "111")
                .with(aDependencyManagement()
                        .withAManagedDependency("someGroup", "dependency", "${project.version}"))
                .withDependency("someGroup", "dependency", "")
                .inDirectory("target/pomFileTests/getDependencies_versionGetsResolvedAgainstDependencyManagementAndProperties")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final List<MavenDependency> dependencies = pomFile.getDependencies();
        assertThat(dependencies, hasSize(1));
        final MavenDependency dependency = dependencies.get(0);
        final ArtifactIdentifier expectedArtifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "dependency", "111");
        assertThat(dependency.getArtifactIdentifier(), equalTo(expectedArtifactIdentifier));
    }

    @Test
    public void getDependencies_versionGetsResolvedAgainstDependencyManagementInParentAndDefaultProperties() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "333");
        TemporaryPomFileBuilder.aPomFile()
                .forArtifact(parentArtifact)
                .with(aDependencyManagement()
                        .withAManagedDependency("someGroup", "dependency", "${project.version}"))
                .inDirectory("target/pomFileTests/getDependencies_versionGetsResolvedAgainstDependencyManagementInParentAndDefaultProperties/")
                .build();
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withDependency("someGroup", "dependency", "")
                .withParent(parentArtifact)
                .inDirectory("target/pomFileTests/getDependencies_versionGetsResolvedAgainstDependencyManagementInParentAndDefaultProperties/submodule")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final List<MavenDependency> dependencies = pomFile.getDependencies();
        assertThat(dependencies, hasSize(1));
        final MavenDependency dependency = dependencies.get(0);
        final ArtifactIdentifier expectedArtifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "dependency", "333");
        assertThat(dependency.getArtifactIdentifier(), equalTo(expectedArtifactIdentifier));
    }

    @Test
    public void getDependencies_versionGetsResolvedAgainstDependencyManagementInParentAndProperties() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "333");
        TemporaryPomFileBuilder.aPomFile()
                .forArtifact(parentArtifact)
                .withPackaging("pom")
                .with(aDependencyManagement()
                        .withAManagedDependency("javax.inject", "javax.inject", "${javax.inject.version}"))
                .withAProperty("javax.inject.version", "1")
                .inDirectory("target/pomFileTests/getDependencies_versionGetsResolvedAgainstDependencyManagementInParentAndProperties/")
                .build();
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withDependency("javax.inject", "javax.inject", "")
                .withParent(parentArtifact)
                .inDirectory("target/pomFileTests/getDependencies_versionGetsResolvedAgainstDependencyManagementInParentAndProperties/submodule")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final List<MavenDependency> dependencies = pomFile.getDependencies();
        assertThat(dependencies, hasSize(1));
        final MavenDependency dependency = dependencies.get(0);
        final ArtifactIdentifier expectedArtifactIdentifier = ArtifactIdentifierBuilder.buildShort("javax.inject", "javax.inject", "1");
        assertThat(dependency.getArtifactIdentifier(), equalTo(expectedArtifactIdentifier));
    }

    @Test
    public void getDependencies_groupIdGetsResolvedAgainstParentGroupId() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "123");
        TemporaryPomFileBuilder.aPomFile()
                .forArtifact(parentArtifact)
                .inDirectory("target/pomFileTests/getDependencies_groupIdGetsResolvedAgainstParentGroupId/")
                .build();
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("", "someArtifact", "")
                .withDependency("${project.groupId}", "arbitraryArtifact", "1.0")
                .withParent(parentArtifact)
                .inDirectory("target/pomFileTests/getDependencies_groupIdGetsResolvedAgainstParentGroupId/submodule")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final List<MavenDependency> dependencies = pomFile.getDependencies();
        assertThat(dependencies, hasSize(1));
        final MavenDependency dependency = dependencies.get(0);
        final ArtifactIdentifier expectedArtifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "arbitraryArtifact", "1.0");
        assertThat(dependency.getArtifactIdentifier(), equalTo(expectedArtifactIdentifier));
    }

    @Test
    public void getDependencies_versionGetsResolvedAgainstParentGroupId() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "123");
        TemporaryPomFileBuilder.aPomFile()
                .forArtifact(parentArtifact)
                .inDirectory("target/pomFileTests/getDependencies_versionGetsResolvedAgainstParentGroupId/")
                .build();
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("", "someArtifact", "")
                .withDependency("someGroup", "arbitraryArtifact", "${project.version}")
                .withParent(parentArtifact)
                .inDirectory("target/pomFileTests/getDependencies_versionGetsResolvedAgainstParentGroupId/submodule")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final List<MavenDependency> dependencies = pomFile.getDependencies();
        assertThat(dependencies, hasSize(1));
        final MavenDependency dependency = dependencies.get(0);
        final ArtifactIdentifier expectedArtifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "arbitraryArtifact", "123");
        assertThat(dependency.getArtifactIdentifier(), equalTo(expectedArtifactIdentifier));
    }
}
