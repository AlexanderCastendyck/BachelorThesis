package castendyck.artifactidentifier;


import static de.castendyck.enforcing.NotNullConstraintEnforcer.ensureNotNull;

public class ArtifactIdentifier {
    private final String groupId;
    private final String artifactId;
    private final String version;

    ArtifactIdentifier(String groupId, String artifactId, String version) {
        ensureNotNull(groupId);
        this.groupId = groupId;
        ensureNotNull(artifactId);
        this.artifactId = artifactId;
        ensureNotNull(version);
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtifactIdentifier that = (ArtifactIdentifier) o;

        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (artifactId != null ? !artifactId.equals(that.artifactId) : that.artifactId != null) return false;
        return version != null ? version.equals(that.version) : that.version == null;

    }

    @Override
    public int hashCode() {
        int result = groupId != null ? groupId.hashCode() : 0;
        result = 31 * result + (artifactId != null ? artifactId.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ArtifactIdentifier{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    public String asSimpleString() {
        final String coordinates = groupId + ":" + artifactId + ":" + version;
        return coordinates;
    }
}
