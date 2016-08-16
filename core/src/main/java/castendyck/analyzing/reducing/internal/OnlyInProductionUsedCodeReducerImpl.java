package castendyck.analyzing.reducing.internal;

import castendyck.analyzing.reducing.Reducer;
import castendyck.analyzing.reducing.ReducerConfiguration;
import castendyck.callgraph.CallPath;
import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;

import java.util.List;

import static castendyck.reduction.ReductionBuilder.aReduction;

public class OnlyInProductionUsedCodeReducerImpl implements Reducer {
    private final CallPathNeverTraversesAnArtifactWithTestScopeReducerImpl callPathNeverTraversesAnArtifactWithTestScopeReducer;
    private final NoClassesInMavenTestDirectoryReducerImpl noClassesInMavenTestDirectoryReducer;

    public OnlyInProductionUsedCodeReducerImpl(ReducerConfiguration reducerConfiguration) {
        this.callPathNeverTraversesAnArtifactWithTestScopeReducer = new CallPathNeverTraversesAnArtifactWithTestScopeReducerImpl(reducerConfiguration);
        this.noClassesInMavenTestDirectoryReducer = new NoClassesInMavenTestDirectoryReducerImpl(reducerConfiguration);
    }

    @Override
    public Reduction reduceFurther(Reduction previousReduction, ProcessedCve processedCve) {
        final Reduction intermediateReduction = callPathNeverTraversesAnArtifactWithTestScopeReducer.reduceFurther(previousReduction, processedCve);
        final Reduction finalReduction = noClassesInMavenTestDirectoryReducer.reduceFurther(intermediateReduction, processedCve);

        final String reason = getReason();
        final List<CallPath> reducedCallPaths = finalReduction.getRemainingCallPaths();
        final Reduction reduction = aReduction()
                .containing(reducedCallPaths)
                .withReason(reason)
                .build();
        return reduction;
    }

    @Override
    public String getReason() {
        return "vulnerability not used in production code";
    }
}
