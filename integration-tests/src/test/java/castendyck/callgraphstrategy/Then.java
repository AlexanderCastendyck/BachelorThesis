package castendyck.callgraphstrategy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Then {
    private final CallGraphStrategy strategy;

    public Then(CallGraphStrategy strategy) {
        this.strategy = strategy;
    }

    public void thenExpect(RoadMapGuidedStrategyTester strategyTester) throws StrategyUnexpectedStateException {
       boolean isCorrectStrategy =  strategyTester.testStrategy(strategy);
        assertThat(isCorrectStrategy, is(true));
    }
}
