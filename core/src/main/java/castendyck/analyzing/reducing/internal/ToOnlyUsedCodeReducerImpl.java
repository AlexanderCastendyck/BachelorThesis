package castendyck.analyzing.reducing.internal;

import castendyck.analyzing.reducing.Reducer;
import castendyck.analyzing.reducing.ReducerConfiguration;
import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.CallPath;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.maven.pomfile.PomFile;
import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;

import java.util.List;
import java.util.stream.Collectors;

import static castendyck.analyzing.reducing.internal.ArtifactsOfProjectFinder.getAllArtifactsOfProject;
import static castendyck.reduction.ReductionBuilder.aReduction;

public class ToOnlyUsedCodeReducerImpl implements Reducer {
    private final List<ArtifactIdentifier> artifactsOfProject;

    public ToOnlyUsedCodeReducerImpl(ReducerConfiguration configuration) {
        final PomFile pomFile = configuration.getPomFile();
        this.artifactsOfProject = getAllArtifactsOfProject(pomFile);
    }

    @Override
    public Reduction reduceFurther(Reduction previousReduction, ProcessedCve processedCve) {
        final List<CallPath> remainingCallPaths = previousReduction.getRemainingCallPaths();
        final List<CallPath> reducedCallPaths = remainingCallPaths.stream()
                .filter(this::endsInAnArtifactOfProject)
                .collect(Collectors.toList());

        final String reason = getReason();
        final Reduction reduction = aReduction()
                .containing(reducedCallPaths)
                .withReason(reason)
                .build();
        return reduction;
    }

    private boolean endsInAnArtifactOfProject(CallPath callPath) {
        final FunctionIdentifier lastFunctionIdentifier = callPath.getCurrentFunctionIdentifier();
        final ArtifactIdentifier artifactWhereCallPathEnds = lastFunctionIdentifier.getArtifactIdentifier();
        final boolean endsInProject = artifactsOfProject.contains(artifactWhereCallPathEnds);
        return endsInProject;
    }

    @Override
    public String getReason() {
        return "vulnerability not used";
    }
}
