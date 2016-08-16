package castendyck.analyzing.analyzerchainproviding.internal;

import castendyck.analyzing.analyzerchain.AnalyzerChain;
import castendyck.analyzing.analyzerchainproviding.AnalyzerChainProvider;
import castendyck.analyzing.analyzerchainproviding.analyzerchainregistering.AnalyzerChainRegistry;
import castendyck.analyzing.analyzerchainproviding.source.Source;
import castendyck.analyzing.analyzerchainproviding.source.SourceRegistryProvidingException;
import castendyck.cve.CVE;
import castendyck.cwe.CWE;

public class AnalyzerChainProviderImpl implements AnalyzerChainProvider {
    private final AnalyzerChainRegistry analyzerChainRegistry;

    public AnalyzerChainProviderImpl(Source source) throws SourceRegistryProvidingException {
        analyzerChainRegistry = source.provideRegistry();
    }

    @Override
    public AnalyzerChain providerAnalyzerChainFor(CVE cve) {
        final CWE cwe = cve.getCwe();
        return analyzerChainRegistry.getChainFor(cwe);
    }
}
