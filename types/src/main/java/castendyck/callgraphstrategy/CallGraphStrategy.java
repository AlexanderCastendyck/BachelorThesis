package castendyck.callgraphstrategy;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.CallPath;

import javax.annotation.Nullable;
import java.util.List;

public interface CallGraphStrategy {
    CallPath determineNextCallPath(@Nullable CallPath currentCallPath, List<CallPath> callPaths) throws StrategyUnexpectedStateException;

    @Nullable
    ArtifactIdentifier getActiveArtifact();

    boolean isFinished(List<CallPath> callPaths);
}
