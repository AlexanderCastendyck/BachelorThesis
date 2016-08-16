package castendyck.analyzedcve;

import castendyck.certainty.Certainty;
import castendyck.cve.CVE;
import castendyck.cve.CveTestBuilder;
import castendyck.judgement.Judgement;
import castendyck.judgement.JudgementBuilder;
import castendyck.processedcve.ProcessedCve;
import castendyck.processedcve.ProcessedCveBuilder;
import castendyck.reduction.Reduction;
import castendyck.reduction.ReductionBuilder;
import castendyck.risklevel.RiskLevel;

public class AnalyzedCveTestBuilder {
    private String cveName;
    private Certainty certainty;
    private RiskLevel riskLevel;

    public static AnalyzedCveTestBuilder aAnalyzedCve() {
        return new AnalyzedCveTestBuilder();
    }

    public AnalyzedCveTestBuilder forCve(String cveName) {
        this.cveName = cveName;
        return this;
    }

    public AnalyzedCveTestBuilder withCertainty(Certainty certainty) {
        this.certainty = certainty;
        return this;
    }

    public AnalyzedCveTestBuilder withRisk(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
        return this;
    }

    public AnalyzedCve build(){
        final CVE cve = CveTestBuilder.aCve()
                .withName(cveName)
                .build();
        final ProcessedCve processedCve = ProcessedCveBuilder.aProcessedCve()
                .forCve(cve)
                .build();
        final Reduction reduction = ReductionBuilder.aReduction()
                .build();
        final Judgement judgement = JudgementBuilder.aJudgment()
                .forProcessedCve(processedCve)
                .basedOnReduction(reduction)
                .withCertainty(certainty)
                .withRiskLevel(riskLevel)
                .build();
        final AnalyzedCve analyzedCve = AnalyzedCveBuilder.anAnalyzedCve()
                .forAProcessedCve(processedCve)
                .withJudgement(judgement)
                .build();
        return analyzedCve;
    }
}
