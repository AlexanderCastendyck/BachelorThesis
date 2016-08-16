package castendyck.maven.pomfile;

public class PomFileException  extends RuntimeException{
    public PomFileException(String message) {
        super(message);
    }

    public PomFileException(String message, Throwable e) {
        super(message, e);
    }
}
