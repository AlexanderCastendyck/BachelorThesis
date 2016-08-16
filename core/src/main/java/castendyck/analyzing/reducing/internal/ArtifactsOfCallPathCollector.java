package castendyck.analyzing.reducing.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.CallPath;
import castendyck.functionidentifier.FunctionIdentifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArtifactsOfCallPathCollector {
    public static List<ArtifactIdentifier> findArtifactsTraversedBy(CallPath callPath) {
        final List<FunctionIdentifier> functions = callPath.getFunctions();

        final Set<ArtifactIdentifier> traversedArtifacts = new HashSet<>();
        for (FunctionIdentifier function : functions) {
            final ArtifactIdentifier artifactIdentifier = function.getArtifactIdentifier();
            traversedArtifacts.add(artifactIdentifier);
        }

        final ArrayList<ArtifactIdentifier> traversedArtifactsAsList = new ArrayList<>(traversedArtifacts);
        return traversedArtifactsAsList;
    }
}
