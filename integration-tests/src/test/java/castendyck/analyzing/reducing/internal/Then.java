package castendyck.analyzing.reducing.internal;

import castendyck.callgraph.CallPath;
import castendyck.reduction.Reduction;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class Then {
    private final Reduction reduction;
    private final Reduction expectedReduction;

    public Then(Reduction reduction, Reduction expectedReduction) {
        this.reduction = reduction;
        this.expectedReduction = expectedReduction;
    }

    public void thenExpectResultWithNoCallPaths() {
        final List<CallPath> remainingCallPaths = reduction.getRemainingCallPaths();
        assertThat(remainingCallPaths, hasSize(0));
    }

    public void thenExpectResultWithOneCallPath() {
        final List<CallPath> remainingCallPaths = reduction.getRemainingCallPaths();
        assertThat(remainingCallPaths, hasSize(1));

        checkIfReductionMatchesExpectedReduction();
    }

    public void thenExpectResultWithTwoCallPaths() {
        final List<CallPath> remainingCallPaths = reduction.getRemainingCallPaths();
        assertThat(remainingCallPaths, hasSize(2));

        checkIfReductionMatchesExpectedReduction();
    }

    public void thenExpectResultWithAllThreeCallPaths() {
        final List<CallPath> remainingCallPaths = reduction.getRemainingCallPaths();
        assertThat(remainingCallPaths, hasSize(3));

        checkIfReductionMatchesExpectedReduction();
    }

    private void checkIfReductionMatchesExpectedReduction() {
        assertThat(reduction, equalTo(expectedReduction));
    }
}
