package castendyck.maven.pomfile;

public class PomFileCreationException extends Exception {
    public PomFileCreationException(String message, Exception exception) {
        super(message, exception);
    }
}
