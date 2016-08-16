package castendyck.analyzing.reducing.internal;

import castendyck.analyzing.reducing.Reducer;
import castendyck.analyzing.reducing.ReducerConfiguration;
import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.CallPath;
import castendyck.mavendependency.MavenDependency;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileCreationException;
import castendyck.pomfilelocator.PomFileLocationException;
import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;
import castendyck.scope.MavenScope;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static castendyck.analyzing.reducing.internal.ArtifactsOfCallPathCollector.findArtifactsTraversedBy;
import static castendyck.reduction.ReductionBuilder.aReduction;


public class CallPathNeverTraversesAnArtifactWithTestScopeReducerImpl implements Reducer {
    private final Logger logger = Logger.getLogger(CallPathNeverTraversesAnArtifactWithTestScopeReducerImpl.class);
    private final PomFile pomFile;

    public CallPathNeverTraversesAnArtifactWithTestScopeReducerImpl(ReducerConfiguration configuration) {
        this.pomFile = configuration.getPomFile();
    }

    @Override
    public Reduction reduceFurther(Reduction previousReduction, ProcessedCve processedCve) {
        final List<CallPath> remainingCallPaths = previousReduction.getRemainingCallPaths();
        final List<CallPath> reducedCallPaths = new ArrayList<>();

        for (CallPath callPath : remainingCallPaths) {
            final List<ArtifactIdentifier> traversedArtifacts = findArtifactsTraversedBy(callPath);
            final List<ArtifactIdentifier> artifactsWithTestScope = findArtifactsWithTestScope(traversedArtifacts);

            if (artifactsWithTestScope.isEmpty()) {
                reducedCallPaths.add(callPath);
            }
        }

        final String reason = getReason();
        final Reduction reduction = aReduction()
                .containing(reducedCallPaths)
                .withReason(reason)
                .build();
        return reduction;
    }

    private List<ArtifactIdentifier> findArtifactsWithTestScope(List<ArtifactIdentifier> artifacts) {
        final List<MavenDependency> mavenDependencies = collectAllMavenDependenciesFrom(pomFile);

        final List<ArtifactIdentifier> artifactsWithTestScope = new ArrayList<>();
        for (ArtifactIdentifier artifact : artifacts) {
            final MavenDependency mavenDependency = getMavenDependencyFor(artifact, mavenDependencies);
            if (mavenDependency != null && hasTestScope(mavenDependency)) {
                artifactsWithTestScope.add(artifact);
            }
        }
        return artifactsWithTestScope;
    }

    private MavenDependency getMavenDependencyFor(ArtifactIdentifier artifact, List<MavenDependency> mavenDependencies) {
        final List<MavenDependency> possibleRedundantMatchingDependencies = mavenDependencies.stream()
                .filter(md -> md.getArtifactIdentifier().equals(artifact))
                .collect(Collectors.toList());
        if (possibleRedundantMatchingDependencies.size() >= 1) {
            return possibleRedundantMatchingDependencies.get(0);
        } else {
            return null;
        }
    }

    private List<MavenDependency> collectAllMavenDependenciesFrom(PomFile pomFile) {
        final List<MavenDependency> mavenDependencies = pomFile.getDependencies();

        for (PomFile subModule : getSubModulesOf(pomFile)) {
            final List<MavenDependency> mavenDependenciesOfSubModule = collectAllMavenDependenciesFrom(subModule);
            mavenDependencies.addAll(mavenDependenciesOfSubModule);
        }
        return mavenDependencies;
    }

    private List<PomFile> getSubModulesOf(PomFile pomFile) {
        try {
            return pomFile.getModules();
        } catch (PomFileLocationException | PomFileCreationException e) {
            logger.debug(e.getClass() + "was thrown at CallPathNeverTraversesAnArtifactWithTestScopeReducerImpl: " + e.getMessage());
            logger.error("Could not obtain submodules of " + pomFile.getArtifactIdentifier().asSimpleString());
            logger.error("Ignoring it's submodules");

            return new ArrayList<>();
        }
    }

    private boolean hasTestScope(MavenDependency mavenDependency) {
        final String scope = mavenDependency.getScope().asString();
        return scope.equals(MavenScope.TEST);
    }

    @Override
    public String getReason() {
        return "Vulnerability in artifact with scope \"test\"";
    }
}
