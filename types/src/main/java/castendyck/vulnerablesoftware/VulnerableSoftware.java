package castendyck.vulnerablesoftware;

import castendyck.cpe.CPE;
import de.castendyck.enforcing.NotNullConstraintEnforcer;

public class VulnerableSoftware {
    private final CPE cpe;

    private VulnerableSoftware(CPE cpe) {
        NotNullConstraintEnforcer.ensureNotNull(cpe);
        this.cpe = cpe;
    }

    public static VulnerableSoftware createNew(CPE cpe) {
        return new VulnerableSoftware(cpe);
    }

    public static VulnerableSoftware createFromString(String cpeString) {
        final CPE cpe = CPE.createNew(cpeString);
        return createNew(cpe);
    }

    public CPE getCpe() {
        return cpe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VulnerableSoftware that = (VulnerableSoftware) o;

        return cpe.equals(that.cpe);

    }

    @Override
    public int hashCode() {
        return cpe.hashCode();
    }

    @Override
    public String toString() {
        return "VulnerableSoftware{" +
                "cpe='" + cpe + '\'' +
                '}';
    }
}
