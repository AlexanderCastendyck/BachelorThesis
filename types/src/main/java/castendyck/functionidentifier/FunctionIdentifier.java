package castendyck.functionidentifier;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.classidentifier.ClassIdentifier;
import castendyck.functionname.FunctionName;
import castendyck.functionsignature.FunctionSignature;

import static de.castendyck.enforcing.NotNullConstraintEnforcer.ensureNotNull;


public class FunctionIdentifier {
    private final ArtifactIdentifier artifactIdentifier;
    private final ClassIdentifier classIdentifier;
    private final FunctionName functionName;
    private final FunctionSignature functionSignature;

    FunctionIdentifier(ArtifactIdentifier artifactIdentifier, ClassIdentifier classIdentifier, FunctionName functionName, FunctionSignature functionSignature) {
        ensureNotNull(artifactIdentifier);
        this.artifactIdentifier = artifactIdentifier;
        ensureNotNull(classIdentifier);
        this.classIdentifier = classIdentifier;
        ensureNotNull(functionName);
        this.functionName = functionName;
        ensureNotNull(functionSignature);
        this.functionSignature = functionSignature;
    }

    public ArtifactIdentifier getArtifactIdentifier() {
        return this.artifactIdentifier;
    }

    public ClassIdentifier getClassIdentifier() {
        return this.classIdentifier;
    }


    public FunctionName getFunctionName() {
        return this.functionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionIdentifier that = (FunctionIdentifier) o;

        if (!artifactIdentifier.equals(that.artifactIdentifier)) return false;
        if (!classIdentifier.equals(that.classIdentifier)) return false;
        if (!functionName.equals(that.functionName)) return false;
        return functionSignature.equals(that.functionSignature);

    }

    @Override
    public int hashCode() {
        int result = artifactIdentifier.hashCode();
        result = 31 * result + classIdentifier.hashCode();
        result = 31 * result + functionName.hashCode();
        result = 31 * result + functionSignature.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FunctionIdentifier{" +
                "artifactIdentifier=" + artifactIdentifier +
                ", classIdentifier=" + classIdentifier +
                ", functionName=" + functionName +
                '}';
    }
}
