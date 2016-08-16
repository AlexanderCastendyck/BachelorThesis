package castendyck.analyzing.reducing.internal;

import org.junit.Test;

import static castendyck.analyzing.reducing.internal.CallPathNeverTraversesAnArtifactWithTestScopeReducerExamples.*;
import static castendyck.analyzing.reducing.internal.CallPathNeverTraversesAnArtifactWithTestScopeReducerExamples.noCallPaths;
import static castendyck.analyzing.reducing.internal.Given.given;

public class CallPathNeverTraversesAnArtifactWithTestScopeReducerImplTestIT {

    @Test
    public void givenNoCallPaths_reducedResultShouldContainNoCallPathsEither() throws Exception {
        given(noCallPaths())
                .whenReduced()
                .thenExpectResultWithNoCallPaths();
    }
    @Test
    public void givenOneCallPathWithTestScopeAndOneWithout_reducedResultShouldContainOneCallPath() throws Exception {
        given(oneCallPathWithTestScopeAndOneWithout())
                .whenReduced()
                .thenExpectResultWithOneCallPath();
    }

    @Test
    public void givenTwoCallPathsWithTestScope_reducedResultShouldContainNoCallPath() throws Exception {
        given(twoCallPathsWithTestScope())
                .whenReduced()
                .thenExpectResultWithNoCallPaths();
    }

    @Test
    public void givenTwoCallPathsWithCompileScope_reducedResultShouldContainBothCallPaths() throws Exception {
        given(twoCallPathsWithCompileScope())
                .whenReduced()
                .thenExpectResultWithTwoCallPaths();
    }

    @Test
    public void givenACallPathTraversingSeveralArtifactsEndingInTestScope_reducedResultShouldContainNoCallPath() throws Exception {
        given(aCallPathTraversingSeveralArtifactsEndingInTestScope())
                .whenReduced()
                .thenExpectResultWithNoCallPaths();
    }

    @Test
    public void givenAProjectWithTwoSubModulesOneHavingACallPathWithTestScopeAndTheOtherOneWithCompileScope_reducedResultShouldContainJustOneCallPath() throws Exception {
        given(aProjectWithTwoSubModulesOneHavingACallPathWithTestScopeAndTheOtherOneWithCompileScope())
                .whenReduced()
                .thenExpectResultWithOneCallPath();
    }
}
