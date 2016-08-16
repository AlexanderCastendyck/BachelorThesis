package castendyck.temporarypomfile;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.dependency.Dependency;
import de.castendyck.file.FileUtils;
import castendyck.mavendependency.MavenDependency;
import castendyck.mavendependency.MavenDependencyBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TemporaryPomFileBuilder {
    private final List<MavenDependency> dependencies;
    private final Map<String, String> properties;
    private Path directory;
    private ArtifactIdentifierInformation artifactIdentifierInformation;
    private List<String> modules;
    private String packaging;
    private String name;
    private ArtifactIdentifier parent;
    private String parentRelativePath;
    private DependencyManagementTestBuilder dependencyManagementTestBuilder;
    private BuildTestBuilder buildTestBuilder;

    private TemporaryPomFileBuilder() {
        this.dependencies = new ArrayList<>();
        this.modules = new ArrayList<>();
        this.packaging = "jar";
        this.name = "pom.xml";

        final String currentDirectory = System.getProperty("user.dir");
        final Path targetPath = Paths.get(currentDirectory, "target", "generated-pom-files-for-tests");
        this.directory = targetPath;
        this.properties = new HashMap<>();
    }

    public static TemporaryPomFileBuilder aPomFile() throws IOException {
        return new TemporaryPomFileBuilder();
    }

    public TemporaryPomFileBuilder forArtifact(String groupId, String artifactId, String version) {
        this.artifactIdentifierInformation = new ArtifactIdentifierInformation(groupId, artifactId, version);
        return this;
    }

    public TemporaryPomFileBuilder forArtifact(ArtifactIdentifier artifactIdentifier) {
        final String groupId = artifactIdentifier.getGroupId();
        final String artifactId = artifactIdentifier.getArtifactId();
        final String version = artifactIdentifier.getVersion();
        this.artifactIdentifierInformation = new ArtifactIdentifierInformation(groupId, artifactId, version);
        return this;
    }

    public TemporaryPomFileBuilder withNoDependencies() {
        return this;
    }

    public TemporaryPomFileBuilder withMavenDependencies(ArtifactIdentifier... artifactIdentifiers) {
        Arrays.stream(artifactIdentifiers)
                .forEach(this::withDependency);
        return this;
    }

    public TemporaryPomFileBuilder withMavenDependencies(MavenDependency... mavenDependencies) {
        Arrays.stream(mavenDependencies)
                .forEach(this::withDependency);
        return this;
    }

    public TemporaryPomFileBuilder withMavenDependencies(List<MavenDependency> mavenDependencies) {
        mavenDependencies.stream()
                .forEach(this::withDependency);
        return this;
    }

    public TemporaryPomFileBuilder withDependencies(List<Dependency> dependencies) {
        dependencies.stream()
                .map(Dependency::getArtifactIdentifier)
                .forEach(this::withDependency);
        return this;
    }

    public TemporaryPomFileBuilder withDependency(String groupId, String artifactId, String version) {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort(groupId, artifactId, version);
        return withDependency(artifactIdentifier);
    }

    public TemporaryPomFileBuilder withDependency(ArtifactIdentifier artifactIdentifier) {
        final MavenDependency mavenDependency = MavenDependencyBuilder.aMavenDependency()
                .forArtifact(artifactIdentifier)
                .withScope("compile")
                .build();
        return withDependency(mavenDependency);
    }

    public TemporaryPomFileBuilder withDependency(MavenDependency mavenDependency) {
        dependencies.add(mavenDependency);
        return this;
    }

    public TemporaryPomFileBuilder withModules(List<String> moduleNames) {
        this.modules = moduleNames;
        return this;
    }

    public TemporaryPomFileBuilder withPackaging(String packaging) {
        this.packaging = packaging;
        return this;
    }

    public TemporaryPomFileBuilder inDirectory(String directory) {
        final Path path = Paths.get(directory);
        return inDirectory(path);
    }

    public TemporaryPomFileBuilder inDirectory(Path directory) {
        this.directory = directory;
        return this;
    }

    public TemporaryPomFileBuilder withParent(ArtifactIdentifier parentArtifact) {
        this.parent = parentArtifact;
        return this;
    }

    public TemporaryPomFileBuilder withRelativeParentPath(String parentRelativePath) {
        this.parentRelativePath = parentRelativePath;
        return this;
    }

    public TemporaryPomFileBuilder named(String name) {
        this.name = name;
        return this;
    }

    public TemporaryPomFileBuilder withAProperty(String propertyName, String propertyValue) {
        this.properties.put(propertyName, propertyValue);
        return this;
    }

    public TemporaryPomFileBuilder with(DependencyManagementTestBuilder dependencyManagementTestBuilder) {
        this.dependencyManagementTestBuilder = dependencyManagementTestBuilder;
        return this;
    }

    public TemporaryPomFileBuilder with(BuildTestBuilder buildTestBuilder) {
        this.buildTestBuilder = buildTestBuilder;
        return this;
    }

    public File build() throws IOException {
        final String pomFileContent = createPomFileContent();
        final File file = createPomFileWithContent(pomFileContent);
        return file;
    }

    private String createPomFileContent() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "         <modelVersion>4.0.0</modelVersion>\n\n");

        if (parent != null) {
            appendParent(stringBuilder);
        }

        artifactIdentifierInformation.print(stringBuilder);
        stringBuilder.append("<packaging>")
                .append(packaging)
                .append("</packaging>\n");

        if (!properties.isEmpty()) {
            appendProperties(stringBuilder);
        }
        appendModules(stringBuilder);

        if (dependencyManagementTestBuilder != null) {
            dependencyManagementTestBuilder.writeToStringBuilder(stringBuilder);
        }
        appendDependencies(stringBuilder);

        if (buildTestBuilder != null) {
            buildTestBuilder.writeToStringBuilder(stringBuilder);
        }

        stringBuilder.append("</project>");
        return stringBuilder.toString();
    }

    private void appendParent(StringBuilder stringBuilder) {
        stringBuilder.append("<parent>");
        appendArtifact(stringBuilder, parent);

        if (parentRelativePath != null) {
            stringBuilder.append("<relativePath>")
                    .append(parentRelativePath)
                    .append("</relativePath>");
        }

        stringBuilder.append("</parent>");
    }

    private void appendProperties(StringBuilder stringBuilder) {
        stringBuilder.append("<properties>");

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            final String propertyName = entry.getKey();
            final String propertyValue = entry.getValue();
            stringBuilder.append("<").append(propertyName).append(">")
                    .append(propertyValue)
                    .append("</").append(propertyName).append(">")
                    .append("\n");
        }
        stringBuilder.append("</properties>");
    }

    private void appendModules(StringBuilder stringBuilder) {
        if (!modules.isEmpty()) {
            stringBuilder.append("<modules>\n");

            modules.stream()
                    .forEach(m -> stringBuilder.append("<module>").append(m).append("</module>\n"));

            stringBuilder.append("</modules>\n\n");
        }
    }

    private void appendDependencies(StringBuilder stringBuilder) {
        if (!dependencies.isEmpty()) {
            stringBuilder.append("\n\n<dependencies>\n");
            dependencies.stream().forEach(d -> appendDependency(stringBuilder, d));
            stringBuilder.append("</dependencies>\n\n");
        }
    }

    private void appendDependency(StringBuilder stringBuilder, MavenDependency dependency) {
        stringBuilder.append("<dependency>\n");

        final ArtifactIdentifier artifactIdentifier = dependency.getArtifactIdentifier();
        appendArtifact(stringBuilder, artifactIdentifier);

        final String scope = dependency.getScope().asString();
        stringBuilder.append("<scope>").append(scope).append("</scope>\n");

        stringBuilder.append("</dependency>\n");
    }

    private void appendArtifact(StringBuilder stringBuilder, ArtifactIdentifier artifactIdentifier) {
        stringBuilder.append("<groupId>").append(artifactIdentifier.getGroupId()).append("</groupId>\n")
                .append("<artifactId>").append(artifactIdentifier.getArtifactId()).append("</artifactId>\n")
                .append("<version>").append(artifactIdentifier.getVersion()).append("</version>\n");
    }

    private File createPomFileWithContent(String pomFileContent) throws IOException {
        FileUtils.createThisAndParentDirectories(directory);

        final Path filePath = directory.resolve(name);
        final Path tempPath = FileUtils.createOrOverrideFile(filePath);
        final File file = tempPath.toFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(pomFileContent);
        fileWriter.close();
        return file;
    }

    private class ArtifactIdentifierInformation {
        private String groupId;
        private String artifactId;
        private String version;


        public ArtifactIdentifierInformation(String groupId, String artifactId, String version) {

            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }

        public void print(StringBuilder stringBuilder) {
            if (groupId != null) {
                stringBuilder.append("<groupId>").append(groupId).append("</groupId>\n");
            }
            if (artifactId != null) {
                stringBuilder.append("<artifactId>").append(artifactId).append("</artifactId>\n");
            }
            if (version != null) {
                stringBuilder.append("<version>").append(version).append("</version>\n");
            }
        }
    }
}
