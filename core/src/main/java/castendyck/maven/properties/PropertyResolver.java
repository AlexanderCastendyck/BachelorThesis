package castendyck.maven.properties;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.mavenproperty.MavenProperties;
import castendyck.maven.pomfile.PomFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PropertyResolver {
    private static final Pattern WRAPPING_PATTERN= Pattern.compile("^\\$\\{(?<inner>[.-{}$]+)\\}$");

    public static String resolveProperty(String propertyName, PomFile pomFile) {
        String propertyToResolve = propertyName;
        if(isWrapped(propertyName)){
            propertyToResolve = unwrap(propertyName);
        }
        String resolvedProperty = resolve(propertyToResolve, pomFile);
        while(needsToBeResolved(resolvedProperty)){
            final String unwrappedPropertyName = unwrap(resolvedProperty);
            resolvedProperty = resolve(unwrappedPropertyName, pomFile);
        }
        return resolvedProperty;
    }

    private static boolean isWrapped(String propertyName) {
        final Matcher matcher = WRAPPING_PATTERN.matcher(propertyName);
        final boolean matches = matcher.matches();
        return matches;
    }

    private static String unwrap(String propertyName) {
        final Matcher matcher = WRAPPING_PATTERN.matcher(propertyName);
        if(matcher.matches()){
            final String unwrappedPropertyName = matcher.group("inner");
            return unwrappedPropertyName;
        }else{
            return propertyName;
        }
    }

    public static boolean needsToBeResolved(String propertyName) {
        return isWrapped(propertyName);
    }

    private static String resolve(String propertyName, PomFile pomFile) {
        final MavenProperties properties = pomFile.getProperties();
        if (properties.containsProperty(propertyName)) {
            return properties.getPropertyValue(propertyName);
        }

        if (pomFile.hasParent()) {
            PomFile parentPom = pomFile.getParent();
            return resolveProperty(propertyName, parentPom);
        }

        final ArtifactIdentifier artifactIdentifier = pomFile.getArtifactIdentifier();
        final String artifactId = artifactIdentifier.getArtifactId();
        throw new PropertyNotFoundException("Could not find property " + propertyName + " in Pom of " + artifactId + " or in its parent poms");
    }
}
