package castendyck.analyzing;

import castendyck.analyzing.internal.AnalyzerImpl;

public class AnalyzerFactory {
    public static Analyzer newInstance(AnalyzerConfiguration configuration) throws AnalyzingException {
        return new AnalyzerImpl(configuration);
    }
}
