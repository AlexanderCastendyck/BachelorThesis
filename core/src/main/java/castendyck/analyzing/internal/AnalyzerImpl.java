package castendyck.analyzing.internal;

import castendyck.analysisrequest.AnalysisRequest;
import castendyck.analysisresult.AnalysisResult;
import castendyck.analysisresult.AnalysisResultBuilder;
import castendyck.analyzedcve.AnalyzedCve;
import castendyck.analyzedcve.AnalyzedCveBuilder;
import castendyck.analyzing.Analyzer;
import castendyck.analyzing.AnalyzerConfiguration;
import castendyck.analyzing.AnalyzingException;
import castendyck.analyzing.analyzerchain.AnalyzerChain;
import castendyck.analyzing.analyzerchainproviding.AnalyzerChainProvider;
import castendyck.analyzing.analyzerchainproviding.AnalyzerChainProviderFactory;
import castendyck.analyzing.analyzerchainproviding.source.Source;
import castendyck.analyzing.analyzerchainproviding.source.SourceConfiguration;
import castendyck.analyzing.analyzerchainproviding.source.SourceRegistryProvidingException;
import castendyck.analyzing.analyzerchainproviding.source.internal.HardCodedSourceImpl;
import castendyck.analyzing.processedcveconverting.ControlPathsToProcessedCveConverter;
import castendyck.callgraph.CallPath;
import castendyck.cve.CVE;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.judgement.Judgement;
import castendyck.maven.pomfile.PomFile;
import castendyck.maventestpathidentifier.MavenTestPathIdentifier;
import castendyck.processedcve.ProcessedCve;
import castendyck.repository.LocalRepository;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.nio.file.Path;
import java.util.List;

public class AnalyzerImpl implements Analyzer {
    private final AnalyzerChainProvider analyzerChainProvider;

    public AnalyzerImpl(AnalyzerConfiguration analyzerConfiguration) throws AnalyzingException {
        final Source source = createHardCodedSource(analyzerConfiguration);
        analyzerChainProvider = createAnalyzerChainProvider(source);
    }

    private AnalyzerChainProvider createAnalyzerChainProvider(Source source) throws AnalyzingException {
        try {
            return AnalyzerChainProviderFactory.newInstanceFromSource(source);
        } catch (SourceRegistryProvidingException e) {
            throw new AnalyzingException(e);
        }
    }

    private Source createHardCodedSource(AnalyzerConfiguration analyzerConfiguration) {
        final PomFile pomFile = analyzerConfiguration.getPomFile();
        final DependencyGraph dependencyGraph = analyzerConfiguration.getDependencyGraph();
        final LocalRepository localRepository = analyzerConfiguration.getLocalRepository();
        final Path projectRootPath = analyzerConfiguration.getProjectRootPath();
        final MavenTestPathIdentifier mavenTestPathIdentifier = analyzerConfiguration.getMavenTestPathIdentifier();


        final SourceConfiguration configuration = new SourceConfiguration(pomFile, dependencyGraph, localRepository, projectRootPath, mavenTestPathIdentifier);
        final HardCodedSourceImpl source = new HardCodedSourceImpl(configuration);
        return source;
    }

    @Override
    public AnalysisResult analyze(AnalysisRequest analysisRequest) {
        final List<ProcessedCve> processedCves = extractProcessedCves(analysisRequest);

        final AnalysisResultBuilder analysisResultBuilder = AnalysisResultBuilder.anAnalysisResult();
        for (ProcessedCve processedCve : processedCves) {
            final CVE cve = processedCve.getCve();
            final AnalyzerChain analyzerChain = analyzerChainProvider.providerAnalyzerChainFor(cve);
            final Judgement judgement = analyzerChain.traverse(processedCve);
            final AnalyzedCve analyzedCve = mapToAnalyzedCve(processedCve, judgement);

            analysisResultBuilder.withAnAnalyzedCve(analyzedCve);
        }

        final AnalysisResult analysisResult = analysisResultBuilder.build();
        return analysisResult;
    }

    private List<ProcessedCve> extractProcessedCves(AnalysisRequest analysisRequest) {
        final List<CVE> cves = analysisRequest.getCves();
        final List<VulnerablePoint> vulnerablePoints = analysisRequest.getVulnerablePoints();
        final List<CallPath> callPaths = analysisRequest.getCallPaths();
        return ControlPathsToProcessedCveConverter.process(cves, vulnerablePoints, callPaths);
    }

    private AnalyzedCve mapToAnalyzedCve(ProcessedCve processedCve, Judgement judgement) {
        final AnalyzedCve analyzedCve = AnalyzedCveBuilder.anAnalyzedCve()
                .forAProcessedCve(processedCve)
                .withJudgement(judgement)
                .build();
        return analyzedCve;
    }
}
