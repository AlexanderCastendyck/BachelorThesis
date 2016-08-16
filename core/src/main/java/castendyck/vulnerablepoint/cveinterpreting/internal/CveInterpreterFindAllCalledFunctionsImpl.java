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
import castendyck.cpe.CpeNameParsingException;
import castendyck.cve.CVE;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.dependencygraphing.dependencyregistry.NotRegisteredArtifactException;
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

public class CveInterpreterFindAllCalledFunctionsImpl implements CveInterpreter {
    private final static Logger logger = Logger.getLogger(CveInterpreterFindAllCalledFunctionsImpl.class);

    private final UsedCpeFinder usedCpeFinder;
    private final ByteCodeHandler byteCodeHandler;
    private final SourceCodeRegistry sourceCodeRegistry;
    private final DependencyRegistry dependencyRegistry;

    public CveInterpreterFindAllCalledFunctionsImpl(UsedCpeFinder usedCpeFinder, ByteCodeHandler byteCodeHandler, SourceCodeRegistry sourceCodeRegistry, DependencyRegistry dependencyRegistry) {
        this.usedCpeFinder = usedCpeFinder;
        this.byteCodeHandler = byteCodeHandler;
        this.sourceCodeRegistry = sourceCodeRegistry;
        this.dependencyRegistry = dependencyRegistry;
    }

    @Override
    public CveInterpretingResult interpret(CVE cve, PomFile pomFile) {
        final List<CPE> associatedCpes = cve.getCpes();
        final List<CPE> usedCpes = usedCpeFinder.findUsedCpesIn(pomFile, associatedCpes);
        final List<ArtifactIdentifier> potentialVulnerableArtifacts = mapToArtifacts(usedCpes);

        final List<VulnerablePoint> allVulnerablePoints = new ArrayList<>();
        for (ArtifactIdentifier artifact : potentialVulnerableArtifacts) {
            final List<FunctionIdentifier> callingFunctions = locateFunctionsThatAreCallingThisArtifact(artifact);
            final List<VulnerablePoint> vulnerablePointsForArtifact = mapToVulnerablePoints(callingFunctions, cve);
            allVulnerablePoints.addAll(vulnerablePointsForArtifact);
        }

        if (allVulnerablePoints.isEmpty()) {
            return new CveInterpretingResult(CveInterpretingResult.RESULT.NO_USABLE_RESULT, allVulnerablePoints);
        } else {
            return new CveInterpretingResult(CveInterpretingResult.RESULT.HAS_RESULT, allVulnerablePoints);
        }
    }

    private List<FunctionIdentifier> locateFunctionsThatAreCallingThisArtifact(ArtifactIdentifier artifact) {
        final List<ArtifactIdentifier> dependingArtifacts = getArtifactsThatDependOn(artifact);

        final List<FunctionIdentifier> callingFunctions = new ArrayList<>();
        for (ArtifactIdentifier dependingArtifact : dependingArtifacts) {
            final Set<FunctionIdentifier> callingFunctionsFromThisArtifact = findFunctionsBeingCalledByArtifact(dependingArtifact, artifact);
            callingFunctions.addAll(callingFunctionsFromThisArtifact);
        }
        return callingFunctions;
    }

    private List<ArtifactIdentifier> getArtifactsThatDependOn(ArtifactIdentifier artifact) {
        try {
            return dependencyRegistry.getArtifactsThatDependOn(artifact);
        } catch (NotRegisteredArtifactException e) {
            final String message = "CveInterpreterFindAllCalledFunctionsImpl: Could not find Artifacts, that depend on " + artifact + ". " +
                    "Therefore skipping Artifact completely.";
            logger.debug(message);
            final ArrayList<ArtifactIdentifier> emptyList = new ArrayList<>();
            return emptyList;
        }
    }

    private Set<FunctionIdentifier> findFunctionsBeingCalledByArtifact(ArtifactIdentifier dependingArtifact, ArtifactIdentifier targetArtifact) {
        final List<ClassFile> classFiles = getClassesOfArtifact(dependingArtifact);

        final Set<FunctionIdentifier> functionsBeingCalled= new HashSet<>();
        for (ClassFile classFile : classFiles) {
            final ClassIdentifier classID = new ClassIdentifier(classFile);
            final FunctionCalls functionCallsForClass = getCalledFunctionsForClass(dependingArtifact, classID);
            functionCallsForClass.stream()
                    .filter(cf -> callNotFromSameArtifact(cf, targetArtifact))
                    .filter(this::isNotAConstructorCalls)
                    .map(FunctionCall::getTargetFunction)
                    .filter(tf -> tf.getArtifactIdentifier().equals(targetArtifact))
                    .forEach(functionsBeingCalled::add);
        }
        return functionsBeingCalled;
    }

    private boolean isNotAConstructorCalls(FunctionCall functionCall) {
        final FunctionIdentifier targetFunction = functionCall.getTargetFunction();
        final FunctionName functionName = targetFunction.getFunctionName();
        final boolean isConstructor = functionName.asString().equals("<init>");
        return !isConstructor;
    }

    private boolean callNotFromSameArtifact(FunctionCall functionCall, ArtifactIdentifier sameArtifact) {
        final FunctionIdentifier callingFunction = functionCall.getCallingFunction();
        final ArtifactIdentifier callingArtifact = callingFunction.getArtifactIdentifier();
        final boolean fromSameArtifact = callingArtifact.equals(sameArtifact);
        return !fromSameArtifact;
    }


    private FunctionCalls getCalledFunctionsForClass(ArtifactIdentifier dependingArtifact, ClassIdentifier classID) {
        try {
            final FunctionCalls functionCallsForClass = byteCodeHandler.findFunctionsCallsOfClass(classID, dependingArtifact);
            return functionCallsForClass;
        } catch (CouldNotExtractCalledFunctionsOutOfClassException e) {
            final String message = "CveInterpreterFindAllCalledFunctionsImpl: Was not able to get FunctionCalls for " +
                    classID + " in " + dependingArtifact + ", because of " + e.getMessage();
            logger.debug(message);

            final FunctionCalls emptyFunctionCalls = FunctionCalls.noFunctionCalls();
            return emptyFunctionCalls;
        }
    }


    private List<ClassFile> getClassesOfArtifact(ArtifactIdentifier artifactIdentifier) {
        try {

            final JarFile jarFile = sourceCodeRegistry.getJarFile(artifactIdentifier);
            final List<ClassFile> classFiles = jarFile.stream()
                    .map(JarEntry::getName)
                    .map(ClassPathFactory::createNew)
                    .filter(ClassPath::isClassFile)
                    .map(c -> ClassFileFactory.createNew(c.asString()))
                    .collect(Collectors.toList());
            return classFiles;
        } catch (NoJarForThisArtifactIdentifierFoundException e) {
            final String message = "CveInterpreterFindAllCalledFunctionsImpl: Could not obtain jarFile for " + artifactIdentifier + ". " +
                    "Skipping it.";
            logger.debug(message);
            final ArrayList<ClassFile> emptyList = new ArrayList<>();
            return emptyList;
        }
    }

    private List<ArtifactIdentifier> mapToArtifacts(List<CPE> usedCpes) {
        final List<ArtifactIdentifier> mappedArtifactIdentifier = new ArrayList<>();
        for (CPE cpe : usedCpes) {
            try {
                final ArtifactIdentifier artifactIdentifier = CpeToArtifactConverter.convertToArtifact(cpe);
                mappedArtifactIdentifier.add(artifactIdentifier);
            } catch (CpeNameParsingException e) {
                logger.debug("CveInterpreterFindAllCalledFunctionsImpl: Skipped cpe " + cpe.getValue() + ", " +
                        "because mapping failed due to CpeNameParsingException with message " + e.getMessage());
            }
        }
        return mappedArtifactIdentifier;
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
}
