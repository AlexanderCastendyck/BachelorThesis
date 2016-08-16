package castendyck.analysisresult;


import castendyck.analyzedcve.AnalyzedCve;
import castendyck.printable.Printable;

import java.util.List;

public interface AnalysisResult extends Printable {
    List<AnalyzedCve> getAnalyzedCves();
}
