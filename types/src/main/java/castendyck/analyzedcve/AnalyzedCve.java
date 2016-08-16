package castendyck.analyzedcve;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.judgement.Judgement;
import castendyck.printable.Printable;
import castendyck.processedcve.ProcessedCve;

import java.util.List;

public interface AnalyzedCve extends Printable{
    ProcessedCve getProcessedCve();
    Judgement getJudgement();
    List<ArtifactIdentifier> getVulnerableArtifacts();

}
