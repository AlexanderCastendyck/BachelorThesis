package castendyck.analyzing.reducing.internal;

import org.junit.Test;

import static castendyck.analyzing.reducing.internal.NoClassesInMavenTestDirectoryReducerExamples.*;
import static castendyck.analyzing.reducing.internal.NoClassesInMavenTestDirectoryReducerExamples.aCallPathEndingInClassDeepInTestDirectory;

public class NoClassesInMavenTestDirectoryReducerTestIT {

    @Test
    public void givenNoCallPaths_shouldReturnNoCallsPathsEither() throws Exception {
        Given.given(noCallPaths())
                .whenReduced()
                .thenExpectResultWithNoCallPaths();
    }

    @Test
    public void givenACallPathEndingInTest_shouldReturnNoCallPaths() throws Exception {
        Given.given(aCallPathEndingInTest())
                .whenReduced()
                .thenExpectResultWithNoCallPaths();
    }

    @Test
    public void givenACallPathEndingInProduction_shouldReturnOnePath() throws Exception {
        Given.given(aCallPathEndingInProduction())
                .whenReduced()
                .thenExpectResultWithOneCallPath();
    }

    @Test
    public void givenACallPathEndingInTestAndOneInProduction_shouldReturnOneCallPath() throws Exception {
        Given.given(aCallPathEndingInTestAndOneInProduction())
                .whenReduced()
                .thenExpectResultWithOneCallPath();
    }

    @Test
    public void givenACallPathEndingInClassDeepInTestDirectory_shouldReturnNoCallPaths() throws Exception {
        Given.given(aCallPathEndingInClassDeepInTestDirectory())
                .whenReduced()
                .thenExpectResultWithNoCallPaths();
    }
}
