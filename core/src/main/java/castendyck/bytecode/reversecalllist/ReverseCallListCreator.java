package castendyck.bytecode.reversecalllist;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.reversecalllist.ReverseCallList;

public interface ReverseCallListCreator {

    ReverseCallList createForArtifact(ArtifactIdentifier artifactIdentifier);

}
