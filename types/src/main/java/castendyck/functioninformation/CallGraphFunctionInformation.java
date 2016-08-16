package castendyck.functioninformation;

import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.functionidentifier.FunctionIdentifier;

import java.util.List;

public class CallGraphFunctionInformation extends FunctionInformation {
    private final List<FunctionIdentifier> calledFunctions;
    private final List<FunctionIdentifier> functionsCalledBy;

    public CallGraphFunctionInformation(FunctionIdentifier informationFor, List<FunctionIdentifier> calledFunctions, List<FunctionIdentifier> functionsCalledBy) {
        NotNullConstraintEnforcer.ensureNotNull(informationFor);
        this.informationFor = informationFor;
        NotNullConstraintEnforcer.ensureNotNull(calledFunctions);
        this.calledFunctions = calledFunctions;
        NotNullConstraintEnforcer.ensureNotNull(functionsCalledBy);
        this.functionsCalledBy = functionsCalledBy;
    }

    public FunctionIdentifier getInformationFor() {
        return this.informationFor;
    }

    public List<FunctionIdentifier> getFunctionsCalledBy() {
        return this.functionsCalledBy;
    }

    public List<FunctionIdentifier> getCalledFunctions() {
        return calledFunctions;
    }
}
