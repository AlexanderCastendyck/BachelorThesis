package castendyck.analyzing.judging;

import castendyck.judgement.Judgement;
import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;

public interface Judge {

    Judgement makeJudgement(Reduction reduction, ProcessedCve processedCve);

}
