package castendyck.vulnerablepoint.vulnerablepointslocation;

import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.streamfetching.StreamFetcher;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpreter;
import castendyck.vulnerablepoint.cveinterpreting.internal.CveInterpreterFinalBackupImpl;
import castendyck.vulnerablepoint.cveinterpreting.internal.CveInterpreterForFunctionKeywordImpl;
import castendyck.vulnerablepoint.usedcpefinding.UsedCpeFinder;
import castendyck.vulnerablepoint.usedcpefinding.UsedCpeFinderFactory;
import castendyck.vulnerablepoint.vulnerablepointslocation.internal.VulnerablePointLocatorImpl;

import java.util.Arrays;
import java.util.List;

public class VulnerablePointsLocatorFactory {
    public static VulnerablePointLocator createDefaultOne(SourceCodeRegistry sourceCodeRegistry) {
        final UsedCpeFinder usedCpeFinder = UsedCpeFinderFactory.newInstance();
        final List<CveInterpreter> cveInterpreters = Arrays.asList(
                new CveInterpreterForFunctionKeywordImpl(usedCpeFinder)
        );

        final CveInterpreterFinalBackupImpl finalInterpreter = createFinalInterpreter(sourceCodeRegistry);
        return new VulnerablePointLocatorImpl(cveInterpreters, finalInterpreter);
    }

    private static CveInterpreterFinalBackupImpl createFinalInterpreter(SourceCodeRegistry sourceCodeRegistry) {
        final UsedCpeFinder usedCpeFinder2 = UsedCpeFinderFactory.newInstance();
        final ByteCodeHandler byteCodeHandler = new ByteCodeHandler(sourceCodeRegistry, new StreamFetcher());
        return new CveInterpreterFinalBackupImpl(usedCpeFinder2, byteCodeHandler, sourceCodeRegistry);
    }
}
