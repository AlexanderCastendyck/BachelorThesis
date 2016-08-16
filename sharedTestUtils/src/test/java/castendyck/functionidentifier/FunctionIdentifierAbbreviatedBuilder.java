package castendyck.functionidentifier;

import castendyck.artifactidentifier.ArtifactIdentifier;

public class FunctionIdentifierAbbreviatedBuilder {
    public static FunctionIdentifier aFunctionIdentifierFor(ArtifactIdentifier artifactIdentifier, String className, String function){
        return FunctionIdentifierBuilder.aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass(className)
                .forFunction(function)
                .build();
    }
}
