package castendyck.bytecode.calllist;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.calllist.CallList;

public interface CallListCreator {

    CallList createForArtifact(ArtifactIdentifier artifactIdentifier);
}
