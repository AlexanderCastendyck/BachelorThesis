package castendyck.analyzing;

import castendyck.analysisresult.AnalysisResult;
import castendyck.analysisresult.internal.AnalysisResultImpl;
import castendyck.callgraph.CallPath;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;

public class Then {
    private final AnalysisResult analysisResult;

    public Then(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public void thenResultShouldHaveNoVulnerableControlPaths() {
        final List<CallPath> remainingCallPaths = collectAllRemainingCallPaths();
        assertThat(remainingCallPaths, hasSize(0));
    }

    public void thenResultShouldHaveNoRemainingControlPaths() {
        final List<CallPath> remainingCallPaths = collectAllRemainingCallPaths();
        assertThat(remainingCallPaths, hasSize(0));
    }

    private List<CallPath> collectAllRemainingCallPaths() {
        return analysisResult.getAnalyzedCves().stream()
                .flatMap(analyzedCve -> analyzedCve.getJudgement()
                        .getReduction()
                        .getRemainingCallPaths()
                        .stream())
                .collect(Collectors.toList());
    }

    public void thenResultShouldShouldContainVulnerableCallPaths() {
        fail();
    }
}
