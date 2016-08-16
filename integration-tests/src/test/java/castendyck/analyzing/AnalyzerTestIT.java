package castendyck.analyzing;

import org.junit.Ignore;
import org.junit.Test;

public class AnalyzerTestIT {

    @Test
    public void givenNoCallPaths_resultShouldHaveNoVulnerableCallPaths() throws Exception {
        Given.given(AnalyzerTestExamples.noCallPaths())
                .whenAnalyzed()
                .thenResultShouldHaveNoVulnerableControlPaths();
    }

    @Test
    public void givenCallPathsNotUsed_resultShouldHaveNoRemainingCallPaths() throws Exception {
        Given.given(AnalyzerTestExamples.callPathsNotUsed())
                .whenAnalyzed()
                .thenResultShouldHaveNoRemainingControlPaths();
    }

    @Ignore
    @Test
    public void givenCallPathsNotUsedInProduction_resultShouldHaveNoRemainingCallPaths() throws Exception {
        Given.given(AnalyzerTestExamples.callPathsNotUsedInProduction())
                .whenAnalyzed()
                .thenResultShouldHaveNoRemainingControlPaths();
    }

    @Ignore
    @Test
    public void givenCallPathsNotUsedSocketsButHavingACveTargetingSockets_resultShouldHaveNoRemainingCallPaths() throws Exception {
        Given.given(AnalyzerTestExamples.callPathsNotUsedSocketButHavingACveTargetingSockets())
                .whenAnalyzed()
                .thenResultShouldHaveNoRemainingControlPaths();
    }

    @Ignore
    @Test
    public void givenVulnerableCallPaths_resultShouldContainVulnerableCallPaths() throws Exception {
        Given.given(AnalyzerTestExamples.vulnerableCallPaths())
                .whenAnalyzed()
                .thenResultShouldShouldContainVulnerableCallPaths();
    }
}
