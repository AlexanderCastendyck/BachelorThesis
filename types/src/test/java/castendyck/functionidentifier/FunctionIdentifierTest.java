package castendyck.functionidentifier;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.classidentifier.ClassIdentifier;
import de.castendyck.enforcing.ValueMustNotBeNullException;
import castendyck.functionname.FunctionName;
import castendyck.functionname.FunctionNameFactory;
import castendyck.functionsignature.FunctionSignature;
import castendyck.functionsignature.FunctionSignatureFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;

public class FunctionIdentifierTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void constructorExpectsNonNullValue_forArtifactIdentifier() throws Exception {
        final ClassIdentifier classIdentifier = new ClassIdentifier("someClass.class");
        final FunctionName functionName = FunctionNameFactory.createNew("someFunction");
        final FunctionSignature functionSignature = FunctionSignatureFactory.createNew("()V");

        expectedException.expect(ValueMustNotBeNullException.class);
        new FunctionIdentifier(null, classIdentifier, functionName, functionSignature);
    }

    @Test
    public void constructorExpectsNonNullValue_forClassIdentifier() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId("someGroup")
                .withArtifactId("someArtifact")
                .withVersion("1.0")
                .build();
        final FunctionName functionName = FunctionNameFactory.createNew("someFunction");
        final FunctionSignature functionSignature = FunctionSignatureFactory.createNew("()V");

        expectedException.expect(ValueMustNotBeNullException.class);
        new FunctionIdentifier(artifactIdentifier, null, functionName, functionSignature);
    }

    @Test
    public void constructorExpectsNonNullValue_forFunctionName() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId("someGroup")
                .withArtifactId("someArtifact")
                .withVersion("1.0")
                .build();
        final ClassIdentifier classIdentifier = new ClassIdentifier("someClass.class");
        final FunctionSignature functionSignature = FunctionSignatureFactory.createNew("()V");

        expectedException.expect(ValueMustNotBeNullException.class);
        new FunctionIdentifier(artifactIdentifier, classIdentifier, null, functionSignature);
    }

    @Test
    public void constructorExpectsNonNullValue_forFunctionSignature() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId("someGroup")
                .withArtifactId("someArtifact")
                .withVersion("1.0")
                .build();
        final ClassIdentifier classIdentifier = new ClassIdentifier("someClass.class");
        final FunctionName functionName = FunctionNameFactory.createNew("someFunction");

        expectedException.expect(ValueMustNotBeNullException.class);
        new FunctionIdentifier(artifactIdentifier, classIdentifier, functionName, null);
    }

    @Test
    public void equals_returnsTrueForSameValues() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId("someGroup")
                .withArtifactId("someArtifact")
                .withVersion("1.0")
                .build();
        final ClassIdentifier classIdentifier = new ClassIdentifier("someClass.class");
        final FunctionName functionName = FunctionNameFactory.createNew("someFunction");
        final FunctionSignature functionSignature = FunctionSignatureFactory.createNew("()V");

        final FunctionIdentifier functionIdentifier1 = new FunctionIdentifier(artifactIdentifier, classIdentifier, functionName, functionSignature);
        final FunctionIdentifier functionIdentifier2 = new FunctionIdentifier(artifactIdentifier, classIdentifier, functionName, functionSignature);

        Assert.assertThat(functionIdentifier1, equalTo(functionIdentifier2));
    }


}