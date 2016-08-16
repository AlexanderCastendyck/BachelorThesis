package castendyck.mavenproperty;

import de.castendyck.enforcing.NotNullConstraintEnforcer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MavenProperties {
    private final static List<String> notSupportedProperties = new ArrayList<>();
    private final Map<String, String> properties;

    static {
        // The following properties are not supported, because they are used only to group properties logically.
        // Maven doesn't allow them either
        notSupportedProperties.add("project");
        notSupportedProperties.add("project.scm");
        notSupportedProperties.add("project.reporting");
        notSupportedProperties.add("project.ciManagement");
        notSupportedProperties.add("project.dependencyManagement");
        notSupportedProperties.add("project.prerequisites");
        notSupportedProperties.add("project.parent");
        notSupportedProperties.add("project.issueManagement");
        notSupportedProperties.add("project.organization");
        notSupportedProperties.add("project.build");
        notSupportedProperties.add("project.build.pluginManagement");
        notSupportedProperties.add("project.distributionManagement");
        notSupportedProperties.add("project.distributionManagement.site");
        notSupportedProperties.add("project.distributionManagement.relocation");
        notSupportedProperties.add("project.distributionManagement.repository");
        notSupportedProperties.add("project.distributionManagement.repository.uniqueVersion");
        notSupportedProperties.add("project.distributionManagement.snapshotRepository");
        notSupportedProperties.add("project.distributionManagement.snapshotRepository.uniqueVersion");
    }

    MavenProperties(Map<String, String> properties) {
        NotNullConstraintEnforcer.ensureNotNull(properties);
        this.properties = properties;
    }

    public String getPropertyValue(String propertyName){
        ensureIsSupported(propertyName);
        if(containsProperty(propertyName)){
            return properties.get(propertyName);
        }else{
            throw new PropertyNotKnownException(propertyName);
        }
    }

    private void ensureIsSupported(String propertyName) {
        if(notSupportedProperties.contains(propertyName)){
            throw new NotSupportedPropertyException("Not supported property in pom.xml found: "+propertyName);
        }
    }


    public boolean containsProperty(String propertyName){
        ensureIsSupported(propertyName);
        if(!properties.containsKey(propertyName)){
            return false;
        }
        final String propertyValue = properties.get(propertyName);
        return propertyValue != null && !propertyValue.isEmpty();
    }
}
