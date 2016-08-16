package castendyck.bytecode.dependentartifactcalling.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.bytecode.dependentartifactcalling.CallsOfDependentArtifactsFinder;
import castendyck.reversecalllist.ReverseCallList;
import castendyck.bytecode.reversecalllist.ReverseCallListCreator;
import castendyck.functionidentifier.FunctionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class CallsOfDependentArtifactsFinderImpl implements CallsOfDependentArtifactsFinder {
    private final ReverseCallListCreator reverseCallListCreator;

    public CallsOfDependentArtifactsFinderImpl(ReverseCallListCreator reverseCallListCreator) {
        this.reverseCallListCreator = reverseCallListCreator;
    }

    @Override
    public List<FunctionIdentifier> findCallsOfDependentArtifactsCalling(ArtifactIdentifier artifactIdentifier) {
        final ReverseCallList reverseCallList = reverseCallListCreator.createForArtifact(artifactIdentifier);
        final Set<FunctionIdentifier> calledFunctions = reverseCallList.getAllCalledFunctions();

        final List<FunctionIdentifier> dependentCalls = new ArrayList<>();
        for (FunctionIdentifier calledFunction : calledFunctions) {
            final List<FunctionIdentifier> functionsCalling = reverseCallList.getFunctionsCalling(calledFunction);

            functionsCalling.stream()
                    .filter(isCallFromDependentArtifact(artifactIdentifier))
                    .forEach(dependentCalls::add);
        }
        return dependentCalls;
    }

    private Predicate<FunctionIdentifier> isCallFromDependentArtifact(ArtifactIdentifier artifactIdentifier) {
        return fid -> !fid.getArtifactIdentifier().equals(artifactIdentifier);
    }
}
