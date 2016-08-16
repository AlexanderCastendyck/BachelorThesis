package castendyck.maven.pomfile;

import castendyck.temporarypomfile.TemporaryPomFileBuilder;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;

import static castendyck.temporarypomfile.BuildTestBuilder.aBuild;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PomFileGetBuildDirTestIT {
    @Test
    public void getBuildDirectory_returnsValueAsInPomFile() throws Exception {
        final String expectedBuildDirecotry = "target2";
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .with(aBuild()
                        .withBuildDirectory(expectedBuildDirecotry))
                .inDirectory("target/pomFileTests/getBuildDirectory_returnsValueAsInPomFile/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final Path outputDirectory = pomFile.getBuildDirectory();
        assertThat(outputDirectory.toString(), endsWith(expectedBuildDirecotry));
    }

    @Test
    public void getBuildDirectory_returnsDefaultValueIfNotSet() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .with(aBuild()
                        .withBuildDirectory(""))
                .inDirectory("target/pomFileTests/getBuildDirectory_returnsDefaultValueIfNotSet/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final Path outputDirectory = pomFile.getBuildDirectory();
        assertThat(outputDirectory.toString(), endsWith("target"));
    }

    @Test
    public void getBuildDirectory_resolvesPropertyCorrectly() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .with(aBuild()
                        .withBuildDirectory("${buildDirProperty}"))
                .withAProperty("buildDirProperty", "targetDifferent")
                .inDirectory("target/pomFileTests/getBuildDirectory_resolvesPropertyCorrectly/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final Path outputDirectory = pomFile.getBuildDirectory();
        assertThat(outputDirectory.toString(), endsWith("targetDifferent"));
    }

    @Test
    public void getBuildDirectory_getsCorrectAbsolutePath() throws Exception {
        final String extraDirectories = "dirA/dirB/dirC";
        final String targetDirectory = "target/pomFileTests/getBuildDirectory_getsCorrectAbsolutePath/" + extraDirectories;
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .inDirectory( targetDirectory)
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final Path outputDirectory = pomFile.getBuildDirectory();
        final String expectedDirectorySuffix = targetDirectory + "/target";
        assertThat(outputDirectory.toString(), endsWith(expectedDirectorySuffix));
    }
}
