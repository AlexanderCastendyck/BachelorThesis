package castendyck.analyzing.analyzerchainproviding.source.internal;

import castendyck.analyzing.analyzerchainproviding.analyzerchainregistering.AnalyzerChainRegistry;
import castendyck.analyzing.analyzerchainproviding.source.Source;
import castendyck.analyzing.analyzerchainproviding.source.SourceConfiguration;
import castendyck.analyzing.analyzerchainproviding.source.SourceRegistryProvidingException;
import castendyck.analyzing.judging.JudgeConfigurationBuilder;
import castendyck.analyzing.judging.internal.JudgingBasesOnCallPathsLeftJudgeImpl;
import castendyck.analyzing.reducing.ReducerConfigurationBuilder;
import castendyck.analyzing.reducing.internal.OnlyInProductionUsedCodeReducerImpl;
import castendyck.analyzing.reducing.internal.SocketUsedPatternReducerImpl;
import castendyck.analyzing.reducing.internal.ToOnlyUsedCodeReducerImpl;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistryFactory;
import castendyck.cwe.CweType;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistryBuilder;
import castendyck.maven.pomfile.PomFile;
import castendyck.maventestpathidentifier.MavenTestPathIdentifier;
import castendyck.repository.LocalRepository;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class HardCodedSourceImpl implements Source {

    private final PomFile pomFile;
    private final DependencyGraph dependencyGraph;
    private final LocalRepository localRepository;
    private final DependencyRegistry dependencyRegistry;
    private final SourceCodeRegistry sourceCodeRegistry;
    private final Path projectRootPath;
    private final MavenTestPathIdentifier generalMavenTestPathPart;

    public HardCodedSourceImpl(SourceConfiguration configuration) {
        this.pomFile = configuration.getPomFile();
        this.dependencyGraph = configuration.getDependencyGraph();
        this.dependencyRegistry = DependencyRegistryBuilder.aDependencyRegistry()
                .initializedWithGraph(dependencyGraph)
                .build();
        this.localRepository = configuration.getLocalRepository();
        this.sourceCodeRegistry = SourceCodeRegistryFactory.newInstance(dependencyGraph, localRepository);
        this.projectRootPath = configuration.getProjectRootPath();
        this.generalMavenTestPathPart = configuration.getGeneralMavenTestPathPart();
    }

    @Override
    public AnalyzerChainRegistry provideRegistry() throws SourceRegistryProvidingException {
        try {
            final AnalyzerChainBuilder fullAnalyzingChain = AnalyzerChainBuilder.anAnalyzerChain()
                    .withReducer(ToOnlyUsedCodeReducerImpl.class)
                    .withReducer(OnlyInProductionUsedCodeReducerImpl.class)
                    .withReducer(SocketUsedPatternReducerImpl.class)
                    .withJudge(JudgingBasesOnCallPathsLeftJudgeImpl.class);

            final AnalyzerChainBuilder shortChain = AnalyzerChainBuilder.anAnalyzerChain()
                    .withReducer(ToOnlyUsedCodeReducerImpl.class)
                    .withReducer(OnlyInProductionUsedCodeReducerImpl.class)
                    .withJudge(JudgingBasesOnCallPathsLeftJudgeImpl.class);


            final AnalyzerChainRegistry analyzerChainRegistry = AnalyzerChainRegistryBuilder.anAnalyzerChainRegistry()
                    .registeredFor(CweType.CWE_20, fullAnalyzingChain)  //Improper Input Validation
                    .registeredFor(CweType.CWE_89, fullAnalyzingChain)  //SQL Injection
                    .registeredFor(CweType.CWE_94, fullAnalyzingChain) //code injection
                    .registeredFor(CweType.CWE_119, fullAnalyzingChain) //memory errors
                    .registeredFor(CweType.CWE_310, fullAnalyzingChain) // cryptographic issues
                    .registeredFor(CweType.CWE_399, fullAnalyzingChain) // resource management
                    .registeredFor(CweType.CWE_79, shortChain)          //XSS
                    .registeredFor(CweType.CWE_200, shortChain)         //information exposure
                    .registeredFor(CweType.CWE_352, shortChain)         //CSRF
                    .withDefaultChain(shortChain)
                    .configuredWith(ReducerConfigurationBuilder.aConfiguration()
                            .withPomFile(pomFile)
                            .withDependencyRegistry(dependencyRegistry)
                            .withSourceCodeRegistry(sourceCodeRegistry)
                            .withProjectRootPath(projectRootPath)
                            .withGeneralMavenTestPathPart(generalMavenTestPathPart))
                    .configuredWith(JudgeConfigurationBuilder.aJudgeConfiguration()
                            .withDependencyGraph(dependencyGraph)
                            .withLocalRepository(localRepository))
                    .build();
            return analyzerChainRegistry;
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw new SourceRegistryProvidingException(e);
        }
    }
}
