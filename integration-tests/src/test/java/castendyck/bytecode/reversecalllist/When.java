package castendyck.bytecode.reversecalllist;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.reversecalllist.ReverseCallList;
import castendyck.streamfetching.StreamFetcher;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;

public class When {
    private final ReverseCallListStateBuilder.State state;

    public When(ReverseCallListStateBuilder.State state) {
        this.state = state;
    }

    public Then whenReverseCallListIsCreated() {
        final SourceCodeRegistry sourceCodeRegistry = state.getSourceCodeRegistry();
        final StreamFetcher streamFetcher = state.getStreamFetcher();
        final DependencyRegistry dependencyRegistry = state.getDependencyRegistry();
        final ReverseCallListCreator reverseCallListCreator = ReverseCallListCreatorFactory.newInstance(dependencyRegistry, sourceCodeRegistry, streamFetcher);

        final ArtifactIdentifier artifact = state.getArtifact();
        final ReverseCallList reverseCallList = reverseCallListCreator.createForArtifact(artifact);
        final ReverseCallList expectedReverseCallList = state.getExpectedReverseCallList();
        return new Then(reverseCallList, expectedReverseCallList);
    }
}
