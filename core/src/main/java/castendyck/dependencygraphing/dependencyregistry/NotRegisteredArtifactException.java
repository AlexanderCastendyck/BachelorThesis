package castendyck.dependencygraphing.dependencyregistry;

public class NotRegisteredArtifactException extends Exception{
    public NotRegisteredArtifactException(String s) {
        super(s);
    }
}
