package castendyck.maven.pomfile;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.temporarypomfile.TemporaryPomFileBuilder;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PomFileGetParentTestIT {

    @Test
    public void getParent_findsParentInParentLocation() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "1.0");
        TemporaryPomFileBuilder.aPomFile()
                .forArtifact(parentArtifact)
                .inDirectory("target/pomFileTests/getParent_findsParentInParentLocation/")
                .build();
        final File child = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "child", "1.0")
                .withParent(parentArtifact)
                .inDirectory("target/pomFileTests/getParent_findsParentInParentLocation/submodule")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(child);

        final PomFile parentPom = pomFile.getParent();
        assertThat(parentPom.getArtifactIdentifier(), equalTo(parentArtifact));
    }

    @Test
    public void getParent_findsParentWhenParentRelativeLocationIsSetToExpectedParentLocation() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "1.0");
        TemporaryPomFileBuilder.aPomFile()
                .forArtifact(parentArtifact)
                .inDirectory("target/pomFileTests/getParent_findsParentWhenParentRelativeLocationIsSetToExpectedParentLocation/")
                .build();
        final File child = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "child", "1.0")
                .withParent(parentArtifact)
                .withRelativeParentPath("../pom.xml")
                .inDirectory("target/pomFileTests/getParent_findsParentWhenParentRelativeLocationIsSetToExpectedParentLocation/submodule")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(child);

        final PomFile parentPom = pomFile.getParent();
        assertThat(parentPom.getArtifactIdentifier(), equalTo(parentArtifact));
    }

    @Test
    public void getParent_findsParentWhenParentRelativeLocationIsSetToArbitraryLocation() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "1.0");
        TemporaryPomFileBuilder.aPomFile()
                .forArtifact(parentArtifact)
                .inDirectory("target/pomFileTests/getParent_findsParentWhenParentRelativeLocationIsSet/")
                .build();
        final File child = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "child", "1.0")
                .withParent(parentArtifact)
                .withRelativeParentPath("../../pom.xml")
                .inDirectory("target/pomFileTests/getParent_findsParentWhenParentRelativeLocationIsSet/intermediateDirectory/submodule")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(child);

        final PomFile parentPom = pomFile.getParent();
        assertThat(parentPom.getArtifactIdentifier(), equalTo(parentArtifact));
    }

    @Test
    public void getParent_findsParentWhenParentRelativeLocationIsSetToAnotherSubModule() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "1.0");
        TemporaryPomFileBuilder.aPomFile()
                .forArtifact(parentArtifact)
                .inDirectory("target/pomFileTests/getParent_findsParentWhenParentRelativeLocationIsSetToAnotherSubModule/differentSubmodule")
                .build();
        final File child = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "child", "1.0")
                .withParent(parentArtifact)
                .withRelativeParentPath("../differentSubmodule/pom.xml")
                .inDirectory("target/pomFileTests/getParent_findsParentWhenParentRelativeLocationIsSetToAnotherSubModule/submodule")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(child);

        final PomFile parentPom = pomFile.getParent();
        assertThat(parentPom.getArtifactIdentifier(), equalTo(parentArtifact));
    }

    @Test
    public void getParent_findsParentWhenParentRelativeLocationIsToDirectory() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "1.0");
        TemporaryPomFileBuilder.aPomFile()
                .forArtifact(parentArtifact)
                .inDirectory("target/pomFileTests/getParent_findsParentWhenParentRelativeLocationIsToDirectory/")
                .build();
        final File child = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "child", "1.0")
                .withParent(parentArtifact)
                .withRelativeParentPath("../../")
                .inDirectory("target/pomFileTests/getParent_findsParentWhenParentRelativeLocationIsToDirectory/intermediateDirectory/submodule")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(child);

        final PomFile parentPom = pomFile.getParent();
        assertThat(parentPom.getArtifactIdentifier(), equalTo(parentArtifact));
    }

    @Test
    public void getParent_findsParentWhenParentRelativeLocationIsEmpty() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "1.0");
        TemporaryPomFileBuilder.aPomFile()
                .forArtifact(parentArtifact)
                .inDirectory("target/pomFileTests/getParent_findsParentWhenParentRelativeLocationIsEmpty/")
                .build();
        final File child = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "child", "1.0")
                .withParent(parentArtifact)
                .withRelativeParentPath("")
                .inDirectory("target/pomFileTests/getParent_findsParentWhenParentRelativeLocationIsEmpty/submodule")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(child);

        final PomFile parentPom = pomFile.getParent();
        assertThat(parentPom.getArtifactIdentifier(), equalTo(parentArtifact));
    }

    @Test
    public void getParent_findsParentWhenParentIsPomFromADifferentGroupId() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("org.sonatype.oss", "oss-parent", "7");

        final File child = TemporaryPomFileBuilder.aPomFile()
                .forArtifact("someGroup", "child", "1.0")
                .withParent(parentArtifact)
                .inDirectory("target/pomFileTests/getParent_findsParentWhenParentIsPomFromADifferentGroupId/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(child);

        final PomFile parentPom = pomFile.getParent();
        assertThat(parentPom.getArtifactIdentifier(), equalTo(parentArtifact));
    }

}