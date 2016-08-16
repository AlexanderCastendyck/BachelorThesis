package castendyck.analyzing.judging;

import castendyck.analyzing.judging.internal.JudgingBasesOnCallPathsLeftJudgeImpl;
import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.callgraph.CallPathBuilder;
import castendyck.certainty.Certainty;
import castendyck.cpe.CPE;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import castendyck.judgement.Judgement;
import castendyck.judgement.JudgementBuilder;
import castendyck.localrepository.LocalRepositoryBuilder;
import castendyck.processedcve.ProcessedCve;
import castendyck.processedcve.ProcessedCveBuilder;
import castendyck.reduction.Reduction;
import castendyck.reduction.ReductionBuilder;
import castendyck.repository.LocalRepository;
import castendyck.risklevel.RiskLevel;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.io.IOException;

import static castendyck.cve.CveTestBuilder.aCve;
import static castendyck.dependencygraphing.DependencyGraphTestBuilder.aDependencyGraph;
import static castendyck.dependencygraphing.GraphNodeTestBuilder.aGraphNode;
import static castendyck.judgement.JudgementBuilder.aJudgment;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class JudgeTestIT {

    @Test
    public void makeJudgement_notUsedDependency_getsAssessedAsNoRisk() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "someArtifact", "1.0");
        final CPE cpeForArtifact = CPE.createNew("cpe:/a:someGroup:someArtifact:1.0");

        final DependencyGraph dependencyGraph = aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(artifactIdentifier))
                .build();

        final ProcessedCve processedCve = processedCveFor(cpeForArtifact);
        final Reduction reduction = aReductionWithNoCallPaths();
        final Judge judge = aJudgeConfiguredWith(dependencyGraph);

        final Judgement judgement = act(processedCve, reduction, judge);

        assertThat(judgement, is(aJudgment()
                .basedOnReduction(reduction)
                .forProcessedCve(processedCve)
                .withCertainty(Certainty.CERTAIN)
                .withRiskLevel(RiskLevel.NONE)));
    }

    private Judgement act(ProcessedCve processedCve, Reduction reduction, Judge judge) {
        return judge.makeJudgement(reduction, processedCve);
    }

    private Reduction aReductionWithNoCallPaths() {
        return ReductionBuilder.aReduction()
                .build();
    }

    @Test
    public void makeJudgement_notALibrary_getsAssessedAsRisk() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "someArtifact", "1.0");
        final CPE cpeForArtifact = CPE.createNew("cpe:/a:not:AssociatedCpe:1.0");

        final DependencyGraph dependencyGraph = aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(artifactIdentifier))
                .build();

        final ProcessedCve processedCve = processedCveFor(cpeForArtifact);
        final Reduction reduction = aReductionWithNoCallPaths();
        final Judge judge = aJudgeConfiguredWith(dependencyGraph);

        final Judgement judgement = act(processedCve, reduction, judge);

        assertThat(judgement, is(aJudgment()
                .basedOnReduction(reduction)
                .forProcessedCve(processedCve)
                .withCertainty(Certainty.UNSURE)
                .withRiskLevel(RiskLevel.HIGH)));

    }

    @Test
    public void makeJudgement_usedVulnerability_getsAssessedAsRisk() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "someArtifact", "1.0");
        final CPE cpeForArtifact = CPE.createNew("cpe:/a:not:AssociatedCpe:1.0");

        final DependencyGraph dependencyGraph = aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(artifactIdentifier))
                .build();

        final ProcessedCve processedCve = processedCveFor(cpeForArtifact);
        final Reduction reduction = aReductionWithCallPathsEndingIn(artifactIdentifier);
        final Judge judge = aJudgeConfiguredWith(dependencyGraph);

        final Judgement judgement = act(processedCve, reduction, judge);

        assertThat(judgement, is(aJudgment()
                .basedOnReduction(reduction)
                .forProcessedCve(processedCve)
                .withCertainty(Certainty.UNSURE)
                .withRiskLevel(RiskLevel.HIGH)));

    }

    private Reduction aReductionWithCallPathsEndingIn(ArtifactIdentifier artifactIdentifier) {
        return ReductionBuilder.aReduction()
                .containing(CallPathBuilder.aCallPath()
                        .containingFunctionIdentifier(FunctionIdentifierBuilder.aFunctionIdentifier()
                                .withArtifactIdentifier(artifactIdentifier)
                                .forClass("someClass.class")
                                .forFunction("function")
                                .build())
                        .build())
                .build();
    }

    private ProcessedCve processedCveFor(CPE cpeForArtifact) {
        return ProcessedCveBuilder.aProcessedCve()
                .forCve(aCve()
                        .witCpe(cpeForArtifact)
                        .build())
                .build();
    }

    private JudgingBasesOnCallPathsLeftJudgeImpl aJudgeConfiguredWith(DependencyGraph dependencyGraph) throws IOException {
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .locatedAt("target/JudgeTests/")
                .build();
        final JudgeConfiguration judgeConfiguration = new JudgeConfiguration(dependencyGraph, localRepository);
        return new JudgingBasesOnCallPathsLeftJudgeImpl(judgeConfiguration);
    }

    private Matcher<Judgement> is(JudgementBuilder judgementBuilder) {
        return equalTo(judgementBuilder.build());
    }
}
