package castendyck.judgement;

import castendyck.certainty.Certainty;
import castendyck.judgement.internal.JudgementImpl;
import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;
import castendyck.risklevel.RiskLevel;

public class JudgementBuilder {
    private Certainty certainty;
    private RiskLevel riskLevel;
    private ProcessedCve processedCve;
    private Reduction reduction;

    public static JudgementBuilder aJudgment(){
        return new JudgementBuilder();
    }

    public JudgementBuilder withCertainty(Certainty certainty){
        this.certainty = certainty;
        return this;
    }
    public JudgementBuilder withRiskLevel(RiskLevel riskLevel){
        this.riskLevel = riskLevel;
        return this;
    }
    public JudgementBuilder forProcessedCve(ProcessedCve processedCve){
        this.processedCve = processedCve;
        return this;
    }

    public JudgementBuilder basedOnReduction(Reduction reduction){
        this.reduction = reduction;
        return this;
    }

    public Judgement build(){
        final Judgement judgement = new JudgementImpl(certainty, riskLevel, processedCve, reduction);
        return judgement;
    }
}
