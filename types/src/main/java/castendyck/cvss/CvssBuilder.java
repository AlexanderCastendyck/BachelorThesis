package castendyck.cvss;

import javax.annotation.Nullable;

import static de.castendyck.enforcing.NullStringEscaper.escapeNullStringWith;

public class CvssBuilder {
    private double score;
    private String accessVector = "dummy";
    private String accessComplexity= "dummy";
    private String authentication= "dummy";
    private String confidentialityImpact= "dummy";
    private String integrityImpact= "dummy";
    private String availabilityImpact= "dummy";

    public static CvssBuilder aCvss() {
        return new CvssBuilder();
    }

    public CvssBuilder withAScore(double score) {
        this.score = score;
        return this;
    }

    public CvssBuilder withAccessVector(@Nullable String accessVector) {
        final String escapedValue = escapeNullStringWith(accessVector, "");
        this.accessVector = escapedValue;
        return this;
    }

    public CvssBuilder withAccessComplexity(@Nullable String accessComplexity) {
        final String escapedValue = escapeNullStringWith(accessComplexity, "");
        this.accessComplexity = escapedValue;
        return this;
    }

    public CvssBuilder withAuthentication(@Nullable String authentication) {
        final String escapedValue = escapeNullStringWith(authentication, "");
        this.authentication = escapedValue;
        return this;
    }

    public CvssBuilder withConfidentialityImpact(@Nullable String confidentialityImpact) {
        final String escapedValue = escapeNullStringWith(confidentialityImpact, "");
        this.confidentialityImpact = escapedValue;
        return this;
    }

    public CvssBuilder withIntegrityImpact(@Nullable String integrityImpact) {
        final String escapedValue = escapeNullStringWith(integrityImpact, "");
        this.integrityImpact = escapedValue;
        return this;
    }

    public CvssBuilder withAvailabilityImpact(@Nullable String availabilityImpact) {
        final String escapedValue = escapeNullStringWith(availabilityImpact, "");
        this.availabilityImpact = escapedValue;
        return this;
    }

    public CVSS build() {
        final CVSS cvss = new CVSS(score, accessVector, accessComplexity, authentication, confidentialityImpact, integrityImpact, availabilityImpact);
        return cvss;
    }
}
