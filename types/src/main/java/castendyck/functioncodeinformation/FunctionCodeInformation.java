package castendyck.functioncodeinformation;

import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.functionidentifier.FunctionIdentifier;

import java.io.InputStream;

public class FunctionCodeInformation {
    private final FunctionIdentifier functionIdentifier;
    private final InputStream streamToClassFile;

    public FunctionCodeInformation(FunctionIdentifier functionIdentifier, InputStream streamToClassFile) {
        NotNullConstraintEnforcer.ensureNotNull(functionIdentifier);
        this.functionIdentifier = functionIdentifier;
        NotNullConstraintEnforcer.ensureNotNull(streamToClassFile);
        this.streamToClassFile = streamToClassFile;
    }

    public FunctionIdentifier getFunctionIdentifier() {
        return functionIdentifier;
    }

    public InputStream getStreamToClassFile() {
        return streamToClassFile;
    }
}
