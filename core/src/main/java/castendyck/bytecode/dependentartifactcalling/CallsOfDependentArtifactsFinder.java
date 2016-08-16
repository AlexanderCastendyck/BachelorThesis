package castendyck.bytecode.dependentartifactcalling;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.functionidentifier.FunctionIdentifier;

import java.util.List;

public interface CallsOfDependentArtifactsFinder {

    List<FunctionIdentifier> findCallsOfDependentArtifactsCalling(ArtifactIdentifier artifactIdentifier);
}
