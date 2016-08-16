package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.cve.CVE;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import castendyck.vulnerablepoint.VulnerablePoint;
import castendyck.vulnerablepoint.VulnerablePointBuilder;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpretingResult;

import java.util.ArrayList;
import java.util.List;

public class CveInterpretingResultBuilder {
    private CveInterpretingResult.RESULT result;
    private final List<VulnerablePoint> vulnerablePoints;

    public CveInterpretingResultBuilder() {
        this.vulnerablePoints = new ArrayList<>();
    }

    public static CveInterpretingResultBuilder anInterpretingResult() {
        return new CveInterpretingResultBuilder();
    }

    public CveInterpretingResultBuilder withResultStatus(CveInterpretingResult.RESULT result) {
        this.result = result;
        return this;
    }

    public CveInterpretingResultBuilder withAVulnerablePoint(String method, String clazz, ArtifactIdentifier artifactIdentifier, CVE cve) {
        final FunctionIdentifier functionIdentifier = FunctionIdentifierBuilder.aFunctionIdentifier()
                .forFunction(method)
                .withSignature("()V")
                .forClass(clazz)
                .withArtifactIdentifier(artifactIdentifier)
                .build();

        final VulnerablePoint vulnerablePoint = VulnerablePointBuilder.aVulnerablePoint()
                .forCve(cve)
                .withFunctionIdentifier(functionIdentifier)
                .build();
        this.vulnerablePoints.add(vulnerablePoint);
        return this;
    }

    public CveInterpretingResultBuilder withNoVulnerablePoints() {
        vulnerablePoints.clear();
        return this;
    }

    public CveInterpretingResult build() {
        return new CveInterpretingResult(result, vulnerablePoints);
    }

}
