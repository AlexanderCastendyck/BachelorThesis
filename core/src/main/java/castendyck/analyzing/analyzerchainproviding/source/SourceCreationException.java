package castendyck.analyzing.analyzerchainproviding.source;

public class SourceCreationException extends Exception{
    public SourceCreationException() {
        super();
    }

    public SourceCreationException(Throwable throwable) {
        super(throwable);
    }

    public SourceCreationException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
