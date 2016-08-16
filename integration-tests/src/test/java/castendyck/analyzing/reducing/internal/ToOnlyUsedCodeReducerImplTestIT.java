package castendyck.analyzing.reducing.internal;

import org.junit.Test;

import static castendyck.analyzing.reducing.internal.Given.given;
import static castendyck.analyzing.reducing.internal.ToOnlyUsedCodeReducerExamples.*;
import static castendyck.analyzing.reducing.internal.ToOnlyUsedCodeReducerExamples.projectWithOneModuleWithOneUsedCallPathsInRootAndOneUsedCallPathInTheSubModule;

public class ToOnlyUsedCodeReducerImplTestIT {

    @Test
    public void givenNoCallPaths_reducedResultShouldContainNoCallPathsEither() throws Exception {
        given(noCallPaths())
                .whenReduced()
                .thenExpectResultWithNoCallPaths();
    }

    @Test
    public void givenOneUsedAndOneNotUsedCallPath_reducedResultShouldContainOnlyUsedCallPath() throws Exception {
        given(oneUsedAndOneNotUsedCallPath())
                .whenReduced()
                .thenExpectResultWithOneCallPath();
    }

    @Test
    public void givenThreeNotUsedCallPaths_reducedResultShouldContainNoCallPaths() throws Exception {
        given(threeNotUsedCallPaths())
                .whenReduced()
                .thenExpectResultWithNoCallPaths();
    }

    @Test
    public void givenThreeUsedCallPaths_reducedResultShouldContainAllThreeCallPaths() throws Exception {
        given(threeUsedCallPaths())
                .whenReduced()
                .thenExpectResultWithAllThreeCallPaths();
    }

    @Test
    public void givenProjectWithToSubModulesWithOneUsedCallPathsInEach_reducedResultShouldContainBothCallPaths() throws Exception {
        given(projectWithToSubModulesWithOneUsedCallPathsInEach())
                .whenReduced()
                .thenExpectResultWithTwoCallPaths();
    }

    @Test
    public void givenProjectWithOneModuleWithOneUsedCallPathsInRootAndOneUsedInTheSubModule_reducedResultShouldContainOnlyBothCallPath() throws Exception {
        given(projectWithOneModuleWithOneUsedCallPathsInRootAndOneUsedCallPathInTheSubModule())
                .whenReduced()
                .thenExpectResultWithTwoCallPaths();
    }
}
