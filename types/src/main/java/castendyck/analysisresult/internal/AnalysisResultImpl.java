package castendyck.analysisresult.internal;

import castendyck.analysisresult.AnalysisResult;
import castendyck.analyzedcve.AnalyzedCve;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.media.Media;

import java.util.List;

public class AnalysisResultImpl implements AnalysisResult {
    private final List<AnalyzedCve> analyzedCves;

    public AnalysisResultImpl(List<AnalyzedCve> analyzedCves) {
        NotNullConstraintEnforcer.ensureNotNull(analyzedCves);
        this.analyzedCves = analyzedCves;
    }

    public List<AnalyzedCve> getAnalyzedCves() {
        return analyzedCves;
    }

    @Override
    public void print(Media media) {
        media.starting("header")
                .withText("Analysis finished: ")
                .withNumber(analyzedCves.size())
                .withText(" CVEs checked.")
                .ending("header");
        for (AnalyzedCve analyzedCve : analyzedCves) {
            media.starting("analyzedCve");
            analyzedCve.print(media);
            media.ending("analyzedCve");
        }
    }
}
