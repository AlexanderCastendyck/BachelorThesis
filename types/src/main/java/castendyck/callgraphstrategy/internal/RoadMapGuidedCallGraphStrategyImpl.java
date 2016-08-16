package castendyck.callgraphstrategy.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.CallPath;
import castendyck.callgraph.CallPathBuilder;
import castendyck.callgraphstrategy.CallGraphStrategy;
import castendyck.callgraphstrategy.StrategyUnexpectedStateException;
import castendyck.roadmap.RoadMap;
import castendyck.roadmap.RoadSection;
import castendyck.vulnerablepoint.VulnerablePoint;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class RoadMapGuidedCallGraphStrategyImpl implements CallGraphStrategy {
    private final RoadMap roadMap;
    private ArtifactIdentifier currentArtifact;

    public RoadMapGuidedCallGraphStrategyImpl(RoadMap roadMap) {
        this.roadMap = roadMap;
    }

    @Override
    public CallPath determineNextCallPath(@Nullable CallPath currentCallPath, List<CallPath> callPaths) throws StrategyUnexpectedStateException {
        ensureNotFinished(callPaths);

        if (isFirstCallOfStrategy()) {
            loadNextSectionFromRoadMap(callPaths);
            return nextControlFLow(currentCallPath, callPaths);
        }

        final List<CallPath> controlFlowsActiveInTheCurrentArtifact = controlFlowsActiveInCurrentArtifact(callPaths);
        if (controlFlowsActiveInTheCurrentArtifact.size() > 0) {
            final CallPath nextControlFLow = controlFlowsActiveInTheCurrentArtifact.get(0);
            return nextControlFLow;
        } else {
            loadDataFromRoadMapUntilOneControlFlowBecomesActive(callPaths);
            final List<CallPath> possibleActiveCallPaths = controlFlowsActiveInCurrentArtifact(callPaths);
            return possibleActiveCallPaths.get(0);
        }
    }

    private void ensureNotFinished(List<CallPath> callPaths) throws StrategyUnexpectedStateException {
        if (isFinished(callPaths)) {
            throw new StrategyUnexpectedStateException("Called Strategy, that has already finished");
        }
    }

    private boolean isFirstCallOfStrategy() {
        return currentArtifact == null;
    }

    private void ensureDataIsLeft() throws StrategyUnexpectedStateException {
        if (!roadMap.hasNext()) {
            throw new StrategyUnexpectedStateException("Expected more data from RoadMap, but RoadMap was already depleted");
        }
    }

    private void loadNextSectionFromRoadMap(List<CallPath> callPaths) throws StrategyUnexpectedStateException {
        if (!roadMap.hasNext()) {
            throw new StrategyUnexpectedStateException("Expected more data from RoadMap, but RoadMap was already depleted");
        }

        final RoadSection roadSection = roadMap.getNext();
        currentArtifact = roadSection.getArtifactIdentifier();
        final List<VulnerablePoint> vulnerablePoints = roadSection.getVulnerablePoints();
        final List<CallPath> controlFlowsOfCurrentSection = vulnerablePoints.stream()
                .map(this::mapToCallPath)
                .collect(Collectors.toList());

        callPaths.addAll(controlFlowsOfCurrentSection);
    }

    private CallPath mapToCallPath(VulnerablePoint vulnerablePoint) {
        final CallPath callPath = CallPathBuilder.aCallPath()
                .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                .forVulnerablePoint(vulnerablePoint)
                .build();
        return callPath;
    }

    private CallPath nextControlFLow(@Nullable CallPath currentCallPath, List<CallPath> callPaths) {
        if (currentCallPath != null) {
            return currentCallPath;
        } else {
            return callPaths.get(0);
        }
    }

    private boolean controlFlowIsStillActiveInCurrentArtifact(@Nullable CallPath currentCallPath) {
        return currentCallPath != null && currentCallPath.getCurrentFunctionIdentifier().getArtifactIdentifier().equals(currentArtifact);
    }

    private List<CallPath> controlFlowsActiveInCurrentArtifact(List<CallPath> callPaths) {
        return callPaths.stream()
                .filter(this::controlFlowIsStillActiveInCurrentArtifact)
                .collect(Collectors.toList());
    }

    private void loadDataFromRoadMapUntilOneControlFlowBecomesActive(List<CallPath> callPaths) throws StrategyUnexpectedStateException {
        List<CallPath> possibleActiveCallPaths = controlFlowsActiveInCurrentArtifact(callPaths);
        while (possibleActiveCallPaths.size() == 0) {
            ensureDataIsLeft();
            loadNextSectionFromRoadMap(callPaths);
            possibleActiveCallPaths = controlFlowsActiveInCurrentArtifact(callPaths);
        }
    }

    @Override
    @Nullable
    public ArtifactIdentifier getActiveArtifact() {
        return currentArtifact;
    }

    @Override
    public boolean isFinished(List<CallPath> callPaths) {
        return callPaths.size() == 0 && !roadMap.hasNext();
    }
}
