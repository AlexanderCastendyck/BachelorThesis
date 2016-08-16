package castendyck.callgraphstrategy;

import castendyck.callgraphstrategy.internal.CallGraphStrategyCalculatorImpl;

public class CallGraphStrategyCalculatorFactory {
    public static CallGraphStrategyCalculatorImpl newInstance() {
        return new CallGraphStrategyCalculatorImpl();
    }
}
