package castendyck.vulnerablepoint.cveimporting;

import org.junit.Test;

import static castendyck.vulnerablepoint.cveimporting.VulnerableMavenProjectBuilder.*;

public class CveImporterTestIT {

    @Test
    public void projectWithNoVulnerability_returnsEmptyCveList() throws Exception {
        Given.given(aMavenProjectWithNoVulnerabilities())
                .whenCvesAreAnalyzed()
                .thenNoneShouldBeFound();
    }

    @Test
    public void projectWithOneVulnerability_returnsOneCve() throws Exception {
        Given.given(aMavenProjectWithOneVulnerability())
                .whenCvesAreAnalyzed()
                .thenOneCveShouldBeFound();
    }

    @Test
    public void projectWithComponentWithTwoVulnerabilities_returnsTwoCves() throws Exception {
        Given.given(aMavenProjectWithComponentWithTwoVulnerabilities())
                .whenCvesAreAnalyzed()
                .thenTwoShouldBeFound();
    }

    @Test
    public void projectWithSeveralVulnerabilities_returnsAllCves() throws Exception {
        Given.given(aMavenProjectWithSeveralVulnerabilities())
                .whenCvesAreAnalyzed()
                .thenAllShouldBeFound();
    }


    @Test
    public void projectWithTwoSubModulesHavingAVulnerabilityEach_returnsTwoCves() throws Exception {
        Given.given(VulnerableMavenProjectBuilder.aMavenProjectWithTwoSubModulesHavingAVulnerabilityEach())
                .whenCvesAreAnalyzed()
                .thenTwoShouldBeFound();
    }

    @Test
    public void projectWithNoArtifacts_returnsEmptyCveList() throws Exception {
        Given.given(VulnerableMavenProjectBuilder.aMavenProjectWithNoArtifacts())
                .whenCvesAreAnalyzed()
                .thenNoneShouldBeFound();
    }

}
