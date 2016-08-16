package castendyck.functionCalls;

import castendyck.functionidentifier.FunctionIdentifier;

public class FunctionCall {
    private final FunctionIdentifier callingFunction;
    private final FunctionIdentifier targetFunction;

    public FunctionCall(FunctionIdentifier callingFunction, FunctionIdentifier targetFunction) {
        this.callingFunction = callingFunction;
        this.targetFunction = targetFunction;
    }

    public FunctionIdentifier getCallingFunction() {
        return callingFunction;
    }

    public FunctionIdentifier getTargetFunction() {
        return targetFunction;
    }
}
