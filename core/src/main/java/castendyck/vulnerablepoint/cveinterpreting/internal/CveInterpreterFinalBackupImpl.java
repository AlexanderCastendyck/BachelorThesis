package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.classfile.ClassFile;
import castendyck.classfile.ClassFileFactory;
import castendyck.classidentifier.ClassIdentifier;
import castendyck.classpath.ClassPath;
import castendyck.classpath.ClassPathFactory;
import castendyck.callgraph.functiondata.CouldNotExtractCalledFunctionsOutOfClassException;
import castendyck.functionCalls.FunctionCall;
import castendyck.functionCalls.FunctionCalls;
import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.callgraph.sourcecoderegistering.NoJarForThisArtifactIdentifierFoundException;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.cpe.CPE;
import castendyck.cve.CVE;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionname.FunctionName;
import castendyck.maven.pomfile.PomFile;
import castendyck.vulnerablepoint.VulnerablePoint;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpreter;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpretingResult;
import castendyck.vulnerablepoint.usedcpefinding.UsedCpeFinder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static castendyck.vulnerablepoint.VulnerablePointBuilder.aVulnerablePoint;
import static castendyck.vulnerablepoint.cveinterpreting.internal.CpeToArtifactConverter.convertToArtifactsWhileSkippingUnmappableOnes;

public class CveInterpreterFinalBackupImpl implements CveInterpreter {
    private final static Logger logger = Logger.getLogger(CveInterpreterFinalBackupImpl.class);

    private final UsedCpeFinder usedCpeFinder;
    private final ByteCodeHandler byteCodeHandler;
    private final SourceCodeRegistry sourceCodeRegistry;

    public CveInterpreterFinalBackupImpl(UsedCpeFinder usedCpeFinder, ByteCodeHandler byteCodeHandler, SourceCodeRegistry sourceCodeRegistry) {
        this.usedCpeFinder = usedCpeFinder;
        this.byteCodeHandler = byteCodeHandler;
        this.sourceCodeRegistry = sourceCodeRegistry;
    }

    @Override
    public CveInterpretingResult interpret(CVE cve, PomFile pomFile) {
        final List<CPE> associatedCpes = cve.getCpes();
        final List<CPE> usedCpes = usedCpeFinder.findUsedCpesIn(pomFile, associatedCpes);
        final List<ArtifactIdentifier> potentialVulnerableArtifacts = convertToArtifactsWhileSkippingUnmappableOnes(usedCpes);

        final List<VulnerablePoint> allVulnerablePoints = new ArrayList<>();
        for (ArtifactIdentifier artifact : potentialVulnerableArtifacts) {
            final List<FunctionIdentifier> exportedFunctions = locateVulnerableFunctionsOfArtifact(artifact);
            final List<VulnerablePoint> vulnerablePointsForArtifact = mapToVulnerablePoints(exportedFunctions, cve);
            allVulnerablePoints.addAll(vulnerablePointsForArtifact);
        }

        return new CveInterpretingResult(CveInterpretingResult.RESULT.HAS_RESULT, allVulnerablePoints);
    }

    private List<FunctionIdentifier> locateVulnerableFunctionsOfArtifact(ArtifactIdentifier artifact) {
        try {
            final List<ClassFile> classFiles = getClassesOfArtifact(artifact);
            final NotCalledMethodCollector collector = storeCallsOfArtifactInCollector(artifact, classFiles);
            final List<FunctionIdentifier> exportedFunctions = collector.getNotCalledFunctions();
            return exportedFunctions;
        } catch (NoJarForThisArtifactIdentifierFoundException | CouldNotExtractCalledFunctionsOutOfClassException e) {
            final String message = "CveInterpreterFinalBackupImpl: Failed to find vulnerable FunctionIdentifier for " + artifact.asSimpleString() + ", "
                    + "because " + e.getClass() + " was thrown with message " + e.getMessage();
            logger.debug(message);
            return new ArrayList<>();
        }
    }

    private List<ClassFile> getClassesOfArtifact(ArtifactIdentifier artifactIdentifier) throws NoJarForThisArtifactIdentifierFoundException {
        final JarFile jarFile = sourceCodeRegistry.getJarFile(artifactIdentifier);
        final List<ClassFile> classFiles = jarFile.stream()
                .map(JarEntry::getName)
                .map(ClassPathFactory::createNew)
                .filter(ClassPath::isClassFile)
                .map(c -> ClassFileFactory.createNew(c.asString()))
                .collect(Collectors.toList());
        return classFiles;
    }

    private NotCalledMethodCollector storeCallsOfArtifactInCollector(ArtifactIdentifier artifactIdentifier, List<ClassFile> classFiles) throws CouldNotExtractCalledFunctionsOutOfClassException {
        final NotCalledMethodCollector collector = new NotCalledMethodCollector();
        for (ClassFile classFile : classFiles) {
            final ClassIdentifier classIdentifier = new ClassIdentifier(classFile);
            final FunctionCalls functionsCallsOfClass = byteCodeHandler.findFunctionsCallsOfClass(classIdentifier, artifactIdentifier);
            for (FunctionCall functionCall : functionsCallsOfClass) {
                collector.processCall(functionCall);
            }
        }
        return collector;
    }

    private List<VulnerablePoint> mapToVulnerablePoints(List<FunctionIdentifier> functions, CVE cve) {
        final List<VulnerablePoint> vulnerablePoints = functions.stream()
                .map(f -> aVulnerablePoint()
                        .withFunctionIdentifier(f)
                        .forCve(cve)
                        .build())
                .collect(Collectors.toList());
        return vulnerablePoints;
    }

    private class NotCalledMethodCollector {
        private final Set<FunctionIdentifier> allFunctions;
        private final Set<FunctionIdentifier> calledFunctions;

        private NotCalledMethodCollector() {
            this.allFunctions = new HashSet<>();
            this.calledFunctions = new HashSet<>();
        }

        public void processCall(FunctionCall functionCall) {
            final FunctionIdentifier targetFunction = functionCall.getTargetFunction();
            calledFunctions.add(targetFunction);
            allFunctions.add(targetFunction);

            final FunctionIdentifier callingFunction = functionCall.getCallingFunction();
            if (isNotConstructor(callingFunction)) {
                allFunctions.add(callingFunction);
            }
        }

        private boolean isNotConstructor(FunctionIdentifier callingFunction) {
            final FunctionName functionName = callingFunction.getFunctionName();
            final String name = functionName.asString();
            return !name.equals("<init>");
        }

        public List<FunctionIdentifier> getNotCalledFunctions() {
            final ArrayList<FunctionIdentifier> notCalledFunctions = new ArrayList<>(allFunctions);
            notCalledFunctions.removeAll(calledFunctions);
            return notCalledFunctions;
        }
    }
}
