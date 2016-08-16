package castendyck.analyzing;

public class Given {
    public static When given(AnalyzerTestExamples.AnalyzerTestExample analyzerTestExample) {
        return new When(analyzerTestExample);
    }
}
