package castendyck.dependencygraphing;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.fail;

public class DependencyGraphProviderTestIT {
    @Test
    public void pomFileWithoutDependencies() throws Exception {
        Given.given(DependencyStateBuilder.aPomFileWithoutDependencies())
                .when().graphIsCreated()
                .thenGraphShouldContainOnlyOneRootNode();
    }

    @Test
    public void pomFileWithTwoDirectDependencies() throws Exception {
        Given.given(DependencyStateBuilder.aPomFileWithTwoDirectDependencies())
                .when().graphIsCreated()
                .thenGraphShouldContainRootNodeAndTwoChildren();
    }

    @Test
    public void pomFileWithOneDirectAndOneTransitiveDependency() throws Exception {
        Given.given(DependencyStateBuilder.aPomFileWithOneDirectAndOneTransitiveDependency())
                .when().graphIsCreated()
                .thenGraphShouldContainRootNodeOneDirectChildAndOneChildChild();
    }

    @Test
    public void pomFileWithSeveralDependencies() throws Exception {
        Given.given(DependencyStateBuilder.aPomFileWithSeveralDependencies())
                .when().graphIsCreated()
                .thenGraphShouldContainAllNodesCorrectly();
    }

    @Test
    public void pomFileWithDuplicateDependencyButDifferentVersions() throws Exception {
        Given.given(DependencyStateBuilder.aPomFileWithDuplicateDependencyButDifferentVersions())
                .when().graphIsCreated()
                .thenGraphShouldContainBothDependenciesAsSingleNodes();
    }

    @Test
    public void pomFileWithSubModules() throws Exception {
        Given.given(DependencyStateBuilder.aPomFileWithSubModules())
                .when().graphIsCreated()
                .thenGraphShouldContainOneGraphWithAllSubModulesAndTheirDependencies();
    }

    @Test
    public void pomFileWithSubModulesThatHaveSubModules() throws Exception {
        Given.given(DependencyStateBuilder.aPomFileWithSubModulesThatHaveSubModules())
                .when().graphIsCreated()
                .thenGraphShouldContainOneGraphWithAllSubModulesAndTheirSubModules();
    }

    @Test
    public void pomFileWithSubModulesThatHaveEachOtherAsDependency() throws Exception {
        Given.given(DependencyStateBuilder.aPomFileWithSubModulesThatHaveEachOtherAsDependency())
                .when().graphIsCreated()
                .thenGraphShouldContainOneGraphWithAllSubModulesAndTheirDependenciesAndCrossReferencedOnes();
    }

    @Test
    public void pomFileWithSubModuleThatHasDependencyToOtherModuleWhichHasSubModules() throws Exception {
        Given.given(DependencyStateBuilder.aPomFileWithSubModuleThatHasDependencyToOtherModuleWhichHasSubModules())
                .when().graphIsCreated()
                .thenGraphShouldContainOneGraphWithAllSubModulesAndTheirDependenciesAndCrossReferencedOnesAndTheirSubModules();
    }

    @Test
    public void pomFileWithTestDependencies() throws Exception {
        Given.given(DependencyStateBuilder.aPomFileWithTestDependencies())
                .when().graphIsCreated()
                .thenGraphShouldContainTestDependencies();
    }
}
