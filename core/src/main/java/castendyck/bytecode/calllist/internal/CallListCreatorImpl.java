package castendyck.bytecode.calllist.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.bytecode.calllist.CallListCreator;
import castendyck.classfile.ClassFile;
import castendyck.classfile.ClassFileFactory;
import castendyck.classidentifier.ClassIdentifier;
import castendyck.classpath.ClassPath;
import castendyck.classpath.ClassPathFactory;
import castendyck.callgraph.functiondata.CouldNotExtractCalledFunctionsOutOfClassException;
import castendyck.calllist.CallList;
import castendyck.functionCalls.FunctionCall;
import castendyck.functionCalls.FunctionCalls;
import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.callgraph.sourcecoderegistering.NoJarForThisArtifactIdentifierFoundException;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.functionidentifier.FunctionIdentifier;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class CallListCreatorImpl implements CallListCreator {
    private final static Logger logger = Logger.getLogger(CallListCreatorImpl.class);

    private final SourceCodeRegistry sourceCodeRegistry;
    private final ByteCodeHandler byteCodeHandler;

    public CallListCreatorImpl(SourceCodeRegistry sourceCodeRegistry, ByteCodeHandler byteCodeHandler) {
        this.sourceCodeRegistry = sourceCodeRegistry;
        this.byteCodeHandler = byteCodeHandler;
    }

    @Override
    public CallList createForArtifact(ArtifactIdentifier artifactIdentifier) {
        final CallList callList = new CallList();
        final List<ClassFile> classFiles = getAllClassFilesFromArtifact(artifactIdentifier);
        for (ClassFile classFile : classFiles) {
            final FunctionCalls functionCalls = findFunctionCallsOfThisClass(classFile, artifactIdentifier);
            final FunctionCalls filteredFunctionCalls = filterOutConstructors(functionCalls);
            callList.addFunctionCalls(filteredFunctionCalls);
        }
        return callList;
    }

    private FunctionCalls filterOutConstructors(FunctionCalls functionCalls) {
        final FunctionCalls filteredFunctionCalls = new FunctionCalls();

        for (FunctionCall functionCall : functionCalls) {
            if(isNotAConstructor(functionCall.getTargetFunction())){
                filteredFunctionCalls.addFunctionCalls(functionCall);
            }
        }
        return filteredFunctionCalls;
    }

    private boolean isNotAConstructor(FunctionIdentifier function) {
        final boolean isConstructor = function.getFunctionName().asString().equals("<init>");
        return !isConstructor;
    }

    private FunctionCalls findFunctionCallsOfThisClass(ClassFile classFile, ArtifactIdentifier artifactIdentifier) {
        try {
            final ClassIdentifier classIdentifier = new ClassIdentifier(classFile);
            final FunctionCalls functionCalls = byteCodeHandler.findFunctionsCallsOfClass(classIdentifier, artifactIdentifier);
            return functionCalls;
        } catch (CouldNotExtractCalledFunctionsOutOfClassException e) {
            logger.debug("CouldNotExtractCalledFunctionsOutOfClassException thrown in CallListCreatorImpl");
            logger.error("Error while trying to parse byte code of "+classFile.asString()+" in " + artifactIdentifier.asSimpleString());
            logger.error("Ignoring it. Analysis of cves using this artifact could be not correct");

            return FunctionCalls.noFunctionCalls();
        }
    }

    private List<ClassFile> getAllClassFilesFromArtifact(ArtifactIdentifier artifactIdentifier) {
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
            logger.debug("NoJarForThisArtifactIdentifierFoundException thrown in CallListCreatorImpl");
            logger.error("Could not load classes from jar file for artifact " + artifactIdentifier.asSimpleString());
            logger.error("Ignoring it. Analysis of cves using this artifact could be not correct");
            return new ArrayList<>();
        }
    }
}
