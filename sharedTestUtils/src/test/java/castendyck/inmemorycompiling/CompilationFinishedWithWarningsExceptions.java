package castendyck.inmemorycompiling;

public class CompilationFinishedWithWarningsExceptions extends Exception {
    public CompilationFinishedWithWarningsExceptions(String warnings) {
        super(warnings);
    }
}
