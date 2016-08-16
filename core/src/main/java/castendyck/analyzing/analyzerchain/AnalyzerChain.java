package castendyck.analyzing.analyzerchain;

import castendyck.judgement.Judgement;
import castendyck.processedcve.ProcessedCve;

public interface AnalyzerChain {
     Judgement traverse(ProcessedCve processedCve);
}
