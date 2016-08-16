package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.CallPath;
import castendyck.callgraph.CallPathBuilder;
import castendyck.callgraphstrategy.CallGraphStrategy;
import castendyck.callgraphstrategy.StrategyUnexpectedStateException;
import castendyck.functionidentifier.FunctionIdentifier;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class AllCallFlowsWithinArtifactCallGraphStrategy implements CallGraphStrategy {
    private final List<FunctionIdentifier> dependentCalls;
    private final ArtifactIdentifier artifact;
    private boolean initialControlFlowsAdded;

    public AllCallFlowsWithinArtifactCallGraphStrategy(List<FunctionIdentifier> dependentCalls, ArtifactIdentifier artifact) {
        this.dependentCalls = dependentCalls;
        this.artifact = artifact;
        this.initialControlFlowsAdded = false;
    }

    @Override
    public CallPath determineNextCallPath(@Nullable CallPath currentCallPath, List<CallPath> callPaths) throws StrategyUnexpectedStateException {
        if (currentCallPath == null) {
            final List<CallPath> newCallPaths = mapToControlFLows(dependentCalls);
            callPaths.addAll(newCallPaths);
            initialControlFlowsAdded = true;
        }
        return callPaths.get(0);
    }

    private List<CallPath> mapToControlFLows(List<FunctionIdentifier> dependentCalls) {
        final List<CallPath> callPaths = dependentCalls.stream()
                .map(fid -> CallPathBuilder.aCallPath()
                        .containingFunctionIdentifiers(fid)
                        .build())
                .collect(Collectors.toList());
        return callPaths;
    }

    @Nullable
    @Override
    public ArtifactIdentifier getActiveArtifact() {
        return artifact;
    }

    @Override
    public boolean isFinished(List<CallPath> callPaths) {
        if (dependentCalls.isEmpty()) {
            return true;
        }
        return initialControlFlowsAdded && callPaths.isEmpty();
    }
}
