package castendyck.maven.pomfile;

import castendyck.temporarypomfile.TemporaryPomFileBuilder;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;

import static castendyck.temporarypomfile.BuildTestBuilder.aBuild;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class PomFileGetOutputDirectoryTestIT {
    @Test
    public void getOutputDirectory_returnsValueAsInPomFile() throws Exception {
        final String expectedOututPath = "different/dir";
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .with(aBuild()
                        .withOutpoutDirectory(expectedOututPath))
                .inDirectory("target/pomFileTests/getOutputDirectory_returnsValueAsInPomFile/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final Path outputDirectory = pomFile.getOutputDirectory();
        assertThat(outputDirectory.toString(), endsWith(expectedOututPath));
    }

    @Test
    public void getOutputDirectory_returnsDefaultValueIfNotSet() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .with(aBuild()
                        .withOutpoutDirectory(""))
                .inDirectory("target/pomFileTests/getOutputDirectory_returnsDefaultValueIfNotSet/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final Path outputDirectory = pomFile.getOutputDirectory();
        assertThat(outputDirectory.toString(), endsWith("target/classes"));
    }

    @Test
    public void getOutputDirectory_resolvesPropertyCorrectly() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .with(aBuild()
                        .withOutpoutDirectory("${outputDirProperty}"))
                .withAProperty("outputDirProperty", "target/here")
                .inDirectory("target/pomFileTests/getOutputDirectory_resolvesPropertyCorrectly/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final Path outputDirectory = pomFile.getOutputDirectory();
        assertThat(outputDirectory.toString(), endsWith("target/here"));
    }

    @Test
    public void getOutputDirectory_getsCorrectAbsolutePath() throws Exception {
        final String extraDirectories = "dirA/dirB/dirC";
        final String targetDirectory = "target/pomFileTests/getOutputDirectory_getsCorrectAbsolutePath/" + extraDirectories;
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .inDirectory(targetDirectory)
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final Path outputDirectory = pomFile.getOutputDirectory();
        final String expectedDirectorySuffix = targetDirectory + "/target/classes";
        assertThat(outputDirectory.toString(), endsWith(expectedDirectorySuffix));
    }
}
