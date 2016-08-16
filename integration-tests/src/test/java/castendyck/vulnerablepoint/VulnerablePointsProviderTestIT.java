package castendyck.vulnerablepoint;

import org.junit.Ignore;
import org.junit.Test;

public class VulnerablePointsProviderTestIT {

    @Test
    public void collectVulnerablePointsForAProjectWithNoDependencies_findsNoVulnerablePoints() throws Exception {
        Given.given(ProjectWithVulnerablePointsBuilder.aMavenProjectWithNoDependencies())
                .whenVulnerablePointsAreCollected()
                .thenNoVulnerablePointsShouldBeFound();
    }

    @Test
    public void collectVulnerablePointsForAProjectWithNoVulnerabilities_findsNoVulnerablePoints() throws Exception {
        Given.given(ProjectWithVulnerablePointsBuilder.aMavenProjectWithNoVulnerabilities())
                .whenVulnerablePointsAreCollected()
                .thenNoVulnerablePointsShouldBeFound();
    }

    @Test
    public void collectVulnerablePointsForAProjectWithOneVulnerabilityWithOneLocation_findsOneVulnerablePoint() throws Exception {
        Given.given(ProjectWithVulnerablePointsBuilder.aMavenProjectWithOneVulnerabilityWithOneLocation())
                .whenVulnerablePointsAreCollected()
                .thenOneVulnerablePointShouldBeFound();
    }

    @Ignore
    @Test
    public void collectVulnerablePointsForAProjectWithTwoVulnerabilityWithOneLocationEach_findsOneVulnerablePointEach() throws Exception {
        Given.given(ProjectWithVulnerablePointsBuilder.aMavenProjectWithTwoVulnerabilityWithOneLocationEach())
                .whenVulnerablePointsAreCollected()
                .thenTwoVulnerablePointShouldBeFound();
    }

    @Ignore
    @Test
    public void collectVulnerablePointsForAProjectWithOneVulnerabilityWithSeveralLocations_findsAllVulnerablePoint() throws Exception {
        Given.given(ProjectWithVulnerablePointsBuilder.aMavenProjectWithOneVulnerabilityWithSeveralLocations())
                .whenVulnerablePointsAreCollected()
                .thenAllVulnerablePointShouldBeFound();
    }

    @Ignore
    @Test
    public void collectVulnerablePointsForAMultiModuleProjectWithVulnerabilities_findsAllVulnerablePoint() throws Exception {
        Given.given(ProjectWithVulnerablePointsBuilder.aMultiModuleProjectWithVulnerabilities())
                .whenVulnerablePointsAreCollected()
                .thenAllVulnerablePointShouldBeFound();
    }

}
