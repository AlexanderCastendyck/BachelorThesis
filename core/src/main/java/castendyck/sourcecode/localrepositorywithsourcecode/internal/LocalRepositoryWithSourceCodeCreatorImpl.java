package castendyck.sourcecode.localrepositorywithsourcecode.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileCreationException;
import castendyck.pomfilelocator.PomFileLocationException;
import castendyck.repository.LocalRepository;
import castendyck.sourcecode.jarpacking.JarPacker;
import castendyck.sourcecode.localrepositorywithsourcecode.LocalRepositoryWithSourceCodeCreator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalRepositoryWithSourceCodeCreatorImpl implements LocalRepositoryWithSourceCodeCreator {
    private final static Pattern INCLUDED_PACKAGING_TYPES = Pattern.compile("(jar|war|ear)");
    private final JarPacker jarPacker;

    public LocalRepositoryWithSourceCodeCreatorImpl(JarPacker jarPacker) {
        this.jarPacker = jarPacker;
    }

    @Override
    public LocalRepository extendLocalRepositoryWithSourceCode(LocalRepository localRepository, PomFile parentPom) throws PomFileLocationException, PomFileCreationException, IOException {
        final LocalRepositoryWithSourceCodeBuilder builder = LocalRepositoryWithSourceCodeBuilder.aLocalRepositoryWithSourceCode()
                .withLocalRepository(localRepository);

        fillSourceCodeOfModuleAndItsSubModulesIn(parentPom, builder);

        final LocalRepositoryWithSourceCode repositoryWithSourceCode = builder.build();
        return repositoryWithSourceCode;
    }

    private void fillSourceCodeOfModuleAndItsSubModulesIn(PomFile pomFile, LocalRepositoryWithSourceCodeBuilder builder) throws PomFileLocationException, PomFileCreationException, IOException {
        fillSourceCodeOfModuleIn(pomFile, builder);
        if(pomFile.isMultiModuleProject()){
            final List<PomFile> modules = pomFile.getModules();
            for (PomFile module : modules) {
                fillSourceCodeOfModuleAndItsSubModulesIn(module, builder);
            }
        }
    }

    private void fillSourceCodeOfModuleIn(PomFile pomFile, LocalRepositoryWithSourceCodeBuilder builder) throws IOException {
        final String packaging = pomFile.getPackaging();
        final String lowerCasePackaging = packaging.trim().toLowerCase();
        final Matcher matcher = INCLUDED_PACKAGING_TYPES.matcher(lowerCasePackaging);
        if(matcher.matches()){
            final Path targetDirectory = determineOutputDirectory(pomFile);
            final ArtifactIdentifier artifactIdentifier = pomFile.getArtifactIdentifier();
            final String fileName = createFileNameFor(artifactIdentifier);
            final Path path = jarPacker.packDirectory(pomFile.getOutputDirectory(), fileName, targetDirectory);
            builder.withASourceCodeJar(artifactIdentifier, path);
        }
    }

    private Path determineOutputDirectory(PomFile pomFile) {
        final Path buildDirectory = pomFile.getBuildDirectory();
        final Path targetDirectory = buildDirectory.resolve("temporaryJars");
        return targetDirectory;
    }

    private String createFileNameFor(ArtifactIdentifier artifactIdentifier) {
        final String artifactId = artifactIdentifier.getArtifactId();
        final String version = artifactIdentifier.getVersion();
        final String name = artifactId + "-" + version + ".jar";
        return name;
    }

}
