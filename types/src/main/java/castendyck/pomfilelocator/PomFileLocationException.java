package castendyck.pomfilelocator;

public class PomFileLocationException extends Exception {
    public PomFileLocationException(Exception e) {
        super(e);
    }

    public PomFileLocationException() {
        super();
    }
}
