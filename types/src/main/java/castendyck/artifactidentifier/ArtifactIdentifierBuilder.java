package castendyck.artifactidentifier;

public class ArtifactIdentifierBuilder {
    private String groupId;
    private String artifactId;
    private String version;

    public ArtifactIdentifierBuilder() {
    }

    public static ArtifactIdentifierBuilder anArtifactIdentifier() {
        return new ArtifactIdentifierBuilder();
    }

    public static ArtifactIdentifier buildShort(String groupId, String artifactId, String version) {
        return anArtifactIdentifier()
                .withGroupId(groupId)
                .withArtifactId(artifactId)
                .withVersion(version)
                .build();
    }

    public ArtifactIdentifierBuilder withGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public ArtifactIdentifierBuilder withArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public ArtifactIdentifierBuilder withVersion(String version) {
        this.version = version;
        return this;
    }

    public ArtifactIdentifier build() {
        final ArtifactIdentifier artifactIdentifier = new ArtifactIdentifier(groupId, artifactId, version);
        return artifactIdentifier;
    }
 }
