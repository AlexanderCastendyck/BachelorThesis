package castendyck.callgraph;

import castendyck.cve.CVE;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.vulnerablepoint.VulnerablePoint;

import javax.annotation.Nullable;
import java.util.List;

public interface CallPath {
    boolean containsFunctionIdentifier(FunctionIdentifier functionIdentifier);

    void addFunctionIdentifier(FunctionIdentifier functionIdentifier) throws FunctionIdentifierAlreadyPresentInThisCallGraphException;

    FunctionIdentifier getCurrentFunctionIdentifier();

    FunctionIdentifier getStartingFunctionIdentifier();

    List<FunctionIdentifier> getFunctions();

    CallPath cloneCallPath();

    @Nullable VulnerablePoint getAssociatedVulnerablePoint();
}
