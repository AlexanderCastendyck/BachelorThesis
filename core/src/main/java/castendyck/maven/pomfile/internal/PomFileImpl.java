package castendyck.maven.pomfile.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.dependencymanagement.DependencyManagement;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileCreationException;
import castendyck.maven.pomfile.PomFileException;
import castendyck.maven.pomfile.PomFileFactory;
import castendyck.maven.properties.PropertyResolver;
import castendyck.mavendependency.MavenDependency;
import castendyck.mavendependency.MavenDependencyBuilder;
import castendyck.mavenproperty.MavenProperties;
import castendyck.pomfilelocator.PomFileLocationException;
import castendyck.pomfilelocator.PomFileLocator;
import castendyck.repository.LocalRepository;
import castendyck.repository.LocalRepositoryFactory;
import de.castendyck.utils.PathUtil;
import org.apache.log4j.Logger;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static castendyck.artifactidentifier.ArtifactIdentifierBuilder.anArtifactIdentifier;
import static de.castendyck.enforcing.NotNullConstraintEnforcer.ensureNotNull;
import static castendyck.maven.pomfile.internal.ModelToDependencyManagementConverter.convertToDependencyManagement;
import static castendyck.maven.pomfile.internal.ModelToMavenPropertiesConverter.convertToProperties;
import static castendyck.maven.properties.PropertyResolver.needsToBeResolved;

public class PomFileImpl implements PomFile {
    private final Logger logger = Logger.getLogger(PomFileImpl.class);
    private final Model model;
    private final PomFileLocator pomFileLocator;

    public PomFileImpl(Model model, PomFileLocator pomFileLocator) {
        ensureNotNull(model);
        this.model = model;
        ensureNotNull(pomFileLocator);
        this.pomFileLocator = pomFileLocator;
    }

    @Override
    public ArtifactIdentifier getArtifactIdentifier() {
        final String groupId = getGroupId();
        final String artifactId = getArtifactId();
        final String version = getVersion();
        final ArtifactIdentifier artifactIdentifier = anArtifactIdentifier()
                .withGroupId(groupId)
                .withArtifactId(artifactId)
                .withVersion(version)
                .build();
        return artifactIdentifier;

    }

    private String getGroupId() {
        final String groupIdValue = model.getGroupId();
        if (groupIdValue != null) {
            final String groupId = resolveIfProperty(groupIdValue);
            return groupId;
        } else {
            final PomFile parent = getParent();
            final ArtifactIdentifier parentArtifactIdentifier = parent.getArtifactIdentifier();
            final String groupId = parentArtifactIdentifier.getGroupId();
            return groupId;
        }
    }

    private String getArtifactId() {
        final String artifactIdValue = model.getArtifactId();
        if (artifactIdValue != null) {
            final String artifactId = resolveIfProperty(artifactIdValue);
            return artifactId;
        }else{
            throw new PomFileException("ArtifactId was not for "+model.getPomFile());
        }
    }

    private String getVersion() {
        String versionValue = model.getVersion();
        if (versionValue != null) {
            final String version = resolveIfProperty(versionValue);
            return version;
        } else {
            final PomFile parent = getParent();
            final ArtifactIdentifier parentArtifactIdentifier = parent.getArtifactIdentifier();
            final String version = parentArtifactIdentifier.getVersion();
            return version;
        }
    }

    private String resolveIfProperty(String value) {
        return resolveIfProperty(value, this);
    }

    private String resolveIfProperty(String value, PomFile pomFile) {
        if (value != null && needsToBeResolved(value)) {
            final String resolveProperty = PropertyResolver.resolveProperty(value, pomFile);
            return resolveProperty;
        }
        return value;
    }

    private String checkDependencyManagementOfPom(String groupId, String artifactId, PomFile pomFile) {
        //logger.info("checkingDependencyManagementOfPom: " + pomFile +" for "+groupId+":"+artifactId);
        final DependencyManagement dependencyManagement = pomFile.getDependencyManagement();
        if (dependencyManagement.containsVersionFor(groupId, artifactId)) {
            final String version = dependencyManagement.getVersionOf(groupId, artifactId);
            return resolveIfProperty(version, pomFile);
        } else {
            return checkDependencyManagementOfParent(groupId, artifactId, pomFile);
        }
    }

    private String checkDependencyManagementOfParent(String groupId, String artifactId, PomFile pomFile) {
        if (pomFile.hasParent()) {
            final PomFile parent = pomFile.getParent();
            //logger.info("   checking DependencyManagement of parent: "+parent);
            return checkDependencyManagementOfPom(groupId, artifactId, parent);
        } else {
            final String message = "Could not find version in DependencyManagement for " + groupId + ":" + artifactId +
                    " in " + model.getPomFile() + " or its parent poms";
            throw new PomFileException(message);
        }
    }

    @Override
    public List<MavenDependency> getDependencies() {
        final List<Dependency> dependencies = model.getDependencies();
        final List<MavenDependency> mavenDependencies = dependencies.stream()
                .map(this::mapToMavenDependency)
                .collect(Collectors.toList());
        return mavenDependencies;
    }

    private MavenDependency mapToMavenDependency(Dependency dependency) {
        final String groupIdValue = dependency.getGroupId();
        final String groupId = resolveIfProperty(groupIdValue);
        final String artifactIdValue = dependency.getArtifactId();
        final String artifactId = resolveIfProperty(artifactIdValue);
        final String version = getVersionOfDependency(dependency, groupId, artifactId);
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId(groupId)
                .withArtifactId(artifactId)
                .withVersion(version)
                .build();

        final String scope = dependency.getScope();
        final MavenDependency mavenDependency = MavenDependencyBuilder.aMavenDependency()
                .forArtifact(artifactIdentifier)
                .withScope(scope)
                .build();
        return mavenDependency;
    }

    private String getVersionOfDependency(Dependency dependency, String groupId, String artifactId) {
        String versionValue = dependency.getVersion();
        if (!isSet(versionValue)) {
            versionValue = checkDependencyManagementOfPom(groupId, artifactId, this);
        }
        return resolveIfProperty(versionValue);
    }

    @Override
    public boolean isMultiModuleProject() throws PomFileLocationException, PomFileCreationException {
        final String packaging = getPackaging();
        return !getModules().isEmpty() && packaging.equalsIgnoreCase("pom");
    }

    @Override
    public List<PomFile> getModules() throws PomFileLocationException, PomFileCreationException {
        final List<String> modules = model.getModules();
        final File projectDirectory = model.getProjectDirectory();

        final List<PomFile> pomFiles = new ArrayList<>();
        for (String module : modules) {
            final Path subDirectory = Paths.get(projectDirectory.getPath(), module);
            final Path pomFilePath = pomFileLocator.locateInDirectory(subDirectory);
            final PomFile pomFile = PomFileFactory.createFromPath(pomFilePath, pomFileLocator);
            pomFiles.add(pomFile);
        }
        return pomFiles;
    }

    @Override
    public MavenProperties getProperties() {
        final MavenProperties mavenProperties = convertToProperties(model);
        return mavenProperties;
    }

    @Override
    public boolean hasParent() {
        final Parent parent = model.getParent();
        return parent != null;
    }

    @Override
    public PomFile getParent() {
        final Parent parent = model.getParent();
        if (parent == null) {
            throw new PomFileException("Expected parent, but no parent was set in " + model.getPomFile());
        }
        final Path pomFilePath;
        if (isParentFromDifferentGroupId(parent)) {
            pomFilePath = getPomPathToDifferentPom(parent);
            //logger.info("getParent via getPomPathToDifferentPom- > "+pomFilePath);
        } else if (relativePathToParentIsSet(parent)) {
            pomFilePath = getPomPathFromRelativePath();
            //logger.info("getParent via getPomPathFromRelativePath -> "+pomFilePath);
        } else {
            pomFilePath = getPathOfParentModule();
            //logger.info("getParent via getPathOfParentModule -> "+pomFilePath);
        }

        return createParentPom(pomFilePath);
    }

    private boolean isParentFromDifferentGroupId(Parent parent) {
        final String groupId = model.getGroupId();
        final String parentGroupId = parent.getGroupId();
        //logger.info("Test if parent is from different groupId. groupId:"+groupId +" -> parent:"+parentGroupId);
        if(groupId == null || !isSet(groupId)){
            return false;
        }
        return !groupId.startsWith(parentGroupId);
    }

    private Path getPomPathToDifferentPom(Parent parent) {
        final LocalRepository localRepository = LocalRepositoryFactory.createNewInStandardMavenLocation();
        final ArtifactIdentifier parentArtifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId(parent.getGroupId())
                .withArtifactId(parent.getArtifactId())
                .withVersion(parent.getVersion())
                .build();
        final Path parentPomFilePath = localRepository.potentialPomFileLocationFor(parentArtifactIdentifier);
        return parentPomFilePath;
    }

    private boolean relativePathToParentIsSet(Parent parent) {
        final String relativePath = parent.getRelativePath();
        final boolean isSet = isSet(relativePath) ;
        return isSet;
    }

    private Path getPomPathFromRelativePath() {
        final String directory = model.getPomFile().getParent();
        final String relativePath = model.getParent().getRelativePath();
        logger.info("  getPomPathFromRelativePath : directory:"+directory+" + "+relativePath);
        return resolveRelativePathToDirectory(relativePath, directory);
    }


    private Path resolveRelativePathToDirectory(String relativePath, String directory) {
        final Path expectedPomFileLocation = PathUtil.resolveRelative(directory, relativePath);
        if (isParentPomFileAndNotOnlyParentPomDirectory(expectedPomFileLocation)) {
            return expectedPomFileLocation;
        } else {
            final Path pomFilePath = locateParentPomFileInDirectory(expectedPomFileLocation);
            return pomFilePath;
        }
    }

    private boolean isParentPomFileAndNotOnlyParentPomDirectory(Path expectedPomFileLocation) {
        return expectedPomFileLocation.toString()
                .endsWith(".xml");
    }

    private Path locateParentPomFileInDirectory(Path expectedPomFileLocation) {
        try {
            return pomFileLocator.locateInDirectory(expectedPomFileLocation);
        } catch (PomFileLocationException e) {
            final String message = e.getMessage();
            throw new PomFileException(message, e);
        }
    }

    private Path getPathOfParentModule() {
        final File pomFile = model.getPomFile();
        final Path path = pomFile.toPath();
        final Path currentDirectory = path.getParent();
        final Path parentDirectory = currentDirectory.getParent();
        return parentDirectory.resolve("pom.xml");
    }

    private PomFile createParentPom(Path pomFilePath) {
        try {
            return PomFileFactory.createFromPath(pomFilePath);
        } catch (PomFileCreationException e) {
            throw new PomFileException(e.getMessage(), e);
        }
    }

    @Override
    public DependencyManagement getDependencyManagement() {
        final DependencyManagement dependencyManagement = convertToDependencyManagement(model);
        return dependencyManagement;
    }

    @Override
    public String getPackaging() {
        final String packaging = model.getPackaging();
        if (isSet(packaging)) {
            final String resolvedPackaging = resolveIfProperty(packaging);
            return resolvedPackaging;
        } else {
            return "jar";
        }
    }

    @Override
    public Path getOutputDirectory() {
        final Build build = model.getBuild();
        Path outputPath;
        if (build == null) {
            outputPath = Paths.get("target/classes");
        } else {
            final String outputDirectory = build.getOutputDirectory();
            if (isSet(outputDirectory)) {
                final String resolvedOutputDir = resolveIfProperty(outputDirectory);
                outputPath = Paths.get(resolvedOutputDir);
                return outputPath;
            } else {
                outputPath = Paths.get("target/classes");
            }
        }
        final Path baseDir = getBaseDir();
        final Path outputAbsolutePath = baseDir.resolve(outputPath).toAbsolutePath();
        return outputAbsolutePath;
    }

    private boolean isSet(String value) {
        return value != null && !value.trim().isEmpty();
    }

    @Override
    public Path getBuildDirectory() {
        final Build build = model.getBuild();
        Path buildPath;
        if (build == null) {
            buildPath = Paths.get("target");
        } else {
            final String buildDirectory = build.getDirectory();
            if (isSet(buildDirectory)) {
                final String resolvedBuildDir = resolveIfProperty(buildDirectory);
                buildPath = Paths.get(resolvedBuildDir);
            } else {
                buildPath = Paths.get("target");
            }
        }
        final Path baseDir = getBaseDir();
        final Path buildDirectoryPath = baseDir.resolve(buildPath).toAbsolutePath();
        return buildDirectoryPath;
    }

    private Path getBaseDir() {
        final File pomFile = model.getPomFile();
        final Path pomPath = pomFile.toPath();
        final Path baseDir = pomPath.getParent();
        return baseDir;
    }

    @Override
    public String toString() {
        return "PomFile: " + model.getPomFile();
    }
}
