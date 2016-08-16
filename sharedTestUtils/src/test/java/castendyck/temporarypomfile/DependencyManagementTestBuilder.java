package castendyck.temporarypomfile;

import java.util.ArrayList;
import java.util.List;

public class DependencyManagementTestBuilder {
    private final List<DependencyInformation> dependencyInformation = new ArrayList<>();

    public static DependencyManagementTestBuilder aDependencyManagement() {
        return new DependencyManagementTestBuilder();
    }

    public DependencyManagementTestBuilder withAManagedDependency(String groupId, String artifactId) {
        final DependencyInformation dependencyInformation = new DependencyInformation(groupId, artifactId);
        this.dependencyInformation.add(dependencyInformation);
        return this;
    }

    public DependencyManagementTestBuilder withAManagedDependency(String groupId, String artifactId, String  version) {
        final DependencyInformation dependencyInformation = new DependencyInformation(groupId, artifactId, version);
        this.dependencyInformation.add(dependencyInformation);
        return this;
    }

    public void writeToStringBuilder(StringBuilder stringBuilder) {
        stringBuilder.append("<dependencyManagement>");
        if(!dependencyInformation.isEmpty()) {
            stringBuilder.append("<dependencies>");
            for (DependencyInformation dependencyInformation : this.dependencyInformation) {
                dependencyInformation.writeToStringBuilder(stringBuilder);
            }
            stringBuilder.append("</dependencies>");
        }
        stringBuilder.append("</dependencyManagement>");
    }


    private class DependencyInformation {
        private String groupId;
        private String artifactId;
        private String version;


        public DependencyInformation(String groupId, String artifactId) {
            this.groupId = groupId;
            this.artifactId = artifactId;
        }

        public DependencyInformation(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }

        public void writeToStringBuilder(StringBuilder stringBuilder) {
            stringBuilder.append("<dependency>");
            if(groupId != null){
                stringBuilder.append("<groupId>").append(groupId).append("</groupId>");
            }
            if(artifactId != null){
                stringBuilder.append("<artifactId>").append(artifactId).append("</artifactId>");
            }
            if(version != null){
                stringBuilder.append("<version>").append(version).append("</version>");
            }
            stringBuilder.append("</dependency>");
        }
    }
}
