package castendyck.dependencygraphing.graphcreation.internal;

import castendyck.aetherdependency.AetherLeafDependency;
import castendyck.aetherdependency.AetherRootDependency;
import castendyck.aetherdependency.AetherRootDependencyBuilder;
import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.dependency.Dependency;
import castendyck.dependency.DependencyFactory;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraph.DependencyGraphBuilder;
import castendyck.dependencygraph.GraphNode;
import castendyck.dependencygraph.GraphNodeBuilder;
import castendyck.dependencygraphing.graphcreation.DependencyGraphCreator;
import castendyck.dependencygraphing.graphcreation.GraphCreationException;
import castendyck.dependencygraphing.graphfetching.DependencyGraphFetcher;
import castendyck.dependencygraphing.graphfetching.GraphFetchingException;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileCreationException;
import castendyck.mavendependency.MavenDependency;
import castendyck.pomfilelocator.PomFileLocationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static castendyck.dependencygraph.GraphNodeBuilder.aGraphNode;

public class DependencyGraphCreatorImpl implements DependencyGraphCreator {
    private final DependencyGraphFetcher dependencyGraphFetcher;
    private final PomFile parentPom;

    public DependencyGraphCreatorImpl(DependencyGraphFetcher dependencyGraphFetcher, PomFile parentPom) {
        this.dependencyGraphFetcher = dependencyGraphFetcher;
        this.parentPom = parentPom;
    }


    @Override
    public DependencyGraph createDependencyGraphFor(PomFile pomFile) throws GraphCreationException {
        if (isMultiModuleProject(pomFile)) {
            final List<PomFile> modules = loadModulesOf(pomFile);
            final List<DependencyGraph> subModuleGraphs = fetchGraphsForSubModules(modules);
            final DependencyGraph dependencyGraph = mergeDependencyGraphs(subModuleGraphs, pomFile);
            return dependencyGraph;
        } else if (containsDependenciesToOtherModules(pomFile)) {
            final DependencyGraph dependencyGraph = fetchDependencyGraphForNormalDependencies(pomFile);
            final List<DependencyGraph> dependencyGraphs = fetchDependencyGraphsOfOtherModules(pomFile);
            return combineGraphs(dependencyGraph, dependencyGraphs);
        } else {
            return fetchGraph(pomFile);
        }
    }

    private DependencyGraph combineGraphs(DependencyGraph dependencyGraph, List<DependencyGraph> dependencyGraphsOfOtherModules) {
        final GraphNode rootNode = dependencyGraph.getRootNode();
        final Dependency dependency = rootNode.getRelatedDependency();
        final List<GraphNode> children = rootNode.getChildren();
        dependencyGraphsOfOtherModules.stream()
                .map(DependencyGraph::getRootNode)
                .forEach(children::add);

        final DependencyGraph dependencyGraphWithAllDependencies = DependencyGraphBuilder.aDependencyGraph()
                .withARootNode(GraphNodeBuilder.aGraphNode()
                        .withDependency(dependency)
                        .withChildren(children))
                .build();
        return dependencyGraphWithAllDependencies;
    }

    private List<DependencyGraph> fetchDependencyGraphsOfOtherModules(PomFile pomFile) throws GraphCreationException  {
        final List<MavenDependency> dependenciesToOtherModules = getDependenciesToOtherModules(pomFile);
        final List<DependencyGraph> dependencyGraphs = new ArrayList<>();
        for (MavenDependency dependency : dependenciesToOtherModules) {
            final PomFile pomFileOfDependency = getPomFileOfDependency(dependency, parentPom);
            final DependencyGraph dependencyGraphForDependency = createDependencyGraphFor(pomFileOfDependency);
            dependencyGraphs.add(dependencyGraphForDependency);
        }
        return dependencyGraphs;
    }

    private PomFile getPomFileOfDependency(MavenDependency dependency, PomFile pom) throws GraphCreationException {
        final ArtifactIdentifier dependencyArtifact = dependency.getArtifactIdentifier();
        final PomFile matchingPomFile = getPomFileMatchingArtifactIdentifier(pom, dependencyArtifact);
        if(matchingPomFile != null) {
            return matchingPomFile;
        }else{
            throw new GraphCreationException("Could not find PomFile for artifact : "+dependencyArtifact.asSimpleString());
        }
    }

    private PomFile getPomFileMatchingArtifactIdentifier(PomFile pom, ArtifactIdentifier dependencyArtifact) throws GraphCreationException {
        if (pom.getArtifactIdentifier().equals(dependencyArtifact)) {
            return pom;
        } else {
            final List<PomFile> modules =  loadModulesOf(pom);
            for (PomFile childPom : modules) {
                final PomFile foundPom = getPomFileMatchingArtifactIdentifier(childPom, dependencyArtifact);
                if (foundPom != null) {
                    return foundPom;
                }
            }
        }
        return null;
    }

    private DependencyGraph fetchDependencyGraphForNormalDependencies(PomFile pomFile) throws GraphCreationException {
        ArtifactIdentifier artifactIdentifier = pomFile.getArtifactIdentifier();
        final List<MavenDependency> normalDependencies = getNormalDependencies(pomFile);
        final AetherRootDependency aetherDependency = mapToAetherRootDependency(artifactIdentifier, normalDependencies);
        return fetchGraph(aetherDependency);
    }

    private boolean containsDependenciesToOtherModules(PomFile pomFile) {
        final List<MavenDependency> dependenciesToOtherModules = getDependenciesToOtherModules(pomFile);
        return dependenciesToOtherModules.size() > 0;
    }

    private List<MavenDependency> getDependenciesToOtherModules(PomFile pomFile) {
        final ArtifactIdentifier artifactIdentifier = pomFile.getArtifactIdentifier();
        final String groupId = artifactIdentifier.getGroupId();
        final List<MavenDependency> dependencies = pomFile.getDependencies();
        return dependencies.stream()
                .filter(dep -> dep.getArtifactIdentifier().getGroupId().equals(groupId))
                .collect(Collectors.toList());
    }

    private List<MavenDependency> getNormalDependencies(PomFile pomFile) {
        final ArtifactIdentifier artifactIdentifier = pomFile.getArtifactIdentifier();
        final String groupId = artifactIdentifier.getGroupId();
        final List<MavenDependency> dependencies = pomFile.getDependencies();
        final List<MavenDependency> normalDependencies = dependencies.stream()
                .filter(dep -> !dep.getArtifactIdentifier().getGroupId().equals(groupId))
                .collect(Collectors.toList());
        return normalDependencies;
    }

    private boolean isMultiModuleProject(PomFile pomFile) throws GraphCreationException {
        try {
            return pomFile.isMultiModuleProject();
        } catch (PomFileLocationException | PomFileCreationException e) {
            throw new GraphCreationException(e);
        }
    }

    private List<PomFile> loadModulesOf(PomFile pomFile) throws GraphCreationException {
        try {
            return pomFile.getModules();
        } catch (PomFileLocationException | PomFileCreationException e) {
            throw new GraphCreationException(e);
        }
    }

    private List<DependencyGraph> fetchGraphsForSubModules(List<PomFile> modules) throws GraphCreationException {
        List<DependencyGraph> graphs = new ArrayList<>();
        for (PomFile module : modules) {
            final DependencyGraph dependencyGraph = createDependencyGraphFor(module);
            graphs.add(dependencyGraph);
        }
        return graphs;
    }

    private DependencyGraph fetchGraph(PomFile pomFile) throws GraphCreationException {
        ArtifactIdentifier artifactIdentifier = pomFile.getArtifactIdentifier();
        List<MavenDependency> dependencies = pomFile.getDependencies();
        AetherRootDependency aetherDependency = mapToAetherRootDependency(artifactIdentifier, dependencies);
        return fetchGraph(aetherDependency);
    }

    private DependencyGraph fetchGraph(AetherRootDependency aetherDependency) throws GraphCreationException {
        try {
            final DependencyGraph dependencyGraph = dependencyGraphFetcher.fetchGraph(aetherDependency);
            return dependencyGraph;
        } catch (GraphFetchingException e) {
            throw new GraphCreationException(e);
        }
    }

    private AetherRootDependency mapToAetherRootDependency(ArtifactIdentifier artifactIdentifier, List<MavenDependency> dependencies) {
        List<AetherLeafDependency> aetherLeafDependencies = dependencies.stream()
                .map(this::mapToAetherLeafDependency)
                .collect(Collectors.toList());

        AetherRootDependency aetherRootDependency = AetherRootDependencyBuilder.anAetherRootDependency()
                .forArtifact(artifactIdentifier)
                .withDependencies(aetherLeafDependencies)
                .build();
        return aetherRootDependency;
    }

    private AetherLeafDependency mapToAetherLeafDependency(MavenDependency mavenDependency) {
        final ArtifactIdentifier artifactIdentifier = mavenDependency.getArtifactIdentifier();
        AetherLeafDependency dependency = AetherLeafDependency.forArtifact(artifactIdentifier);
        return dependency;
    }

    private DependencyGraph mergeDependencyGraphs(List<DependencyGraph> dependencyGraphs, PomFile pomFile) {
        final List<GraphNode> children = dependencyGraphs.stream()
                .map(DependencyGraph::getRootNode)
                .collect(Collectors.toList());

        final ArtifactIdentifier artifactIdentifier = pomFile.getArtifactIdentifier();
        Dependency dependency = DependencyFactory.dependencyFor(artifactIdentifier);

        final GraphNode newRootNode = aGraphNode()
                .withDependency(dependency)
                .withChildren(children)
                .build();

        final DependencyGraph dependencyGraph = new DependencyGraph(newRootNode);
        return dependencyGraph;
    }
}
