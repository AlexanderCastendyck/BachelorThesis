package castendyck.reporting.result.internal;

import castendyck.analysisresult.AnalysisResult;
import castendyck.media.Media;
import castendyck.reporting.result.Result;

public class StandardResultImpl implements Result {
    private final AnalysisResult analysisResult;

    public StandardResultImpl(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }


    @Override
    public void print(Media media) {
        analysisResult.print(media);
    }
}
