package castendyck.analyzedcve.internal;

import castendyck.analyzedcve.AnalyzedCve;
import castendyck.artifactidentifier.ArtifactIdentifier;
import de.castendyck.collections.ObjectListToStringCollector;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.judgement.Judgement;
import castendyck.media.Media;
import castendyck.processedcve.ProcessedCve;

import java.util.List;

public class AnalyzedCveImpl implements AnalyzedCve {
    private final ProcessedCve processedCve;
    private final Judgement judgement;
    private final List<ArtifactIdentifier> vulnerableArtifacts;

    public AnalyzedCveImpl(ProcessedCve processedCve, Judgement judgement, List<ArtifactIdentifier> vulnerableArtifacts) {
        NotNullConstraintEnforcer.ensureNotNull(processedCve);
        this.processedCve = processedCve;
        NotNullConstraintEnforcer.ensureNotNull(judgement);
        this.judgement = judgement;
        NotNullConstraintEnforcer.ensureNotNull(vulnerableArtifacts);
        this.vulnerableArtifacts = vulnerableArtifacts;
    }

    @Override
    public ProcessedCve getProcessedCve() {
        return processedCve;
    }

    @Override
    public Judgement getJudgement() {
        return judgement;
    }

    @Override
    public List<ArtifactIdentifier> getVulnerableArtifacts() {
        return vulnerableArtifacts;
    }

    @Override
    public void print(Media media) {
        media.starting("judgement");
        judgement.print(media);
        media.ending("judgement");

        media.starting("vulnerableArtifacts");
        if (vulnerableArtifacts.size() == 1) {
            final String vulnerableArtifact = vulnerableArtifacts.get(0).asSimpleString();
            media.withText("vulnerable artifact: ")
                    .with("vulnerableArtifact", vulnerableArtifact);
        }
        if (vulnerableArtifacts.size() > 1) {
            final String vulnerableArtifactsAsString = vulnerableArtifacts.stream()
                    .collect(ObjectListToStringCollector.collectToString());
            media.withText("vulnerable artifacts: ")
                    .with("vulnerableArtifact", vulnerableArtifactsAsString);
        }
        media.ending("vulnerableArtifacts");
    }
}
