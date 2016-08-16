package castendyck.vulnerablepoint.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.cve.CVE;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.vulnerablepoint.VulnerablePoint;

public class VulnerablePointImpl implements VulnerablePoint {
    private final FunctionIdentifier functionIdentifier;
    private final CVE cve;

    public VulnerablePointImpl(FunctionIdentifier functionIdentifier, CVE cve) {
        NotNullConstraintEnforcer.ensureNotNull(functionIdentifier);
        this.functionIdentifier = functionIdentifier;
        NotNullConstraintEnforcer.ensureNotNull(cve);
        this.cve = cve;
    }


    @Override
    public ArtifactIdentifier getArtifactIdentifier() {
        return functionIdentifier.getArtifactIdentifier();
    }

    @Override
    public FunctionIdentifier getFunctionIdentifier() {
        return functionIdentifier;
    }

    @Override
    public CVE getAssociatedCve() {
        return cve;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VulnerablePointImpl that = (VulnerablePointImpl) o;

        if (!functionIdentifier.equals(that.functionIdentifier)) return false;
        return cve.equals(that.cve);

    }

    @Override
    public int hashCode() {
        int result = functionIdentifier.hashCode();
        result = 31 * result + cve.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "VulnerablePointImpl{" +
                "functionIdentifier=" + functionIdentifier +
                ", cve=" + cve +
                '}';
    }
}
