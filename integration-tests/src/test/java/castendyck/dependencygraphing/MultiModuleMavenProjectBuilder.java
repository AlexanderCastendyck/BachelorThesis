package castendyck.dependencygraphing;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import de.castendyck.file.FileUtils;
import castendyck.pomfilelocator.PomFileLocator;
import castendyck.temporarypomfile.TemporaryPomFileBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiModuleMavenProjectBuilder {
    private ArtifactIdentifier artifact;
    private List<MavenModuleBuilder> subModules = new ArrayList<>();
    private Path currentPath;
    private List<MultiModuleMavenProjectBuilder> multiModuleSubModules = new ArrayList<>();

    public MultiModuleMavenProjectBuilder(String directory) {
        final String workingDir = System.getProperty("user.dir");
        this.currentPath = Paths.get(workingDir, "target", "generated-pom-files-for-tests", directory);
    }


    public static MultiModuleMavenProjectBuilder aMultiModuleProject(String directoryName) {
        return new MultiModuleMavenProjectBuilder(directoryName);
    }


    public MultiModuleMavenProjectBuilder forArtifact(String group, String artifact, String version) {
        this.artifact = ArtifactIdentifierBuilder.buildShort(group, artifact, version);
        return this;
    }

    public MultiModuleMavenProjectBuilder withASubModule(MavenModuleBuilder mavenModuleBuilder) {
        this.subModules.add(mavenModuleBuilder);
        return this;
    }

    public MultiModuleMavenProjectBuilder withASubModule(MultiModuleMavenProjectBuilder multiModuleMavenProjectBuilder) {
        this.multiModuleSubModules.add(multiModuleMavenProjectBuilder);
        return this;
    }

    public MultiModuleMavenProjectBuilder inDirectory(Path directory) {
        final Path absolutePath = directory.toAbsolutePath();
        this.currentPath = absolutePath;
        return this;
    }

    private void setPathRelativeToParentPath(Path parentPath) {
        final String moduleName = artifact.getArtifactId();
        this.currentPath = parentPath.resolve(moduleName);
    }

    public MultiModuleProject build() throws IOException {
        FileUtils.createThisAndParentDirectories(currentPath);

        List<String> moduleNames = determineModuleNames();
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact(artifact)
                .withModules(moduleNames)
                .withPackaging("pom")
                .inDirectory(currentPath)
                .build();

        final PomFileLocatorForTemporaryPomFiles pomFileLocator = new PomFileLocatorForTemporaryPomFiles();
        pomFileLocator.registerModule(currentPath, file);
        fillSubModulesIntoPomFileLocator(pomFileLocator);

        final Path parent = currentPath.getParent();
        return new MultiModuleProject(file, pomFileLocator, parent);
    }

    private void fillSubModulesIntoPomFileLocator(PomFileLocatorForTemporaryPomFiles pomFileLocator) throws IOException {
        for (MavenModuleBuilder subModule : subModules) {
            subModule.setParentDirectory(currentPath);
            final File pomFile = subModule.build();
            final String artifactName = subModule.getArtifactName();
            pomFileLocator.registerModule(artifactName, pomFile);
        }

        for (MultiModuleMavenProjectBuilder multiModuleSubModule : multiModuleSubModules) {
            multiModuleSubModule.setPathRelativeToParentPath(currentPath);
            final MultiModuleProject multiModuleProject = multiModuleSubModule.build();
            pomFileLocator.merge(multiModuleProject.pomFileLocator);
        }
    }

    private List<String> determineModuleNames(){
        List<String> moduleNames = subModules.stream()
                .map(MavenModuleBuilder::getArtifactName)
                .collect(Collectors.toList());
        multiModuleSubModules.stream()
                .map(m -> m.artifact.getArtifactId())
                .forEach(moduleNames::add);

        return moduleNames;
    }

    class MultiModuleProject {
        private final File pomFile;
        private final PomFileLocatorForTemporaryPomFiles pomFileLocator;
        private final Path pathToCleanUp;

        MultiModuleProject(File pomFile, PomFileLocatorForTemporaryPomFiles pomFileLocator, Path pathToCleanUp) {
            this.pomFile = pomFile;
            this.pomFileLocator = pomFileLocator;
            this.pathToCleanUp = pathToCleanUp;
        }

        public File getRootPomFile() {
            return pomFile;
        }

        public PomFileLocator getFilledPomFileLocator() {
            return pomFileLocator;
        }

        public Path getPathToCleanUp() {
            return pathToCleanUp;
        }
    }

    private class PomFileLocatorForTemporaryPomFiles implements PomFileLocator {
        private Map<Path, Path> moduleToPomFileMap = new HashMap<>();

        public void merge(PomFileLocatorForTemporaryPomFiles pomFileLocator) {
            this.moduleToPomFileMap.putAll(pomFileLocator.moduleToPomFileMap);
        }

        public void registerModule(String name, File file) {
            final Path modulePath = currentPath.resolve(name);

            final Path pomFile = file.toPath();
            moduleToPomFileMap.put(modulePath, pomFile);
        }

        public void registerModule(Path path, File file) {
            final Path pomFile = file.toPath();
            moduleToPomFileMap.put(path, pomFile);
        }

        @Override
        public Path locateInDirectory(Path directory) {
            return moduleToPomFileMap.get(directory);
        }
    }
}
