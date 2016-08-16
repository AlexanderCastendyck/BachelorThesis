package castendyck.callgraph.functiondata;


import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import castendyck.inmemorycompiling.classes.ClassSourceCodeBuilder;

import java.util.ArrayList;
import java.util.List;

import static castendyck.inmemorycompiling.classes.NormalClassSourceCodeBuilder.sourceCodeForAClass;

class ClassStateBuilder {
    private final String className;
    private final SourceCodeStateBuilder sourceCodeStateBuilder;
    private final ArtifactIdentifier artifactIdentifier;
    private final List<FunctionStateBuilder> functions;

    public ClassStateBuilder(String name, SourceCodeStateBuilder sourceCodeStateBuilder, ArtifactIdentifier artifactIdentifier) {
        this.className = name;
        this.sourceCodeStateBuilder = sourceCodeStateBuilder;
        this.artifactIdentifier = artifactIdentifier;
        this.functions = new ArrayList<>();
    }

    public FunctionStateBuilder withAFunction(String functionName) {
        final FunctionStateBuilder functionStateBuilder = new FunctionStateBuilder(functionName, this);
        functions.add(functionStateBuilder);
        return functionStateBuilder;
    }


    public String getClassName() {
        return className;
    }

    public ArtifactIdentifier getArtifactIdentifier() {
        return artifactIdentifier;
    }

    public SourceCodeStateBuilder getSourceCodeStateBuilder() {
        return sourceCodeStateBuilder;
    }

    public List<FunctionIdentifier> getExpectedCalls() {
        final FunctionStateBuilder functionStateBuilder = functions.get(0);
        return functionStateBuilder.getExpectedCalls();
    }

    public FunctionIdentifier buildFunctionIdentifier() {
        final FunctionStateBuilder firstFunction = functions.get(0);
        final String nameOfFirstFunction = firstFunction.getFunctionName();
        final FunctionIdentifier functionIdentifier = FunctionIdentifierBuilder.aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass(className + ".class")
                .forFunction(nameOfFirstFunction)
                .build();
        return functionIdentifier;
    }

    public ClassSourceCodeBuilder build() {
        final ClassSourceCodeBuilder classSourceCodeBuilder = sourceCodeForAClass()
                .named(className);
        functions.stream().map(FunctionStateBuilder::build)
                .flatMap(List::stream)
                .forEach(classSourceCodeBuilder::withAMethod);
        return classSourceCodeBuilder;
    }
}