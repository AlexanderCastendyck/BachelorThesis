package castendyck.vulnerablepoint.usedcpefinding.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.cpe.CPE;
import castendyck.cpes.cpetoartifactmatching.CpeToArtifactMatcher;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileCreationException;
import castendyck.mavendependency.MavenDependency;
import castendyck.pomfilelocator.PomFileLocationException;
import castendyck.vulnerablepoint.usedcpefinding.UsedCpeFinder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsedCpeFinderImpl implements UsedCpeFinder {
    private static final Logger logger = Logger.getLogger(UsedCpeFinderImpl.class);

    @Override
    public List<CPE> findUsedCpesIn(PomFile pomFile, List<CPE> cpes) {
        final List<ArtifactIdentifier> usedArtifacts = collectUsedArtifacts(pomFile);

        final List<CPE> usedCpes = new ArrayList<>();
        for (CPE cpe : cpes) {
            if (artifactIsInUsedArtifacts(cpe, usedArtifacts)) {
                usedCpes.add(cpe);
            }
        }
        return usedCpes;
    }

    private boolean artifactIsInUsedArtifacts(CPE cpe, List<ArtifactIdentifier> usedArtifacts) {
        for (ArtifactIdentifier artifact : usedArtifacts) {
            if (CpeToArtifactMatcher.matches(cpe, artifact)) {
                return true;
            }
        }
        return false;
    }

    private List<ArtifactIdentifier> collectUsedArtifacts(PomFile pomFile) {
        final List<MavenDependency> dependencies = pomFile.getDependencies();
        final List<ArtifactIdentifier> artifacts = dependencies.stream()
                .map(MavenDependency::getArtifactIdentifier)
                .collect(Collectors.toList());

        for (PomFile subModulePomFile : subModulesOf(pomFile)) {
            final List<ArtifactIdentifier> artifactIdentifiers = collectUsedArtifacts(subModulePomFile);
            artifacts.addAll(artifactIdentifiers);
        }
        return artifacts;
    }

    private List<PomFile> subModulesOf(PomFile pomFile) {
        try {
            return pomFile.getModules();
        } catch (PomFileLocationException | PomFileCreationException e) {
            final String message = "UsedCpeFinderImpl: Skipped submodules of " + pomFile.getArtifactIdentifier().asSimpleString() + ", " +
                    "because exception " + e.getClass() + " was thrown with message " + e.getMessage();
            logger.debug(message);
            return new ArrayList<>();
        }
    }
}
