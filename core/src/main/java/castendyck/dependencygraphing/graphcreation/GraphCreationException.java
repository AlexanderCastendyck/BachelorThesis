package castendyck.dependencygraphing.graphcreation;

public class GraphCreationException extends Exception {
    public GraphCreationException(Exception e) {
        super(e);
    }

    public GraphCreationException(String message) {
        super(message);
    }
}
