package castendyck.callgraph.sourcecoderegistering;

import java.io.IOException;
import java.nio.file.Path;

public class NoJarForThisArtifactIdentifierFoundException extends Exception {
    public NoJarForThisArtifactIdentifierFoundException(String s) {
        super(s);
    }

    public NoJarForThisArtifactIdentifierFoundException(Exception e) {
        super(e);
    }

}
