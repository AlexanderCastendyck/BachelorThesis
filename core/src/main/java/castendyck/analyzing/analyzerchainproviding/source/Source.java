package castendyck.analyzing.analyzerchainproviding.source;

import castendyck.analyzing.analyzerchainproviding.analyzerchainregistering.AnalyzerChainRegistry;

public interface Source {

    AnalyzerChainRegistry provideRegistry() throws SourceRegistryProvidingException;
}
