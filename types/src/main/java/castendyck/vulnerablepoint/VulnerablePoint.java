package castendyck.vulnerablepoint;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.cve.CVE;
import castendyck.functionidentifier.FunctionIdentifier;

public interface VulnerablePoint {
    ArtifactIdentifier getArtifactIdentifier();
    FunctionIdentifier getFunctionIdentifier();
    CVE getAssociatedCve();

}
