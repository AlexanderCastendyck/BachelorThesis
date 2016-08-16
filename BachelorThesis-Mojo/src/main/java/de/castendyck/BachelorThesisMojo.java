package de.castendyck;

import castendyck.analysisrequest.AnalysisRequest;
import castendyck.analysisresult.AnalysisResult;
import castendyck.analyzing.Analyzer;
import castendyck.analyzing.AnalyzerConfiguration;
import castendyck.analyzing.AnalyzerFactory;
import castendyck.analyzing.AnalyzingException;
import castendyck.callgraph.*;
import castendyck.callgraphstrategy.CallGraphStrategy;
import castendyck.callgraphstrategy.CallGraphStrategyCalculatorFactory;
import castendyck.callgraphstrategy.CallGraphStrategyRequestDto;
import castendyck.callgraphstrategy.internal.CallGraphStrategyCalculatorImpl;
import de.castendyck.collections.ObjectListToStringCollector;
import castendyck.cve.CVE;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.DependencyGraphProvider;
import castendyck.dependencygraphing.DependencyGraphProviderConfiguration;
import castendyck.dependencygraphing.DependencyGraphProviderFactory;
import castendyck.dependencygraphing.DependencyGraphProvidingException;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileCreationException;
import castendyck.maven.pomfile.PomFileFactory;
import castendyck.maventestpathidentifier.MavenTestPathIdentifier;
import castendyck.maventestpathidentifier.MavenTestPathIdentifierFactory;
import castendyck.pomfilelocator.PomFileLocationException;
import castendyck.reporting.ExceptionResultBuilder;
import castendyck.reporting.Reporter;
import castendyck.reporting.ReporterFactory;
import castendyck.reporting.StandardResultBuilder;
import castendyck.reporting.result.Result;
import castendyck.repository.LocalRepository;
import castendyck.repository.LocalRepositoryFactory;
import castendyck.settings.Settings;
import castendyck.sourcecode.localrepositorywithsourcecode.LocalRepositoryWithSourceCodeCreator;
import castendyck.sourcecode.localrepositorywithsourcecode.LocalRepositoryWithSourceCodeCreatorFactory;
import castendyck.vulnerablepoint.*;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static castendyck.settings.InitialSettingsBuilder.Settings;

@Mojo(name = "check",
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
        requiresOnline = true,
        defaultPhase = LifecyclePhase.VERIFY)
public class BachelorThesisMojo extends AbstractMojo {

    /**
     * The repository system maven uses
     */
    @Component
    private RepositorySystem repoSystem;

    /**
     * The current repository/network configuration of Maven.
     */
    @Parameter(readonly = true, required = true, defaultValue = "${repositorySystemSession}")
    private RepositorySystemSession repoSession;

    /**
     * The project's remote repositories to use for the resolution of project dependencies.
     */
    @Parameter(readonly = true, required = true, defaultValue = "${project.remoteProjectRepositories}")
    private List<RemoteRepository> projectRepos;

    /**
     * The project as defined in the pom
     */
    @Parameter(property = "project", required = true, readonly = true)
    private MavenProject project;

    /**
     * In case of a multimodule project, all modules are stored in the reactor
     */
    @Parameter(property = "reactorProjects", readonly = true, required = true)
    private List<MavenProject> reactorProjects;

    /**
     * Path to the local maven repository
     */
    @Parameter(property = "localRepo")
    private String localRepositoryPath;

    /**
     * Path to file containing suppression information for owasp tool
     */
    @Parameter(property = "suppressionFile", defaultValue = "cve_ignore.xml")
    private String suppressionFile;

    /**
     * part of path, that identifies a directory to contain test files
     */
    @Parameter(property = "mavenTestPathIdentifier", defaultValue = "src/test/java/")
    private String mavenTestPathIdentifier;


    public void execute() throws MojoExecutionException {
        try {
            System.out.println("RepoSystem: " + repoSystem);
            System.out.println("RepoSession: " + repoSession);
            System.out.println("Repos: " + projectRepos.stream().collect(ObjectListToStringCollector.collectToString()));
            System.out.println("Artifacts: " + project.getArtifacts().stream().collect(ObjectListToStringCollector.collectToString()));


            final Settings settings = loadSettings();

            final DependencyGraph dependencyGraph = calculateDependencyGraph(settings);
            final CollectedVulnerablePoints collectedVulnerablePoints = collectVulnerablePoints(dependencyGraph, settings);
            final List<CallPath> callPaths = determineCallPaths(dependencyGraph, collectedVulnerablePoints, settings);
            final AnalysisResult analysisResult = analyzeCallPaths(dependencyGraph, collectedVulnerablePoints, callPaths, settings);
            report(analysisResult);
        } catch (Exception e) {
            reportError(e);
            throw new MojoExecutionException("Build failed, because " + e.getClass() + " was throw." + e);
        }
    }

    private Settings loadSettings() throws PomFileCreationException, MojoExecutionException, IOException, PomFileLocationException {
        final File projectPomFile = project.getFile();
        final PomFile pomFile = PomFileFactory.createFromFile(projectPomFile);
        final LocalRepository localRepository = localRepositoryFor(localRepositoryPath, pomFile);
        final MavenTestPathIdentifier generalMavenTestPathIdentifier = MavenTestPathIdentifierFactory.createNew(mavenTestPathIdentifier);
        final Path suppressionFile = Paths.get(this.suppressionFile);
        final Path ProjectRootPath = projectPomFile.toPath().getParent();

        final Settings settings = Settings()
                .forMavenProject(project)
                .withReactorProjects(reactorProjects)
                .withRepositories(projectRepos)
                .withRepositorySystemSession(repoSession)
                .withRepositorySystem(repoSystem)
                .withPomFile(pomFile)
                .withLocalRepository(localRepository)
                .withProjectRootPath(ProjectRootPath)
                .withMavenTestPathIdentifier(generalMavenTestPathIdentifier)
                .withSuppressionFile(suppressionFile)
                .build();
        return settings;
    }

    private LocalRepository localRepositoryFor(String localRepositoryPath, PomFile pomFile) throws MojoExecutionException, IOException, PomFileLocationException, PomFileCreationException {
        final LocalRepository mavenLocalRepository = createMavenLocalRepository(localRepositoryPath);
        final LocalRepositoryWithSourceCodeCreator localRepositoryWithSourceCodeCreator = LocalRepositoryWithSourceCodeCreatorFactory.newInstance();
        final LocalRepository localRepository = localRepositoryWithSourceCodeCreator.extendLocalRepositoryWithSourceCode(mavenLocalRepository, pomFile);
        return localRepository;
    }

    private LocalRepository createMavenLocalRepository(String localRepositoryPath) throws MojoExecutionException {
        if (localRepositoryPath == null || localRepositoryPath.equals("")) {
            return LocalRepositoryFactory.createNewInStandardMavenLocation();
        } else {
            final Path path = Paths.get(localRepositoryPath);
            if (!path.isAbsolute()) {
                throw new MojoExecutionException("Path for configuration parameter localRepo has to be absolute");
            }
            return LocalRepositoryFactory.createNewInPath(path);
        }
    }

    private DependencyGraph calculateDependencyGraph(Settings settings) throws DependencyGraphProvidingException {
        final List<RemoteRepository> projectRepositories = settings.getRemoteRepositories();
        final RepositorySystem repositorySystem = settings.getRepositorySystem();
        final RepositorySystemSession repositorySystemSession = settings.getRepositorySystemSession();
        final PomFile parentPom = settings.getParentPom();
        final DependencyGraphProviderConfiguration configuration = new DependencyGraphProviderConfiguration(projectRepositories, repositorySystem, repositorySystemSession, parentPom);
        final DependencyGraphProvider dependencyGraphProvider = DependencyGraphProviderFactory.newInstance(configuration);

        final PomFile pomFile = settings.getParentPom();
        return dependencyGraphProvider.provideDependencyGraphFor(pomFile);
    }

    private CollectedVulnerablePoints collectVulnerablePoints(DependencyGraph dependencyGraph, Settings settings) throws VulnerablePointsCollectingException {
        final LocalRepository localRepository = settings.getLocalRepository();
        final boolean owaspSkipTestScope = settings.getOwaspSkipTestScope();
        final Path suppressionFile = settings.getSuppressionFile();
        final VulnerablePointsProviderConfiguration configuration = new VulnerablePointsProviderConfiguration(localRepository, owaspSkipTestScope, suppressionFile);
        final VulnerablePointsProvider vulnerablePointsProvider = VulnerablePointsProviderFactory.newInstance(configuration);

        final MavenProject project = settings.getProject();
        final List<MavenProject> reactorProjects = settings.getReactorProjects();
        final PomFile pomFile = settings.getParentPom();
        final VulnerablePointsRequestDto requestDto = new VulnerablePointsRequestDto(project, reactorProjects, pomFile, dependencyGraph);

        final CollectedVulnerablePoints collectedVulnerablePoints = vulnerablePointsProvider.collectVulnerablePoints(requestDto);
        return collectedVulnerablePoints;
    }

    private AnalysisResult analyzeCallPaths(DependencyGraph dependencyGraph, CollectedVulnerablePoints collectedVulnerablePoints, List<CallPath> callPaths, Settings settings) throws AnalyzingException {
        final PomFile pomFile = settings.getParentPom();
        final LocalRepository localRepository = settings.getLocalRepository();
        final Path projectRootPath = settings.getProjectRootPath();
        final MavenTestPathIdentifier mavenTestPathIdentifier = settings.getGeneralMavenTestPathIdentifier();
        final AnalyzerConfiguration configuration = new AnalyzerConfiguration(pomFile, dependencyGraph, localRepository, projectRootPath, mavenTestPathIdentifier);
        final Analyzer analyzer = AnalyzerFactory.newInstance(configuration);

        final List<CVE> cves = collectedVulnerablePoints.getCves();
        final List<VulnerablePoint> vulnerablePoints = collectedVulnerablePoints.getVulnerablePoints();
        final AnalysisRequest analysisRequest = new AnalysisRequest(cves, vulnerablePoints, callPaths);
        return analyzer.analyze(analysisRequest);
    }

    private List<CallPath> determineCallPaths(DependencyGraph dependencyGraph, CollectedVulnerablePoints collectedVulnerablePoints, Settings settings) throws CallGraphCollectingException {
        final LocalRepository localRepository = settings.getLocalRepository();
        final CallGraphCollectorConfiguration callGraphCollectorConfiguration = new CallGraphCollectorConfiguration(localRepository);
        final CallGraphCollector callGraphCollector = CallGraphCollectorFactory.newInstance(callGraphCollectorConfiguration);
        final CallGraphStrategyCalculatorImpl callGraphStrategyCalculator = CallGraphStrategyCalculatorFactory.newInstance();
        final CallGraphStrategyRequestDto callGraphStrategyRequestDto = new CallGraphStrategyRequestDto(dependencyGraph, collectedVulnerablePoints.getVulnerablePoints());
        final CallGraphStrategy strategy = callGraphStrategyCalculator.calculateStrategy(callGraphStrategyRequestDto);
        final CallGraphCollectRequestDto callGraphCollectRequestDto = new CallGraphCollectRequestDto(strategy, dependencyGraph);
        return callGraphCollector.collectCallGraph(callGraphCollectRequestDto);
    }

    private void report(AnalysisResult analysisResult) {
        final Log log = getLog();
        final Reporter reporter = ReporterFactory.newInstanceForMavenConsole(log);
        final Result result = StandardResultBuilder.aResult()
                .forAnalysisResult(analysisResult)
                .build();
        reporter.report(result);
    }

    private void reportError(Exception e) {
        final Log log = getLog();
        final Reporter reporter = ReporterFactory.newInstanceForMavenConsole(log);
        final Result result = ExceptionResultBuilder.anErroneousResult()
                .forError(e)
                .build();
        reporter.report(result);
    }
}