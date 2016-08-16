package castendyck.reporting;

import castendyck.analysisresult.AnalysisResult;
import castendyck.analysisresult.internal.AnalysisResultImpl;
import castendyck.reporting.result.Result;
import castendyck.reporting.result.internal.StandardResultImpl;

public class StandardResultBuilder {
    private AnalysisResult analysisResult;

    public static StandardResultBuilder aResult() {
        return new StandardResultBuilder();
    }

    public StandardResultBuilder forAnalysisResult(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
        return this;
    }

    public Result build() {
        return new StandardResultImpl(analysisResult);
    }
}
