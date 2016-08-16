package castendyck.analyzing.analyzerchainproviding;

import castendyck.analyzing.analyzerchainproviding.internal.AnalyzerChainProviderImpl;
import castendyck.analyzing.analyzerchainproviding.source.Source;
import castendyck.analyzing.analyzerchainproviding.source.SourceRegistryProvidingException;

public class AnalyzerChainProviderFactory {
    public static AnalyzerChainProvider newInstanceFromSource(Source source) throws SourceRegistryProvidingException {
        final AnalyzerChainProviderImpl analyzerChainProvider = new AnalyzerChainProviderImpl(source);
        return analyzerChainProvider;
    }
}
