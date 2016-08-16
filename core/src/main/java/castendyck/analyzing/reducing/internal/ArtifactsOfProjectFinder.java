package castendyck.analyzing.reducing.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileCreationException;
import castendyck.pomfilelocator.PomFileLocationException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArtifactsOfProjectFinder {
    private final static Logger logger = Logger.getLogger(ArtifactsOfProjectFinder.class);

    public static List<ArtifactIdentifier> getAllArtifactsOfProject(PomFile pomFile) {
        final Set<ArtifactIdentifier> projectsArtifacts = collectArtifacts(pomFile);
        final ArrayList<ArtifactIdentifier> artifactIdentifierArrayList = new ArrayList<>(projectsArtifacts);
        return artifactIdentifierArrayList;
    }
    private static Set<ArtifactIdentifier> collectArtifacts(PomFile pomFile) {
        final ArtifactIdentifier currentArtifact = pomFile.getArtifactIdentifier();
        final Set<ArtifactIdentifier> projectsArtifacts = aSetContaining(currentArtifact);

        for (PomFile subModule : getSubModulesOf(pomFile)) {
            final Set<ArtifactIdentifier> subModulesArtifacts = collectArtifacts(subModule);
            projectsArtifacts.addAll(subModulesArtifacts);
        }
        return projectsArtifacts;
    }

    private static List<PomFile> getSubModulesOf(PomFile pomFile)  {
        try {
            return pomFile.getModules();
        } catch (PomFileLocationException | PomFileCreationException e) {
            logger.debug(e.getClass() + "was thrown at ArtifactsOfProjectFinder: "+e.getMessage());
            logger.error("Could not obtain submodules of "+pomFile.getArtifactIdentifier().asSimpleString());
            logger.error("Ignoring it's submodules");

            return new ArrayList<>();
        }
    }
    private static Set<ArtifactIdentifier> aSetContaining(ArtifactIdentifier artifactIdentifier){
        final Set<ArtifactIdentifier> set = new HashSet<>();
        set.add(artifactIdentifier);
        return set;
    }
}
