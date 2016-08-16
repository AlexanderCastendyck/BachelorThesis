package castendyck.cwe;

import castendyck.cwe.internal.CweTextToTypeParser;
import de.castendyck.enforcing.NotNullConstraintEnforcer;

public class CWE {
    private final String value;

    private CWE(String value) {
        NotNullConstraintEnforcer.ensureNotNull(value);
        this.value = value;
    }

    public static CWE createNew(String value){
        return new CWE(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CWE cwe = (CWE) o;

        return value.equals(cwe.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "CWE{" +
                "value='" + value + '\'' +
                '}';
    }

    public CweType getType() {
        return CweTextToTypeParser.parse(value);
    }
}
