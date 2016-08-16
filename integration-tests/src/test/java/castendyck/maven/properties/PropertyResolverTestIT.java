package castendyck.maven.properties;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileFactory;
import castendyck.mavenproperty.NotSupportedPropertyException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;

import static castendyck.temporarypomfile.TemporaryPomFileBuilder.aPomFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PropertyResolverTestIT {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void resolveProperty_findsPropertyInPomFile() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .inDirectory("target/pomFileTests/resolveProperty_findsPropertyInPomFile/")
                .withAProperty("property", "123")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final String propertyValue = PropertyResolver.resolveProperty("property", pomFile);

        assertThat(propertyValue, equalTo("123"));
    }

    @Test
    public void resolveProperty_findsMandatoryPropertyInPomFile() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .inDirectory("target/pomFileTests/resolveProperty_findsMandatoryPropertyInPomFile/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final String propertyValue = PropertyResolver.resolveProperty("project.groupId", pomFile);

        assertThat(propertyValue, equalTo("someGroup"));
    }

    @Test
    public void resolveProperty_findsMandatoryPropertyInParentPomFile() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "123");
        aPomFile()
                .forArtifact(parentArtifact)
                .withAProperty("testProp", "123")
                .inDirectory("target/pomFileTests/resolveProperty_findsMandatoryPropertyInParentPomFile/")
                .build();
        final File file = aPomFile()
                .forArtifact("someGroup", "child", "123")
                .withParent(parentArtifact)
                .inDirectory("target/pomFileTests/resolveProperty_findsMandatoryPropertyInParentPomFile/submodule/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final String propertyValue = PropertyResolver.resolveProperty("testProp", pomFile);

        assertThat(propertyValue, equalTo("123"));
    }

    @Test
    public void resolveProperty_findsWrappedProperty() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "someArtifact", "1337")
                .inDirectory("target/pomFileTests/resolveProperty_findsWrappedProperty/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final String propertyValue = PropertyResolver.resolveProperty("${project.version}", pomFile);

        assertThat(propertyValue, equalTo("1337"));
    }

    @Test
    public void resolveProperty_findsWrappedPropertyInParentPomFile() throws Exception {
        final ArtifactIdentifier parentArtifact = ArtifactIdentifierBuilder.buildShort("someGroup", "parent", "123");
        aPomFile()
                .forArtifact(parentArtifact)
                .withAProperty("test_Prop", "hello here")
                .inDirectory("target/pomFileTests/resolveProperty_findsWrappedPropertyInParentPomFile/")
                .build();
        final File file = aPomFile()
                .forArtifact("someGroup", "child", "123")
                .withParent(parentArtifact)
                .inDirectory("target/pomFileTests/resolveProperty_findsWrappedPropertyInParentPomFile/submodule/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final String propertyValue = PropertyResolver.resolveProperty("${test_Prop}", pomFile);

        assertThat(propertyValue, equalTo("hello here"));
    }

    @Test
    public void resolveProperty_findsPropertyThatReferencesADifferentProeprty() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "child", "123")
                .withAProperty("type", "realValue")
                .withAProperty("reference", "${type}")
                .inDirectory("target/pomFileTests/resolveProperty_findsPropertyThatReferencesADifferentProeprty/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final String propertyValue = PropertyResolver.resolveProperty("${reference}", pomFile);

        assertThat(propertyValue, equalTo("realValue"));
    }

    @Test
    public void resolveProperty_throwsErrorWhenNotSupportedPropertyIsUsed() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "child", "123")
                .withAProperty("notSupportedProperty", "${project.dependencyManagement}")
                .inDirectory("target/pomFileTests/resolveProperty_throwsErrorWhenNotSupportedPropertyIsUsed/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        expectedException.expect(NotSupportedPropertyException.class);
        PropertyResolver.resolveProperty("${notSupportedProperty}", pomFile);
    }
}
