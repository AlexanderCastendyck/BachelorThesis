package castendyck.functioninformation;

import castendyck.functionidentifier.FunctionIdentifier;

public abstract class FunctionInformation {
    protected FunctionIdentifier informationFor;

    public FunctionIdentifier getFunctionIdentifier() {
        return informationFor;
    }
}
