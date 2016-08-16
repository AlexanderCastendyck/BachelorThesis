package castendyck.maven.pomfile;

import castendyck.mavenproperty.MavenProperties;
import org.junit.Test;

import java.io.File;

import static castendyck.temporarypomfile.TemporaryPomFileBuilder.aPomFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PomFilePropertyTestIT {

    @Test
    public void getProperties_includesImportantMandatoryProperties() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .withPackaging("war")
                .inDirectory("target/pomFileTests/getProperties_includesImportantMandatoryProperties/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final MavenProperties properties = pomFile.getProperties();
        assertThat(properties.getPropertyValue("project.groupId"), equalTo("someGroup"));
        assertThat(properties.getPropertyValue("project.artifactId"), equalTo("someArtifact"));
        assertThat(properties.getPropertyValue("project.version"), equalTo("123"));
        assertThat(properties.getPropertyValue("project.packaging"), equalTo("war"));
    }

    @Test
    public void getProperties_usesDefaultValuesIfPropertyNotSet() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .inDirectory("target/pomFileTests/getProperties_usesDefaultValuesIfPropertyNotSet/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final MavenProperties properties = pomFile.getProperties();
        assertThat(properties.getPropertyValue("project.packaging"), equalTo("jar"));
    }

    @Test
    public void getProperties_includesUserProperties() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .inDirectory("target/pomFileTests/getProperties_includesImportantMandatoryEvenIfNotSet/")
                .withAProperty("property1", "1")
                .withAProperty("property2", "ABC")
                .withAProperty("property3", "something different")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final MavenProperties properties = pomFile.getProperties();
        assertThat(properties.getPropertyValue("property1"), equalTo("1"));
        assertThat(properties.getPropertyValue("property2"), equalTo("ABC"));
        assertThat(properties.getPropertyValue("property3"), equalTo("something different"));
    }

    @Test
    public void getProperties_containsNoPropertiesNotbeingSet() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .inDirectory("target/pomFileTests/getProperties_includesImportantMandatoryEvenIfNotSet/")
                .withAProperty("something", "")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);


        final MavenProperties properties = pomFile.getProperties();
        assertThat(properties.containsProperty("something"), is(false));
    }
}
