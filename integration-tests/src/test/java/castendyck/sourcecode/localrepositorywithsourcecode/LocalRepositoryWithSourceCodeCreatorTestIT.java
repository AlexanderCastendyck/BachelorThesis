package castendyck.sourcecode.localrepositorywithsourcecode;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.directorybuilding.DirectoryBuilder;
import castendyck.filebuilding.FileBuilder;
import castendyck.inmemorycompiling.ByteCode;
import castendyck.localrepository.LocalRepositoryBuilder;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileCreationException;
import castendyck.maven.pomfile.PomFileFactory;
import castendyck.pomfilelocator.PomFileLocationException;
import castendyck.repository.LocalRepository;
import castendyck.sourcecode.jarpacking.internal.JarPackerImpl;
import castendyck.sourcecode.localrepositorywithsourcecode.internal.LocalRepositoryWithSourceCodeCreatorImpl;
import castendyck.temporarypomfile.TemporaryPomFileBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.jar.JarFile;

import static castendyck.inmemorycompiling.ByteCodeBuilder.aByteCode;
import static castendyck.inmemorycompiling.classes.NormalClassSourceCodeBuilder.sourceCodeForAClass;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class LocalRepositoryWithSourceCodeCreatorTestIT {
    private final static Path TEST_ROOT_DIR = Paths.get("target/LocalRepositoryWithSourceCodeCreatorTests");
    private final LocalRepositoryWithSourceCodeCreator localRepositoryWithSourceCodeCreator;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    public LocalRepositoryWithSourceCodeCreatorTestIT() {
        this.localRepositoryWithSourceCodeCreator = LocalRepositoryWithSourceCodeCreatorFactory.newInstance();
    }

    @Test
    public void givenNoAdditionalClassFiles_localRepositoryContainsOnlyClassOfMavensLocalRepository() throws Exception {
        final List<ByteCode> byteCodes = aByteCode().forAClass(sourceCodeForAClass()
                .named("someClass"))
                .build();
        final Path directory = DirectoryBuilder.aDirectory(TEST_ROOT_DIR + "/givenNoAdditionalClassFilesTest/")
                .with(DirectoryBuilder.aSubDirectory("target")
                        .with(DirectoryBuilder.aSubDirectory("classes")))
                .build();
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .locatedAt(directory)
                .containing(byteCodes)
                .build();
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "someArtifact", "1.0");
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "differentArtifact", "1.0")
                .inDirectory(directory)
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final LocalRepository extendedLocalRepository = act(localRepository, pomFile);

        final Path locationOfArtifact = extendedLocalRepository.potentialJarFileLocationFor(artifactIdentifier);
        assertThat(locationOfArtifact, equalTo(localRepository.potentialJarFileLocationFor(artifactIdentifier)));
    }

    @Test
    public void givenOneAdditionalClassFiles_repositoryContainsProjectsJar() throws Exception {
        final Path directory = DirectoryBuilder.aDirectory(TEST_ROOT_DIR + "/givenOneAdditionalClassFiles_repositoryContainsProjectsJar/")
                .with(DirectoryBuilder.aSubDirectory("target")
                        .with(DirectoryBuilder.aSubDirectory("classes")
                                .with(FileBuilder.aFile("main.class"))))
                .build();
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .locatedAt(directory)
                .build();
        final ArtifactIdentifier projectArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "differentArtifact", "1.0");
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact(projectArtifact)
                .inDirectory(directory)
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file)
                ;
        final LocalRepository extendedLocalRepository = act(localRepository, pomFile);

        final Path locationOfArtifact = extendedLocalRepository.potentialJarFileLocationFor(projectArtifact);
        assertThat(locationOfArtifact.toString(), endsWith("/target/temporaryJars/differentArtifact-1.0.jar"));
    }

    @Test
    public void givenOneAdditionalClassFiles_repositoryContainsJarWithClassFile() throws Exception {
        final Path directory = DirectoryBuilder.aDirectory(TEST_ROOT_DIR + "/givenOneAdditionalClassFiles_repositoryContainsJarWithClassFile/")
                .with(DirectoryBuilder.aSubDirectory("target")
                        .with(DirectoryBuilder.aSubDirectory("classes")
                                .with(FileBuilder.aFile("main.class"))))
                .build();
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .locatedAt(directory)
                .build();
        final ArtifactIdentifier projectArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "differentArtifact", "1.0");
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact(projectArtifact)
                .inDirectory(directory)
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final LocalRepository extendedLocalRepository = act(localRepository, pomFile);

        final JarFile jarFile = jarFileFor(projectArtifact, extendedLocalRepository);
        assertThat(jarFile, is(JarFileMatcher.aJarFileContaining("main.class")));
    }

    @Test
    public void givenOneAdditionalClassFiles_classFilesHaveCorrectPackage() throws Exception {
        final Path directory = DirectoryBuilder.aDirectory(TEST_ROOT_DIR + "/givenOneAdditionalClassFiles_classFilesHaveCorrectPackage/")
                .with(DirectoryBuilder.aSubDirectory("target")
                        .with(DirectoryBuilder.aSubDirectory("classes")
                                .with(DirectoryBuilder.aSubDirectory("org")
                                        .with(DirectoryBuilder.aSubDirectory("apache")
                                                .with(FileBuilder.aFile("main.class"))))))
                                .build();
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .locatedAt(directory)
                .build();
        final ArtifactIdentifier projectArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "differentArtifact", "1.0");
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact(projectArtifact)
                .inDirectory(directory)
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final LocalRepository extendedLocalRepository = act(localRepository, pomFile);

        final JarFile jarFile = jarFileFor(projectArtifact, extendedLocalRepository);
        assertThat(jarFile, is(JarFileMatcher.aJarFileContaining("org/apache/main.class")));
    }

    private LocalRepository act(LocalRepository localRepository, PomFile pomFile) throws PomFileLocationException, PomFileCreationException, IOException {
        return localRepositoryWithSourceCodeCreator.extendLocalRepositoryWithSourceCode(localRepository, pomFile);
    }

    private JarFile jarFileFor(ArtifactIdentifier projectArtifact, LocalRepository extendedLocalRepository) throws IOException {
        final Path locationOfArtifact = extendedLocalRepository.potentialJarFileLocationFor(projectArtifact);
        return new JarFile(locationOfArtifact.toFile());
    }
}
