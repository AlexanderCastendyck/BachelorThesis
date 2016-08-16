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

public class CveBuilder {
    private String name;
    private String description;
    private CWE cwe;
    private CVSS cvss;
    private final List<CPE> cpes;
    private final Set<Reference> references;
    private final Set<VulnerableSoftware> vulnerableSoftwareList;

    public CveBuilder() {
        this.cpes = new ArrayList<>();
        this.references = new HashSet<>();
        this.vulnerableSoftwareList = new HashSet<>();
    }

    public static CveBuilder aCve() {
        return new CveBuilder();
    }

    public CveBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CveBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CveBuilder withCwe(CWE cwe) {
        this.cwe = cwe;
        return this;
    }

    public CveBuilder withCvss(CVSS cvss) {
        this.cvss = cvss;
        return this;
    }

    public CveBuilder withCpes(List<CPE> cpes) {
        this.cpes.addAll(cpes);
        return this;
    }

    public CveBuilder withReferences(Set<Reference> references) {
        this.references.addAll(references);
        return this;
    }

    public CveBuilder withVulnerableSoftwareList(Set<VulnerableSoftware> vulnerableSoftwareList) {
        this.vulnerableSoftwareList.addAll(vulnerableSoftwareList);
        return this;
    }

    public CVE build() {
        final CVE cve = new CVE(name, description, cwe, cvss, cpes, references, vulnerableSoftwareList);
        return cve;
    }
}
