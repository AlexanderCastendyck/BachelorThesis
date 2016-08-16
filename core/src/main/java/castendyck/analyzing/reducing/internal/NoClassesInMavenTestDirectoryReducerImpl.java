package castendyck.analyzing.reducing.internal;

import castendyck.analyzing.reducing.Reducer;
import castendyck.analyzing.reducing.ReducerConfiguration;
import castendyck.callgraph.CallPath;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.javasourcefile.JavaSourceFile;
import castendyck.maventestpathidentifier.MavenTestPathIdentifier;
import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;
import castendyck.reduction.ReductionBuilder;
import castendyck.sourcecode.sourecodefilecollecting.TestClassesCollector;
import castendyck.sourcecode.sourecodefilecollecting.TestClassesCollectorFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static castendyck.reduction.ReductionBuilder.aReduction;

public class NoClassesInMavenTestDirectoryReducerImpl implements Reducer {
    private final TestClassesCollector testClassesCollector;
    private final Path startingPath;
    private List<JavaSourceFile> classesInTestDirectory;

    public NoClassesInMavenTestDirectoryReducerImpl(ReducerConfiguration reducerConfiguration) {
        this.startingPath = reducerConfiguration.getProjectRootPath();
        final MavenTestPathIdentifier mavenTestPathPart = reducerConfiguration.getGeneralMavenTestPathPart();
        final Path testRootDir = startingPath.resolve(mavenTestPathPart.asValue());
        this.testClassesCollector = TestClassesCollectorFactory.newInstanceWithCustomTestPath(testRootDir);
    }

    @Override
    public Reduction reduceFurther(Reduction previousReduction, ProcessedCve processedCve) {
        classesInTestDirectory = testClassesCollector.collectFilesFromMavenTestRepository(startingPath);

        final List<CallPath> remainingCallPaths = previousReduction.getRemainingCallPaths();
        final List<CallPath> reducedCallPaths = remainingCallPaths.stream()
                .filter(this::doesNotEndInTestDirectory)
                .collect(Collectors.toList());

        final String reason = getReason();
        final Reduction reduction = aReduction()
                .containing(reducedCallPaths)
                .withReason(reason)
                .build();
        return reduction;
    }

    private boolean doesNotEndInTestDirectory(CallPath callPath) {
        final FunctionIdentifier functionIdentifier = callPath.getCurrentFunctionIdentifier();
        return !functionLocatedInATestClass(functionIdentifier);
    }

    private boolean functionLocatedInATestClass(FunctionIdentifier functionIdentifier) {
        final FunctionIdentifierJavaSourceFileMatcher matcher = FunctionIdentifierJavaSourceFileMatcher.aMatcherForFunctionIdentifier(functionIdentifier);
        for (JavaSourceFile javaSourceFile : classesInTestDirectory) {
            if(matcher.matches(javaSourceFile)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String getReason() {
        return "vulnerability only used in test code";
    }
}
