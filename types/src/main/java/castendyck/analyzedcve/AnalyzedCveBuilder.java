package castendyck.analyzedcve;

import castendyck.analyzedcve.internal.AnalyzedCveImpl;
import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.judgement.Judgement;
import castendyck.processedcve.ProcessedCve;

import java.util.ArrayList;
import java.util.List;

public class AnalyzedCveBuilder {
    private final List<ArtifactIdentifier> vulnerableArtifacts = new ArrayList<>();
    private ProcessedCve processedCve;
    private Judgement judgement;

    public static AnalyzedCveBuilder anAnalyzedCve(){
        return new AnalyzedCveBuilder();
    }

    public AnalyzedCveBuilder forAProcessedCve(ProcessedCve processedCve) {
        this.processedCve = processedCve;
        return this;
    }

    public AnalyzedCveBuilder withJudgement(Judgement judgement) {
        this.judgement = judgement;
        return this;
    }

    public AnalyzedCveBuilder withAVulnerableArtifact(ArtifactIdentifier artifactIdentifier){
        this.vulnerableArtifacts.add(artifactIdentifier);
        return this;
    }

    public AnalyzedCve build(){
        return new AnalyzedCveImpl(processedCve, judgement, vulnerableArtifacts);
    }
}
