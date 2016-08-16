package castendyck.dependencygraphing;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileFactory;
import castendyck.mavendependency.MavenDependency;
import castendyck.mavendependency.MavenDependencyBuilder;
import castendyck.pomfilelocator.PomFileLocator;
import castendyck.pomfilelocator.PomFileLocatorFactory;
import castendyck.repository.TestRepositories;
import castendyck.repository.TestRepositorySession;
import castendyck.repository.TestRepositorySystem;
import castendyck.temporarypomfile.TemporaryPomFileBuilder;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DependencyStateBuilder {
    private final static Path ROOT_DIRECTORY_FOR_POM_FILES = Paths.get("target", "dependencyGraphProviderTestIT");
    private final File pomFile;
    private final PomFileLocator pomFileLocator;

    public DependencyStateBuilder(File file) {
        this.pomFile = file;
        this.pomFileLocator = PomFileLocatorFactory.createDefaultOne();
    }

    public DependencyStateBuilder(File pomFile, PomFileLocator pomFileLocator) {
        this.pomFile = pomFile;
        this.pomFileLocator = pomFileLocator;
    }

    public static DependencyStateBuilder aPomFileWithoutDependencies() throws Exception {
        Path targetDirectory = ROOT_DIRECTORY_FOR_POM_FILES.resolve("aPomFileWithoutDependencies");
        File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "1.0")
                .withNoDependencies()
                .inDirectory(targetDirectory)
                .build();

        return new DependencyStateBuilder(file);
    }


    public static DependencyStateBuilder aPomFileWithTwoDirectDependencies() throws Exception {
        Path targetDirectory = ROOT_DIRECTORY_FOR_POM_FILES.resolve("aPomFileWithTwoDirectDependencies");
        File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "1.0")
                .withMavenDependencies(
                        dependencyFor("groupA", "artifactA", "1.0"),
                        dependencyFor("groupB", "artifactB", "1.0")
                )
                .inDirectory(targetDirectory)
                .build();

        return new DependencyStateBuilder(file);
    }

    public static DependencyStateBuilder aPomFileWithOneDirectAndOneTransitiveDependency() throws Exception {
        Path targetDirectory = ROOT_DIRECTORY_FOR_POM_FILES.resolve("aPomFileWithOneDirectAndOneTransitiveDependency");
        //Junit:4.12 depends on org.hamcrest:hamcrest-core:1.3
        File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "1.0")
                .withMavenDependencies(
                        dependencyFor("junit", "junit", "4.12")
                )
                .inDirectory(targetDirectory)
                .build();

        return new DependencyStateBuilder(file);
    }

    public static DependencyStateBuilder aPomFileWithSeveralDependencies() throws Exception {
        Path targetDirectory = ROOT_DIRECTORY_FOR_POM_FILES.resolve("aPomFileWithSeveralDependencies");
        File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "1.0")
                .withMavenDependencies(
                        dependencyFor("junit", "junit", "4.12"),
                        dependencyFor("org.eclipse.aether", "aether-transport-http", "1.1.0")
                )
                .inDirectory(targetDirectory)
                .build();

        return new DependencyStateBuilder(file);
    }

    public static DependencyStateBuilder aPomFileWithDuplicateDependencyButDifferentVersions() throws Exception {
        Path targetDirectory = ROOT_DIRECTORY_FOR_POM_FILES.resolve("aPomFileWithDuplicateDependencyButDifferentVersions");
        File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "1.0")
                .withMavenDependencies(
                        dependencyFor("junit", "junit", "4.12"),
                        dependencyFor("junit", "junit", "4.10"))
                .inDirectory(targetDirectory)
                .build();

        return new DependencyStateBuilder(file);
    }

    public static DependencyStateBuilder aPomFileWithSubModules() throws Exception {
        Path targetDirectory = ROOT_DIRECTORY_FOR_POM_FILES.resolve("aPomFileWithSubModules");
        MultiModuleMavenProjectBuilder.MultiModuleProject project = MultiModuleMavenProjectBuilder.aMultiModuleProject("simpleProject")
                .forArtifact("someGroup", "someArtifact", "1.0")
                .withASubModule(MavenModuleBuilder.aMavenModule()
                        .forArtifact("someGroupA", "someArtifactA", "1.0")
                        .withDependency(
                                dependencyFor("junit", "junit", "4.12")))
                .withASubModule(MavenModuleBuilder.aMavenModule()
                        .forArtifact("someGroupB", "someArtifactB", "1.0")
                        .withDependency(
                                dependencyFor("junit", "junit", "4.10")))
                .inDirectory(targetDirectory)
                .build();

        File file = project.getRootPomFile();
        PomFileLocator pomFileLocator = project.getFilledPomFileLocator();
        return new DependencyStateBuilder(file, pomFileLocator);
    }

    public static DependencyStateBuilder aPomFileWithSubModulesThatHaveSubModules() throws IOException {
        Path targetDirectory = ROOT_DIRECTORY_FOR_POM_FILES.resolve("aPomFileWithSubModulesThatHaveSubModules");
        MultiModuleMavenProjectBuilder.MultiModuleProject project = MultiModuleMavenProjectBuilder.aMultiModuleProject("notSoSimpleProject")
                .forArtifact("someGroup", "someArtifact", "1.0")
                .withASubModule(MultiModuleMavenProjectBuilder.aMultiModuleProject("subModule1")
                        .forArtifact("someGroup", "subModule1", "1.0")
                        .withASubModule(MavenModuleBuilder.aMavenModule()
                                .forArtifact("someGroup", "subModule1.1", "1.0")
                                .withDependency(
                                        dependencyFor("junit", "junit", "4.12", "test")))
                        .withASubModule(MavenModuleBuilder.aMavenModule()
                                .forArtifact("someGroup", "subModule1.2", "1.0")))
                .withASubModule(MultiModuleMavenProjectBuilder.aMultiModuleProject("subModule2")
                        .forArtifact("someGroup", "subModule2", "1.0")
                        .withASubModule(MavenModuleBuilder.aMavenModule()
                                .forArtifact("someGroup", "subModule2.1", "1.0")
                                .withDependency(
                                        dependencyFor("org.mockito", "mockito-all", "1.10.19"))))
                .inDirectory(targetDirectory)
                .build();

        File file = project.getRootPomFile();
        PomFileLocator pomFileLocator = project.getFilledPomFileLocator();
        return new DependencyStateBuilder(file, pomFileLocator);
    }

    public static DependencyStateBuilder aPomFileWithSubModulesThatHaveEachOtherAsDependency() throws Exception {
        Path targetDirectory = ROOT_DIRECTORY_FOR_POM_FILES.resolve("aPomFileWithSubModulesThatHaveEachOtherAsDependency");
        MultiModuleMavenProjectBuilder.MultiModuleProject project = MultiModuleMavenProjectBuilder.aMultiModuleProject("simpleProject")
                .forArtifact("someGroup", "someArtifact", "1.0")
                .withASubModule(MavenModuleBuilder.aMavenModule()
                        .forArtifact("someGroup", "someArtifactA", "1.0")
                        .withDependency(
                                dependencyFor("junit", "junit", "4.12")))
                .withASubModule(MavenModuleBuilder.aMavenModule()
                        .forArtifact("someGroup", "someArtifactB", "1.0")
                        .withDependency(
                                dependencyFor("someGroup", "someArtifactA", "1.0")))
                .inDirectory(targetDirectory)
                .build();

        File file = project.getRootPomFile();
        PomFileLocator pomFileLocator = project.getFilledPomFileLocator();
        return new DependencyStateBuilder(file, pomFileLocator);
    }

    public static DependencyStateBuilder aPomFileWithSubModuleThatHasDependencyToOtherModuleWhichHasSubModules() throws Exception {
        Path targetDirectory = ROOT_DIRECTORY_FOR_POM_FILES.resolve("aPomFileWithSubModuleThatHasDependencyToOtherModuleWhichHasSubModules");
        MultiModuleMavenProjectBuilder.MultiModuleProject project = MultiModuleMavenProjectBuilder.aMultiModuleProject("notSoSimpleProject")
                .forArtifact("someGroup", "someArtifact", "1.0")
                .withASubModule(MultiModuleMavenProjectBuilder.aMultiModuleProject("subModule1")
                        .forArtifact("someGroup", "subModule1", "1.0")
                        .withASubModule(MavenModuleBuilder.aMavenModule()
                                .forArtifact("someGroup", "subModule1.1", "1.0")
                                .withDependency(
                                        dependencyFor("junit", "junit", "4.12", "test")))
                        .withASubModule(MavenModuleBuilder.aMavenModule()
                                .forArtifact("someGroup", "subModule1.2", "1.0")))
                .withASubModule(MavenModuleBuilder.aMavenModule()
                        .forArtifact("someGroup", "subModule2", "1.0")
                        .withDependency(dependencyFor("someGroup", "subModule1", "1.0")))
                .inDirectory(targetDirectory)
                .build();

        File file = project.getRootPomFile();
        PomFileLocator pomFileLocator = project.getFilledPomFileLocator();
        return new DependencyStateBuilder(file, pomFileLocator);
    }

    public static DependencyStateBuilder aPomFileWithTestDependencies() throws IOException {
        Path targetDirectory = ROOT_DIRECTORY_FOR_POM_FILES.resolve("aPomFileWithTestDependencies");
        File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "1.0")
                .withMavenDependencies(
                        dependencyFor("junit", "junit", "4.12", "test")
                )
                .inDirectory(targetDirectory)
                .build();
        return new DependencyStateBuilder(file);
    }

    private static MavenDependency dependencyFor(String group, String artifact, String version, String scope) {
        final ArtifactIdentifier artifactIdentifier = dependencyFor(group, artifact, version);
        final MavenDependency mavenDependency = MavenDependencyBuilder.aMavenDependency()
                .forArtifact(artifactIdentifier)
                .withScope(scope)
                .build();
        return mavenDependency;
    }

    private static ArtifactIdentifier dependencyFor(String group, String artifact, String version) {
        return ArtifactIdentifierBuilder.buildShort(group, artifact, version);
    }

    public DependencyState build() throws Exception {
        final PomFile pomFile = PomFileFactory.createFromFile(this.pomFile, pomFileLocator);
        final DependencyGraphProviderConfiguration dependencyGraphProviderConfiguration = createDependencyGraphProviderConfiguration(pomFile);
        final DependencyState dependencyState = new DependencyState(dependencyGraphProviderConfiguration, pomFile);
        return dependencyState;
    }

    private DependencyGraphProviderConfiguration createDependencyGraphProviderConfiguration(PomFile parentPom) {
        final List<RemoteRepository> repositories = TestRepositories.getRepositories();
        final RepositorySystem repositorySystem = TestRepositorySystem.getRepositorySystem();
        final RepositorySystemSession session = TestRepositorySession.getSession(repositorySystem);

        return new DependencyGraphProviderConfiguration(repositories, repositorySystem, session, parentPom);
    }


    class DependencyState {
        private final PomFile pomFile;
        private final DependencyGraphProviderConfiguration configuration;

        public DependencyState(DependencyGraphProviderConfiguration configuration, PomFile pomFile) {
            this.configuration = configuration;
            this.pomFile = pomFile;
        }

        public PomFile getPomFile() {
            return pomFile;
        }

        public DependencyGraphProviderConfiguration getConfiguration() {
            return configuration;
        }
    }
}
