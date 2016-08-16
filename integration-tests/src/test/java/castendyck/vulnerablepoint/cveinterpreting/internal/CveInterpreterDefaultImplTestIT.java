package castendyck.vulnerablepoint.cveinterpreting.internal;

import org.junit.Test;

import static castendyck.vulnerablepoint.cveinterpreting.internal.CveInterpreterDefaultExamples.*;
import static castendyck.vulnerablepoint.cveinterpreting.internal.CveInterpreterDefaultExamples.exampleWithOneCallFromDependentArtifactThatDoesNotEndInTargetArtifact;

public class CveInterpreterDefaultImplTestIT {

    @Test
    public void givenNoCallFromDependentArtifact_expectPositiveResultWithNoVulnerablePoint() throws Exception {
        Given.given(exampleWithNoCallFromDependentArtifact())
                .whenCveIsInterpreted()
                .expectPositiveResultWithNoVulnerablePoint();
    }

    @Test
    public void givenOneCallFromDependentArtifact_expectPositiveResultWithOneVulnerablePoint() throws Exception {
        Given.given(exampleWithOneCallFromDependentArtifact())
                .whenCveIsInterpreted()
                .expectPositiveResultWithOneVulnerablePoint();
    }

    @Test
    public void givenTwoCallsFromDependentArtifacts_expectPositiveResultWithOneVulnerablePoint() throws Exception {
        Given.given(exampleWithTwoCallsFromDependentArtifacts())
                .whenCveIsInterpreted()
                .expectPositiveResultWithOneVulnerablePoint();
    }

    @Test
    public void givenTwoCallsFromDependentArtifactToTwoDifferentFunctions_expectPositiveResultWithTwoVulnerablePoint() throws Exception {
        Given.given(exampleWithTwoCallsFromDependentArtifactToTwoDifferentFunctions())
                .whenCveIsInterpreted()
                .expectPositiveResultWithTwoVulnerablePoints();
    }

    @Test
    public void givenOneCallFromDependentArtifactCallingFurtherInArtifact_expectPositiveResultWithOneVulnerablePoint() throws Exception {
        Given.given(exampleWithOneCallFromDependentArtifactCallingFurtherInArtifact())
                .whenCveIsInterpreted()
                .expectPositiveResultWithOneVulnerablePoint();
    }

    @Test
    public void givenOneCallFromDependentArtifactResultingInTwoCallPathsInArtifact_expectPositiveResultWithTwoVulnerablePoint() throws Exception {
        Given.given(exampleWithOneCallFromDependentArtifactResultingInTwoCallPathsInArtifact())
                .whenCveIsInterpreted()
                .expectPositiveResultWithTwoVulnerablePoints();
    }

    @Test
    public void givenOneCallFromDependentArtifactThatDoesNotEndInTargetArtifact_expectPositiveResultWithNoVulnerablePoint() throws Exception {
        Given.given(exampleWithOneCallFromDependentArtifactThatDoesNotEndInTargetArtifact())
                .whenCveIsInterpreted()
                .expectPositiveResultWithNoVulnerablePoint();
    }
}
