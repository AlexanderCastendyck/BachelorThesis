package castendyck.analysisresult;

import castendyck.analysisresult.internal.AnalysisResultImpl;
import castendyck.analyzedcve.AnalyzedCve;
import castendyck.processedcve.ProcessedCve;

import java.util.ArrayList;
import java.util.List;

public class AnalysisResultBuilder {
    private final List<AnalyzedCve> analyzedCves;

    public AnalysisResultBuilder() {
        this.analyzedCves = new ArrayList<>();
    }

    public static AnalysisResultBuilder anAnalysisResult() {
        return new AnalysisResultBuilder();
    }

    public AnalysisResultBuilder withAnalyzedCves(List<AnalyzedCve> analyzedCves) {
        this.analyzedCves.addAll(analyzedCves);
        return this;
    }

    public AnalysisResultBuilder withAnAnalyzedCve(AnalyzedCve analyzedCve) {
        this.analyzedCves.add(analyzedCve);
        return this;
    }

    public AnalysisResult build() {
        return new AnalysisResultImpl(analyzedCves);
    }
}
