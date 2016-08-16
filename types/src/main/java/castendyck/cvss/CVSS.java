package castendyck.cvss;

import de.castendyck.enforcing.NotNullConstraintEnforcer;

public class CVSS {
    private final double score;
    private final String accessVector;
    private final String accessComplexity;
    private final String authentication;
    private final String confidentialityImpact;
    private final String integrityImpact;
    private final String availabilityImpact;

    CVSS(double score, String accessVector, String accessComplexity, String authentication, String confidentialityImpact, String integrityImpact, String availabilityImpact) {
        NotNullConstraintEnforcer.ensureNotNullOrEmptyString(score);
        score = round(score);
        this.score = score;
        NotNullConstraintEnforcer.ensureNotNullOrEmptyString(accessVector);
        this.accessVector = accessVector;
        NotNullConstraintEnforcer.ensureNotNullOrEmptyString(accessComplexity);
        this.accessComplexity = accessComplexity;
        NotNullConstraintEnforcer.ensureNotNullOrEmptyString(authentication);
        this.authentication = authentication;
        NotNullConstraintEnforcer.ensureNotNullOrEmptyString(confidentialityImpact);
        this.confidentialityImpact = confidentialityImpact;
        NotNullConstraintEnforcer.ensureNotNullOrEmptyString(integrityImpact);
        this.integrityImpact = integrityImpact;
        NotNullConstraintEnforcer.ensureNotNullOrEmptyString(availabilityImpact);
        this.availabilityImpact = availabilityImpact;
    }

    private double round(double score) {
        final int rounded = (int)(score * 10);
        return rounded/10;
    }

    public double getScore() {
        return score;
    }

    public String getAccessVector() {
        return accessVector;
    }

    public String getAccessComplexity() {
        return accessComplexity;
    }

    public String getAuthentication() {
        return authentication;
    }

    public String getConfidentialityImpact() {
        return confidentialityImpact;
    }

    public String getIntegrityImpact() {
        return integrityImpact;
    }

    public String getAvailabilityImpact() {
        return availabilityImpact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CVSS cvss = (CVSS) o;

        if (Double.compare(cvss.score, score) != 0) return false;
        if (!accessVector.equals(cvss.accessVector)) return false;
        if (!accessComplexity.equals(cvss.accessComplexity)) return false;
        if (!authentication.equals(cvss.authentication)) return false;
        if (!confidentialityImpact.equals(cvss.confidentialityImpact)) return false;
        if (!integrityImpact.equals(cvss.integrityImpact)) return false;
        return availabilityImpact.equals(cvss.availabilityImpact);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(score);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + accessVector.hashCode();
        result = 31 * result + accessComplexity.hashCode();
        result = 31 * result + authentication.hashCode();
        result = 31 * result + confidentialityImpact.hashCode();
        result = 31 * result + integrityImpact.hashCode();
        result = 31 * result + availabilityImpact.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CVSS{" +
                "score=" + score +
                ", accessVector='" + accessVector + '\'' +
                ", accessComplexity='" + accessComplexity + '\'' +
                ", authentication='" + authentication + '\'' +
                ", confidentialityImpact='" + confidentialityImpact + '\'' +
                ", integrityImpact='" + integrityImpact + '\'' +
                ", availabilityImpact='" + availabilityImpact + '\'' +
                '}';
    }
}
