package castendyck.callgraph;

import java.util.List;

import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

public class Then {
    private final List<CallPath> callPaths;
    private final List<CallPath> expectedCallPaths;

    public Then(List<CallPath> callPaths, List<CallPath> expectedCallPaths) {

        this.callPaths = callPaths;
        this.expectedCallPaths = expectedCallPaths;
    }

    public void thenResultShouldContainExpectedCallGraph() {
        assertThat(callPaths, equalTo(expectedCallPaths));
    }

    public void thenResultShouldContainTheTwoExpectedControlFlow() {
        assertThat(callPaths, hasSize(2));
        assertThat(callPaths, equalTo(expectedCallPaths));
    }
}
