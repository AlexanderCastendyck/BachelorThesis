package castendyck.maven.pomfile;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.temporarypomfile.TemporaryPomFileBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PomFileGetArtifactIdentifierTestIT {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getArtifactIdentifier_containsCorrectValues() throws Exception {
        final ArtifactIdentifier expectedArtifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "artifact", "3.5");
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact(expectedArtifactIdentifier)
                .inDirectory("target/pomFileTests/getArtifactIdentifier_inheritsGroupIdFromParent/child")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final ArtifactIdentifier artifactIdentifier = pomFile.getArtifactIdentifier();
        assertThat(artifactIdentifier, equalTo(expectedArtifactIdentifier));
    }

    @Test
    public void getArtifactIdentifier_inheritsGroupIdFromParent() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "3.5");
        TemporaryPomFileBuilder.aPomFile()
                .forArtifact(parentArtifact)
                .inDirectory("target/pomFileTests/getArtifactIdentifier_inheritsGroupIdFromParent/")
                .build();
        final File child = TemporaryPomFileBuilder.aPomFile()
                .forArtifact(null, "child", "3.5")
                .withParent(parentArtifact)
                .inDirectory("target/pomFileTests/getArtifactIdentifier_inheritsGroupIdFromParent/child")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(child);

        final ArtifactIdentifier artifactIdentifier = pomFile.getArtifactIdentifier();
        assertThat(artifactIdentifier.getGroupId(), equalTo("someGroup"));
    }

    @Test
    public void getArtifactIdentifier_inheritsVersionFromParent() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "5.2");
        TemporaryPomFileBuilder.aPomFile()
                .forArtifact(parentArtifact)
                .inDirectory("target/pomFileTests/getArtifactIdentifier_inheritsVersionFromParent/")
                .build();
        final File child = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "child", null)
                .withParent(parentArtifact)
                .inDirectory("target/pomFileTests/getArtifactIdentifier_inheritsVersionFromParent/child")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(child);

        final ArtifactIdentifier artifactIdentifier = pomFile.getArtifactIdentifier();
        assertThat(artifactIdentifier.getVersion(), equalTo("5.2"));
    }

    @Test
    public void getArtifactIdentifier_failsForNotSetGroupId() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact(null, "artifact", "1.0")
                .inDirectory("target/pomFileTests/getArtifactIdentifier_failsForNotSetGroupId")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        expectedException.expect(PomFileException.class);
        pomFile.getArtifactIdentifier();
    }

    @Test
    public void getArtifactIdentifier_failsForNotSetArtifactId() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", null, "1.0")
                .inDirectory("target/pomFileTests/getArtifactIdentifier_failsForNotSetArtifactId")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        expectedException.expect(PomFileException.class);
        pomFile.getArtifactIdentifier();
    }

    @Test
    public void getArtifactIdentifier_failsForNotSetVersion() throws Exception {
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "artifact", null)
                .inDirectory("target/pomFileTests/getArtifactIdentifier_failsForNotSetVersion")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        expectedException.expect(PomFileException.class);
        pomFile.getArtifactIdentifier();
    }

}
