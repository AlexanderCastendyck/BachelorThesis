package castendyck.analyzing;

import castendyck.analysisrequest.AnalysisRequest;
import castendyck.analysisresult.AnalysisResult;
import castendyck.callgraph.CallPath;
import castendyck.cve.CVE;

import java.util.ArrayList;
import java.util.List;

public class When {
    private final AnalyzerTestExamples.AnalyzerTestExample example;

    public When(AnalyzerTestExamples.AnalyzerTestExample example) {
        this.example = example;
    }

    public Then whenAnalyzed() throws AnalyzingException {
        final AnalyzerConfiguration configuration = example.getConfiguration();
        final Analyzer analyzer = AnalyzerFactory.newInstance(configuration);
        final List<CallPath> callPaths = example.getCallPaths();
        final List<CVE> cves = example.getCves();

        
        final AnalysisRequest analysisRequest = new AnalysisRequest(cves, new ArrayList<>(), callPaths);
        final AnalysisResult analysisResult = analyzer.analyze(analysisRequest);
        return new Then(analysisResult);
    }
}
