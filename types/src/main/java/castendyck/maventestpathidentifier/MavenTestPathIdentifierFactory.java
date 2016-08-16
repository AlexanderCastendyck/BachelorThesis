package castendyck.maventestpathidentifier;

public class MavenTestPathIdentifierFactory {

    public static MavenTestPathIdentifier createNew(String identifier){
        if(identifier.startsWith("/")){
            identifier = identifier.substring(1);
        }
        if(!identifier.endsWith("/")){
            identifier = identifier + "/";
        }

        final MavenTestPathIdentifier mavenTestPathIdentifier = MavenTestPathIdentifier.parse(identifier);
        return mavenTestPathIdentifier;
    }
}
