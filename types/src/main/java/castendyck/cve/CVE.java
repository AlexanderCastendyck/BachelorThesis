package castendyck.cve;

import castendyck.cpe.CPE;
import castendyck.cvss.CVSS;
import castendyck.cwe.CWE;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.references.Reference;
import castendyck.vulnerablesoftware.VulnerableSoftware;

import java.util.List;
import java.util.Set;

public class CVE {
    private final String name;
    private final String description;
    private final CWE cwe;
    private final CVSS cvss;
    private final List<CPE> cpes;
    private final Set<Reference> references;
    private final Set<VulnerableSoftware> vulnerableSoftwareList;

    CVE(String name, String description, CWE cwe, CVSS cvss, List<CPE> cpes, Set<Reference> references, Set<VulnerableSoftware> vulnerableSoftwareList) {
        NotNullConstraintEnforcer.ensureNotNullOrEmptyString(name);
        this.name = name;
        NotNullConstraintEnforcer.ensureNotNullOrEmptyString(description);
        this.description = description;
        NotNullConstraintEnforcer.ensureNotNull(cwe);
        this.cwe = cwe;
        NotNullConstraintEnforcer.ensureNotNull(cvss);
        this.cvss = cvss;
        NotNullConstraintEnforcer.ensureNotNull(cpes);
        this.cpes = cpes;
        NotNullConstraintEnforcer.ensureNotNull(references);
        this.references = references;
        NotNullConstraintEnforcer.ensureNotNull(vulnerableSoftwareList);
        this.vulnerableSoftwareList = vulnerableSoftwareList;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CWE getCwe() {
        return cwe;
    }

    public CVSS getCvss() {
        return cvss;
    }

    public List<CPE> getCpes() {
        return cpes;
    }

    public Set<Reference> getReferences() {
        return references;
    }

    public Set<VulnerableSoftware> getVulnerableSoftwareList() {
        return vulnerableSoftwareList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CVE cve = (CVE) o;

        if (!name.equals(cve.name)) return false;
        if (!description.equals(cve.description)) return false;
        if (!cwe.equals(cve.cwe)) return false;
        if (!cvss.equals(cve.cvss)) return false;
        if (!cpes.equals(cve.cpes)) return false;
        if (!references.equals(cve.references)) return false;
        return vulnerableSoftwareList.equals(cve.vulnerableSoftwareList);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + cwe.hashCode();
        result = 31 * result + cvss.hashCode();
        result = 31 * result + cpes.hashCode();
        result = 31 * result + references.hashCode();
        result = 31 * result + vulnerableSoftwareList.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CVE{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cwe=" + cwe +
                '}';
    }
}
