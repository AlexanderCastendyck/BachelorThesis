package castendyck.cve;

import castendyck.cpe.CPE;
import castendyck.cvss.CVSS;
import castendyck.cwe.CWE;
import castendyck.references.Reference;
import castendyck.vulnerablesoftware.VulnerableSoftware;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static castendyck.cvss.CvssBuilder.aCvss;

public class CveTestBuilder {
    private final Set<Reference> references;
    private final List<CPE> cpes;
    private final Set<VulnerableSoftware> vulnerableSoftware;
    private String name;
    private CVSS cvss;
    private CWE cwe;
    private String description;

    public CveTestBuilder() {
        this.name = "someName";
        this.cvss = aCvss().withAScore(5.0)
                .build();
        this.cwe = CWE.createNew("CWE-0");
        this.description = "some Description";
        this.cpes = new ArrayList<>();
        this.references = new HashSet<>();
        this.vulnerableSoftware = new HashSet<>();
    }

    public static CveTestBuilder aCve() {
        return new CveTestBuilder();
    }

    public CveTestBuilder witCpe(CPE cpe) {
        this.cpes.add(cpe);
        return this;
    }

    public CveTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CVE build() {
        return CveBuilder.aCve()
                .withName(name)
                .withCvss(cvss)
                .withCwe(cwe)
                .withDescription(description)
                .withCpes(cpes)
                .withReferences(references)
                .withVulnerableSoftwareList(vulnerableSoftware)
                .build();
    }

    public CveTestBuilder withName(String cveName) {
        this.name = cveName;
        return this;
    }
}
