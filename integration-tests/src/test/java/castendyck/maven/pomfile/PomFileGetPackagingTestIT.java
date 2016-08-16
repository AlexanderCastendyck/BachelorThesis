package castendyck.maven.pomfile;

import castendyck.temporarypomfile.TemporaryPomFileBuilder;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PomFileGetPackagingTestIT {

    @Test
    public void getPackaging_returnsValueAsInPomFile() throws Exception {
        final String expectedPackaging = "war";
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withPackaging(expectedPackaging)
                .inDirectory("target/pomFileTests/getPackaging_returnsValueAsInPomFile/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final String packaging = pomFile.getPackaging();
        assertThat(packaging, equalTo(expectedPackaging));
    }

    @Test
    public void getPackaging_returnsJarAsDefault() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withPackaging("")
                .inDirectory("target/pomFileTests/getPackaging_returnsJarAsDefault/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final String packaging = pomFile.getPackaging();
        assertThat(packaging, equalTo("jar"));
    }

    @Test
    public void getPackaging_resolvesPropertyInPackageTag() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withPackaging("${packagingProperty}")
                .withAProperty("packagingProperty", "ear")
                .inDirectory("target/pomFileTests/getPackaging_resolvesPropertyInPackageTag/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final String packaging = pomFile.getPackaging();
        assertThat(packaging, equalTo("ear"));
    }
}
