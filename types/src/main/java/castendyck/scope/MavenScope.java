package castendyck.scope;


public interface MavenScope {
    String COMPILE = "compile";
    String TEST = "test";
    String RUNTIME = "runtime";
    String SYSTEM = "system";
    String PROVIDED = "provided";
    String IMPORT = "import";
    String NONE = "none";

    String asString();
}
