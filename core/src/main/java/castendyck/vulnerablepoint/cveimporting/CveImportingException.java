package castendyck.vulnerablepoint.cveimporting;

public class CveImportingException extends Exception {
    public CveImportingException(Throwable throwable) {
        super(throwable);
    }

    public CveImportingException() {
        super();
    }
}
