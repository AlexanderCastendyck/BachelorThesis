package castendyck.callgraph.functiondata;

public class CouldNotExtractCalledFunctionsOutOfClassException extends Exception {
    public CouldNotExtractCalledFunctionsOutOfClassException(String message) {
        super(message);
    }

    public CouldNotExtractCalledFunctionsOutOfClassException(Exception e) {
        super(e);
    }
}
