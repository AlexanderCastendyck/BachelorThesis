package castendyck.analyzing;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.callgraph.CallPath;
import castendyck.callgraph.CallPathBuilder;
import castendyck.cve.CVE;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.DependencyGraphTestBuilder;
import castendyck.dependencygraphing.GraphNodeTestBuilder;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.localrepository.LocalRepositoryBuilder;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileFactory;
import castendyck.maventestpathidentifier.MavenTestPathIdentifier;
import castendyck.repository.LocalRepository;
import castendyck.temporarypomfile.TemporaryPomFileBuilder;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static castendyck.functionidentifier.FunctionIdentifierBuilder.aFunctionIdentifier;
import static castendyck.vulnerablepoint.VulnerablePointBuilder.*;
import static castendyck.examplecves.ExampleCve.cveForCVE_2013_22541;

public class AnalyzerTestExamples {
    public static AnalyzerTestExample noCallPaths() throws Exception {
        final ArtifactIdentifier artifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");

        final PomFile pomFile = PomFileFactory.createFromFile(TemporaryPomFileBuilder.aPomFile()
                .forArtifact(artifact)
                .build());
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder
                .aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(artifact))
                .build();
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .locatedAt("target/AnalyzerTestExamples/noCallPaths/")
                .withEmptyJarFor(artifact)
                .build();
        final Path projectRootPath = Paths.get("target/");
        final MavenTestPathIdentifier mavenTestPathIdentifier = MavenTestPathIdentifier.parse("src/test/java/");
        final AnalyzerConfiguration configuration = new AnalyzerConfiguration(pomFile, dependencyGraph, localRepository, projectRootPath, mavenTestPathIdentifier);
        final List<CallPath> callPaths = Collections.emptyList();
        final List<CVE> cves = Collections.emptyList();
        return new AnalyzerTestExample(configuration, callPaths, cves);
    }

    public static AnalyzerTestExample callPathsNotUsed() throws Exception {
        final ArtifactIdentifier artifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");

        final PomFile pomFile = PomFileFactory.createFromFile(TemporaryPomFileBuilder.aPomFile()
                .forArtifact(artifact)
                .build());
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder
                .aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(artifact))
                .build();
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .locatedAt("target/AnalyzerTestExamples/callPathsNotUsed/")
                .withEmptyJarFor(artifact)
                .build();
        final Path projectRootPath = Paths.get("target/");
        final MavenTestPathIdentifier mavenTestPathIdentifier = MavenTestPathIdentifier.parse("src/test/java/");
        final AnalyzerConfiguration configuration = new AnalyzerConfiguration(pomFile, dependencyGraph, localRepository, projectRootPath, mavenTestPathIdentifier);

        final VulnerablePoint vulnerablePoint = vulnerablePointFor(cveForCVE_2013_22541());
        final List<CallPath> callPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(aFunctionIdentifier()
                                .withArtifactIdentifier(ArtifactIdentifierBuilder.buildShort("local", "notUsed", "1.0"))
                                .forClass("someClass.class")
                                .forFunction("function")
                                .build())
                        .build()
        );
        final List<CVE> cves = Collections.singletonList(cveForCVE_2013_22541());
        return new AnalyzerTestExample(configuration, callPaths, cves);
    }

    private static VulnerablePoint vulnerablePointFor(CVE cve) {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("someGroup", "someArtifact", "1.0");
        final FunctionIdentifier functionIdentifier = aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass("soemClass.class")
                .forFunction("function")
                .build();
        final VulnerablePoint vulnerablePoint = aVulnerablePoint()
                .forCve(cve)
                .withFunctionIdentifier(functionIdentifier)
                .build();
        return vulnerablePoint;
    }

    public static AnalyzerTestExample callPathsNotUsedInProduction() throws Exception {
        final ArtifactIdentifier artifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");

        final PomFile pomFile = PomFileFactory.createFromFile(TemporaryPomFileBuilder.aPomFile()
                .forArtifact(artifact)
                .build());
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder
                .aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(artifact))
                .build();
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .locatedAt("target/AnalyzerTestExamples/callPathsNotUsedInProduction/")
                .withEmptyJarFor(artifact)
                .build();
        final Path projectRootPath = Paths.get("target/");
        final MavenTestPathIdentifier mavenTestPathIdentifier = MavenTestPathIdentifier.parse("src/test/java/");
        final AnalyzerConfiguration configuration = new AnalyzerConfiguration(pomFile, dependencyGraph, localRepository, projectRootPath, mavenTestPathIdentifier);

        final VulnerablePoint vulnerablePoint = vulnerablePointFor(cveForCVE_2013_22541());
        final List<CallPath> callPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(aFunctionIdentifier()
                                .withArtifactIdentifier(artifact)
                                .forClass("someClass.class")
                                .forFunction("function")
                                .build())
                        .build()
        );
        final List<CVE> cves = Collections.singletonList(cveForCVE_2013_22541());
        return new AnalyzerTestExample(configuration, callPaths, cves);
    }

    public static AnalyzerTestExample callPathsNotUsedSocketButHavingACveTargetingSockets() throws Exception {
        return null;
    }

    public static AnalyzerTestExample vulnerableCallPaths() {
        return null;
    }

    static class AnalyzerTestExample {
        private final AnalyzerConfiguration configuration;
        private final List<CallPath> callPaths;
        private final List<CVE> cves;

        AnalyzerTestExample(AnalyzerConfiguration configuration, List<CallPath> callPaths, List<CVE> cves) {
            this.configuration = configuration;
            this.callPaths = callPaths;
            this.cves = cves;
        }

        public AnalyzerConfiguration getConfiguration() {
            return configuration;
        }

        public List<CallPath> getCallPaths() {
            return callPaths;
        }

        public List<CVE> getCves() {
            return cves;
        }
    }
}
