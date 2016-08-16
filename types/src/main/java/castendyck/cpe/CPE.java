package castendyck.cpe;

import de.castendyck.enforcing.NotNullConstraintEnforcer;


// Format Cpe: cpe:/{part}:{vendor}:{product}:{version}:{update}:{edition}:{language}
public class CPE {
    private enum PartIndex {CPE, PART, VENDOR, PRODUCT, VERSION, UPDATE, EDITION, LANGUAGE}
    private final String value;

    private CPE(String value) {
        NotNullConstraintEnforcer.ensureNotNull(value);
        this.value = value;
    }

    public static CPE createNew(String value) {
        return new CPE(value);
    }

    public String getValue() {
        return value;
    }

    public String getVendor() throws CpeNameParsingException {
        return getPart(PartIndex.VENDOR);
    }

    public String getProduct() throws CpeNameParsingException {
        return getPart(PartIndex.PRODUCT);
    }

    public boolean hasVersionSet() {
        try {
            return getVersion() != null;
        } catch (CpeNameParsingException e) {
            return false;
        }
    }

    public String getVersion() throws CpeNameParsingException {
        return getPart(PartIndex.VERSION);
    }

    private String getPart(PartIndex partIndex) throws CpeNameParsingException {
        final String[] split = value.split(":");
        final int indexInSplit = partIndex.ordinal();
        if (indexInSplit >= split.length) {
            throw new CpeNameParsingException("Could not extract "+partIndex + " out of "+value);
        } else {
            final String part = split[indexInSplit];
            return part;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CPE cpe = (CPE) o;

        return value.equals(cpe.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "CPE{" +
                "name='" + value + '\'' +
                '}';
    }
}
