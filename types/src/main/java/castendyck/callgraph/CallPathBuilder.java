package castendyck.callgraph;

import castendyck.callgraph.internal.CallPathImpl;
import castendyck.cve.CVE;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CallPathBuilder {
    private final List<FunctionIdentifier> functionIdentifiers;
    private VulnerablePoint vulnerablePoint;

    public CallPathBuilder() {
        this.functionIdentifiers = new ArrayList<>();
    }

    public static CallPathBuilder aCallPath() {
        return new CallPathBuilder();
    }

    public CallPathBuilder forVulnerablePoint(VulnerablePoint vulnerablePoint) {
        this.vulnerablePoint = vulnerablePoint;
        return this;
    }

    public CallPathBuilder containingFunctionIdentifier(FunctionIdentifier functionIdentifier) {
        functionIdentifiers.add(functionIdentifier);
        return this;
    }

    public CallPathBuilder containingFunctionIdentifiers(FunctionIdentifier... functionIdentifiersToAdd) {
        Arrays.stream(functionIdentifiersToAdd)
                .forEach(functionIdentifiers::add);
        return this;
    }

    public CallPath build() {
        CallPath callPath = new CallPathImpl(vulnerablePoint, functionIdentifiers);
        return callPath;
    }

}
