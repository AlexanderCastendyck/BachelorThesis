package castendyck.callgraph;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class CallPathTest {

    private final ArtifactIdentifier artifactIdentifier;
    private final FunctionIdentifier functionIdentifier;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private FunctionIdentifier differentFunctionIdentifier;

    public CallPathTest() {
        this.artifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId("someGroup")
                .withArtifactId("someArtifact")
                .withVersion("1.0")
                .build();

        this.functionIdentifier = FunctionIdentifierBuilder.aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass("someClass.class")
                .forFunction("someFunction")
                .build();

        this.differentFunctionIdentifier = FunctionIdentifierBuilder.aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass("different.class")
                .forFunction("differentFunction")
                .build();
    }

    @Test
    public void containsFunctionIdentifier_returnsTrueForExistingFunctionIdentifier() throws Exception {
        CallPath callPath = CallPathBuilder.aCallPath()
                .containingFunctionIdentifier(functionIdentifier)
                .build();

        final boolean containing = callPath.containsFunctionIdentifier(functionIdentifier);
        assertThat(containing, is(true));
    }

    @Test
    public void containsFunctionIdentifier_returnsFalseForDifferentFunctionIdentifier() throws Exception {
        CallPath callPath = CallPathBuilder.aCallPath()
                .containingFunctionIdentifier(functionIdentifier)
                .build();

        final boolean containing = callPath.containsFunctionIdentifier(differentFunctionIdentifier);
        assertThat(containing, is(false));
    }

    @Test
    public void addFunctionIdentifier_succeedsForNotAlreadyStoredFunctionIdentifier() throws Exception {
        CallPath callPath = CallPathBuilder.aCallPath()
                .containingFunctionIdentifier(functionIdentifier)
                .build();

        callPath.addFunctionIdentifier(differentFunctionIdentifier);

        List<FunctionIdentifier> storedFunctionIdentifier = callPath.getFunctions();
        assertThat(storedFunctionIdentifier, contains(functionIdentifier, differentFunctionIdentifier));
    }

    @Test
    public void addFunctionIdentifier_failsForAlreadyStoredFunctionIdentifier() throws Exception {
        CallPath callPath = CallPathBuilder.aCallPath()
                .containingFunctionIdentifier(functionIdentifier)
                .build();

        expectedException.expect(FunctionIdentifierAlreadyPresentInThisCallGraphException.class);
        callPath.addFunctionIdentifier(functionIdentifier);

    }

    @Test
    public void getCurrentFunctionIdentifier_returnsLastAddedFunctionIdentifier() throws Exception {
        CallPath callPath = CallPathBuilder.aCallPath()
                .containingFunctionIdentifier(functionIdentifier)
                .containingFunctionIdentifier(differentFunctionIdentifier)
                .build();

        final FunctionIdentifier currentFunctionIdentifier = callPath.getCurrentFunctionIdentifier();
        assertThat(currentFunctionIdentifier, equalTo(differentFunctionIdentifier));
    }
}