package castendyck.analyzing.judging.internal;

import castendyck.analyzing.judging.Judge;
import castendyck.analyzing.judging.JudgeConfiguration;
import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.CallPath;
import castendyck.certainty.Certainty;
import castendyck.cpe.CPE;
import castendyck.cpes.cpetoartifactmatching.CpeToArtifactMatcher;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.artifactsindgraphcollecting.ArtifactsInDependencyGraphCollector;
import castendyck.dependencygraphing.artifactsindgraphcollecting.ArtifactsInDependencyGraphCollectorFactory;
import castendyck.judgement.Judgement;
import castendyck.judgement.JudgementBuilder;
import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;
import castendyck.repository.LocalRepository;
import castendyck.risklevel.RiskLevel;
import org.apache.log4j.Logger;

import java.util.List;


public class JudgingBasesOnCallPathsLeftJudgeImpl implements Judge {
    private final ArtifactsInDependencyGraphCollector artifactsInDependencyGraphCollector;
    private final DependencyGraph dependencyGraph;
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final LocalRepository localRepository;

    public JudgingBasesOnCallPathsLeftJudgeImpl(JudgeConfiguration judgeConfiguration) {
        this.dependencyGraph = judgeConfiguration.getDependencyGraph();
        this.localRepository = judgeConfiguration.getLocalRepository();
        this.artifactsInDependencyGraphCollector = ArtifactsInDependencyGraphCollectorFactory.newInstance();
    }

    @Override
    public Judgement makeJudgement(Reduction reduction, ProcessedCve processedCve) {
        final List<CallPath> remainingCallPaths = reduction.getRemainingCallPaths();
        final JudgementBuilder judgementBuilder;

        if (!isLibrary(processedCve)) {
            judgementBuilder = JudgementBuilder.aJudgment()
                    .withCertainty(Certainty.UNSURE)
                    .withRiskLevel(RiskLevel.HIGH);
        } else if (remainingCallPaths.isEmpty()) {
            judgementBuilder = JudgementBuilder.aJudgment()
                    .withCertainty(Certainty.CERTAIN)
                    .withRiskLevel(RiskLevel.NONE);
        } else {
            judgementBuilder = JudgementBuilder.aJudgment()
                    .withCertainty(Certainty.UNSURE)
                    .withRiskLevel(RiskLevel.HIGH);
        }

        final Judgement judgement = judgementBuilder
                .forProcessedCve(processedCve)
                .basedOnReduction(reduction)
                .build();
        return judgement;
    }

    private boolean isLibrary(ProcessedCve processedCve) {
        final List<CPE> cpes = processedCve.getCve().getCpes();

        final List<ArtifactIdentifier> usedVulnerableArtifacts = artifactsInDependencyGraphCollector.collectArtifactsThatMatch(dependencyGraph, artifactIdentifier -> {
            for (CPE cpe : cpes) {
                if (CpeToArtifactMatcher.matches(cpe, artifactIdentifier)) {
                    return true;
                }
            }
            return false;
        });

        final boolean isLibrary = usedVulnerableArtifacts.size() >= 1;
        return isLibrary;
    }

}
