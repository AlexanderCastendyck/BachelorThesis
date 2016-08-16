package castendyck.maven.pomfile.internal;

import castendyck.mavenproperty.MavenProperties;
import castendyck.mavenproperty.MavenPropertyBuilder;
import org.apache.maven.model.Model;

import java.util.Map;
import java.util.Properties;

public class ModelToMavenPropertiesConverter {
    public static MavenProperties convertToProperties(Model model) {
        final MavenPropertyBuilder properties = MavenPropertyBuilder.createProperties();
        fillInMandatoryProjectProperties(properties, model);

        final Properties userDefinedProperties = model.getProperties();
        fillInUserDefinedProperties(properties, userDefinedProperties);

        final MavenProperties mavenProperties = properties.build();
        return mavenProperties;
    }

    private static void fillInMandatoryProjectProperties(MavenPropertyBuilder properties, Model model) {
        properties.withAProperty("project.groupId", model.getGroupId());
        properties.withAProperty("project.artifactId", model.getArtifactId());
        properties.withAProperty("project.version", model.getVersion());
        properties.withAProperty("project.description", model.getDescription());
        properties.withAProperty("project.name", model.getName());
        final String basedir = model.getProjectDirectory() != null ? model.getProjectDirectory().getAbsolutePath(): "";
        properties.withAProperty("project.basedir", basedir);
        properties.withAProperty("project.inceptionYear", model.getInceptionYear());
        properties.withAProperty("project.modelVersion", model.getModelVersion());
        final String packaging = model.getPackaging() != null ? model.getPackaging() : "jar";
        properties.withAProperty("project.packaging", packaging);
        properties.withAProperty("project.url", model.getUrl());

        if (model.getParent() != null) {
            properties.withAProperty("project.parent.groupId", model.getParent().getGroupId());
            properties.withAProperty("project.parent.artifactId", model.getParent().getArtifactId());
            properties.withAProperty("project.parent.version", model.getParent().getVersion());
            properties.withAProperty("project.parent.relativePath", model.getParent().getRelativePath());
        }

        if (model.getBuild() != null) {
            properties.withAProperty("project.build.sourceDirectory", model.getBuild().getSourceDirectory());
            properties.withAProperty("project.build.outputDirectory", model.getBuild().getOutputDirectory());
            properties.withAProperty("project.build.scriptSourceDirectory", model.getBuild().getScriptSourceDirectory());
            properties.withAProperty("project.build.testSourceDirectory", model.getBuild().getTestSourceDirectory());
            properties.withAProperty("project.build.testOutputDirectory", model.getBuild().getTestOutputDirectory());
            properties.withAProperty("project.build.directory", model.getBuild().getDirectory());
            properties.withAProperty("project.build.finalName", model.getBuild().getFinalName());
            properties.withAProperty("project.build.defaultGoal", model.getBuild().getDefaultGoal());
        }

        if (model.getDistributionManagement() != null) {
            properties.withAProperty("project.distributionManagement.downloadUrl", model.getDistributionManagement().getDownloadUrl());
            properties.withAProperty("project.distributionManagement.status", model.getDistributionManagement().getStatus());

            if (model.getDistributionManagement().getRelocation() != null) {
                properties.withAProperty("project.distributionManagement.relocation.artifactId", model.getDistributionManagement().getRelocation().getArtifactId());
                properties.withAProperty("project.distributionManagement.relocation.groupId", model.getDistributionManagement().getRelocation().getGroupId());
                properties.withAProperty("project.distributionManagement.relocation.version", model.getDistributionManagement().getRelocation().getVersion());
                properties.withAProperty("project.distributionManagement.relocation.message", model.getDistributionManagement().getRelocation().getMessage());
            }
            if (model.getDistributionManagement().getRepository() != null) {
                properties.withAProperty("project.distributionManagement.repository.url", model.getDistributionManagement().getRepository().getUrl());
                properties.withAProperty("project.distributionManagement.repository.name", model.getDistributionManagement().getRepository().getName());
                properties.withAProperty("project.distributionManagement.repository.layout", model.getDistributionManagement().getRepository().getLayout());
                properties.withAProperty("project.distributionManagement.repository.id", model.getDistributionManagement().getRepository().getId());
            }
            if (model.getDistributionManagement().getSite() != null) {
                properties.withAProperty("project.distributionManagement.site.id", model.getDistributionManagement().getSite().getId());
                properties.withAProperty("project.distributionManagement.site.url", model.getDistributionManagement().getSite().getUrl());
                properties.withAProperty("project.distributionManagement.site.name", model.getDistributionManagement().getSite().getName());
            }
            if (model.getDistributionManagement().getSnapshotRepository() != null) {
                properties.withAProperty("project.distributionManagement.snapshotRepository.name", model.getDistributionManagement().getSnapshotRepository().getName());
                properties.withAProperty("project.distributionManagement.snapshotRepository.id", model.getDistributionManagement().getSnapshotRepository().getId());
                properties.withAProperty("project.distributionManagement.snapshotRepository.url", model.getDistributionManagement().getSnapshotRepository().getUrl());
                properties.withAProperty("project.distributionManagement.snapshotRepository.layout", model.getDistributionManagement().getSnapshotRepository().getLayout());
            }
        }

        if (model.getScm() != null) {
            properties.withAProperty("project.scm.tag", model.getScm().getTag());
            properties.withAProperty("project.scm.url", model.getScm().getUrl());
            properties.withAProperty("project.scm.connection", model.getScm().getConnection());
            properties.withAProperty("project.scm.developerConnection", model.getScm().getDeveloperConnection());
        }

        if (model.getReporting() != null) {
            properties.withAProperty("project.reporting.outputDirectory", model.getReporting().getOutputDirectory());
            properties.withAProperty("project.reporting.excludeDefaults", model.getReporting().getExcludeDefaults());
        }

        if (model.getCiManagement() != null) {
            properties.withAProperty("project.ciManagement.url", model.getCiManagement().getUrl());
            properties.withAProperty("project.ciManagement.system", model.getCiManagement().getSystem());
        }

        if (model.getIssueManagement() != null) {
            properties.withAProperty("project.issueManagement.url", model.getIssueManagement().getUrl());
            properties.withAProperty("project.issueManagement.system", model.getIssueManagement().getSystem());
        }

        if (model.getOrganization() != null) {
            properties.withAProperty("project.organization.url", model.getOrganization().getUrl());
            properties.withAProperty("project.organization.name", model.getOrganization().getName());
        }

        if (model.getPrerequisites() != null) {
            properties.withAProperty("project.prerequisites.maven", model.getPrerequisites().getMaven());
        }

        // not Translated:
        // scm, reporting, ciManagement, dependencyManagement, prerequisites, parent, issueManagement, organization
        // build, build.pluginManagement,
        // distributionManagement, distributionManagement.site, distributionManagement.relocation,
        // distributionManagement.repository, distributionManagement.repository.uniqueVersion
        // distributionManagement.snapshotRepository, distributionManagement.snapshotRepository.uniqueVersion
    }

    private static void fillInUserDefinedProperties(MavenPropertyBuilder propertyBuilder, Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            final String propertyName = (String) entry.getKey();
            final String propertyValue = (String) entry.getValue();

            propertyBuilder.withAProperty(propertyName, propertyValue);
        }
    }
}
