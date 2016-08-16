package castendyck.analyzing;

import castendyck.analysisrequest.AnalysisRequest;
import castendyck.analysisresult.AnalysisResult;
import castendyck.analysisresult.internal.AnalysisResultImpl;

public interface Analyzer {

    AnalysisResult analyze(AnalysisRequest analysisRequest);
}
