package castendyck.maven.pomfile;

import castendyck.dependencymanagement.DependencyManagement;
import castendyck.mavenproperty.MavenProperties;
import castendyck.temporarypomfile.DependencyManagementTestBuilder;
import org.junit.Test;

import java.io.File;

import static castendyck.temporarypomfile.DependencyManagementTestBuilder.aDependencyManagement;
import static castendyck.temporarypomfile.TemporaryPomFileBuilder.aPomFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PomFileGetDependencyManagementTestIT {

    @Test
    public void getDependencyManagement_hasNoVersionInformationForNotExistingDependencyManagement() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .inDirectory("target/pomFileTests/getDependencyManagement_hasNoVersionInformationForNotExistingDependencyManagement/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final DependencyManagement dependencyManagement = pomFile.getDependencyManagement();
        assertThat(dependencyManagement.containsVersionFor("someGroup", "someArtifact"), is(false));
    }

    @Test
    public void getDependencyManagement_hasNoVersionInformationEmptyDependencyManagement() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .with(aDependencyManagement())
                .inDirectory("target/pomFileTests/getDependencyManagement_hasNoVersionInformationEmptyDependencyManagement/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final DependencyManagement dependencyManagement = pomFile.getDependencyManagement();
        assertThat(dependencyManagement.containsVersionFor("someGroup", "someArtifact"), is(false));
    }

    @Test
    public void getDependencyManagement_hasVersionInformation() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .with(aDependencyManagement()
                        .withAManagedDependency("someGroup", "managedArtifact", "1.0"))
                .inDirectory("target/pomFileTests/getDependencyManagement_hasVersionInformation/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final DependencyManagement dependencyManagement = pomFile.getDependencyManagement();
        assertThat(dependencyManagement.containsVersionFor("someGroup", "managedArtifact"), is(true));
        final String version = dependencyManagement.getVersionOf("someGroup", "managedArtifact");
        assertThat(version, equalTo("1.0"));
    }

    @Test
    public void getDependencyManagement_hasNoVersionInformationWhenVersionNotPresent() throws Exception {
        final File file = aPomFile()
                .forArtifact("someGroup", "someArtifact", "123")
                .with(aDependencyManagement()
                        .withAManagedDependency("someGroup", "managedArtifact"))
                .inDirectory("target/pomFileTests/getDependencyManagement_hasNoVersionInformationWhenVersionNotPresent/")
                .build();
        final PomFile pomFile = PomFileFactory.createFromFile(file);

        final DependencyManagement dependencyManagement = pomFile.getDependencyManagement();
        assertThat(dependencyManagement.containsVersionFor("someGroup", "managedArtifact"), is(false));
    }

}
