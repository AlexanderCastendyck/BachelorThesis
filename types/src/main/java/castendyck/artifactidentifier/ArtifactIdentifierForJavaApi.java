package castendyck.artifactidentifier;

public class ArtifactIdentifierForJavaApi extends ArtifactIdentifier{
    public static String GROUP_ID = "java";
    public static String ARTIFACT_ID = "java";
    public static String VERSION = "8.0";

    private ArtifactIdentifierForJavaApi() {
        super(GROUP_ID, ARTIFACT_ID, VERSION);
    }

    public static ArtifactIdentifier createNew(){
        return new ArtifactIdentifierForJavaApi();
    }

}
