package castendyck.callgraph.internal;

import castendyck.callgraph.CallPath;
import castendyck.callgraph.FunctionIdentifierAlreadyPresentInThisCallGraphException;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.vulnerablepoint.VulnerablePoint;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CallPathImpl implements CallPath {
    private final List<FunctionIdentifier> functions;

    @Nullable
    private VulnerablePoint vulnerablePoint;

    public CallPathImpl(VulnerablePoint vulnerablePoint, List<FunctionIdentifier> functions) {
        this.vulnerablePoint = vulnerablePoint;
        NotNullConstraintEnforcer.ensureNotNull(functions);
        this.functions = functions;
    }


    @Override
    public boolean containsFunctionIdentifier(FunctionIdentifier functionIdentifier) {
        final boolean contains = functions.contains(functionIdentifier);
        return contains;
    }

    @Override
    public void addFunctionIdentifier(FunctionIdentifier functionIdentifier) throws FunctionIdentifierAlreadyPresentInThisCallGraphException {
        ensureFunctionIdentifierIsNotAlreadyStored(functionIdentifier);
        functions.add(functionIdentifier);
    }

    private void ensureFunctionIdentifierIsNotAlreadyStored(FunctionIdentifier functionIdentifier) throws FunctionIdentifierAlreadyPresentInThisCallGraphException {
        if (containsFunctionIdentifier(functionIdentifier)) {
            throw new FunctionIdentifierAlreadyPresentInThisCallGraphException();
        }
    }

    @Override
    public FunctionIdentifier getCurrentFunctionIdentifier() {
        final int indexLastElement = functions.size() - 1;
        final FunctionIdentifier functionIdentifier = functions.get(indexLastElement);
        return functionIdentifier;
    }

    @Override
    public FunctionIdentifier getStartingFunctionIdentifier() {
        final FunctionIdentifier functionIdentifier = functions.get(0);
        return functionIdentifier;
    }

    @Override
    public List<FunctionIdentifier> getFunctions() {
        return functions;
    }

    @Nullable
    @Override
    public VulnerablePoint getAssociatedVulnerablePoint() {
        return vulnerablePoint;
    }

    @Override
    public CallPath cloneCallPath() {
        final List<FunctionIdentifier> functions = new ArrayList<>(this.functions);
        final CallPathImpl clonedCallPath = new CallPathImpl(vulnerablePoint, functions);
        return clonedCallPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallPathImpl callPath = (CallPathImpl) o;

        if (!functions.equals(callPath.functions)) return false;
        return vulnerablePoint != null ? vulnerablePoint.equals(callPath.vulnerablePoint) : callPath.vulnerablePoint == null;

    }

    @Override
    public int hashCode() {
        int result = functions.hashCode();
        result = 31 * result + (vulnerablePoint != null ? vulnerablePoint.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CallPathImpl{" +
                "functions=" + functions +
                ", vulnerablePoint=" + vulnerablePoint +
                '}';
    }
}
