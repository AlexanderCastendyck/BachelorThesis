package castendyck.analyzing.analyzerchainproviding.analyzerchainregistering.internal;

import castendyck.analyzing.analyzerchain.AnalyzerChain;
import castendyck.analyzing.analyzerchainproviding.analyzerchainregistering.AnalyzerChainRegistry;
import castendyck.cwe.CWE;
import castendyck.cwe.CweType;
import de.castendyck.enforcing.NotNullConstraintEnforcer;

import java.util.Map;

public class SimpleAnalyzerChainRegistryImpl implements AnalyzerChainRegistry {
    private final Map<CweType, AnalyzerChain> mapping;
    private final AnalyzerChain defaultChain;

    public SimpleAnalyzerChainRegistryImpl(Map<CweType, AnalyzerChain> mapping, AnalyzerChain defaultChain) {
        NotNullConstraintEnforcer.ensureNotNull(mapping);
        this.mapping = mapping;
        NotNullConstraintEnforcer.ensureNotNull(defaultChain);
        this.defaultChain = defaultChain;
    }

    @Override
    public AnalyzerChain getChainFor(CWE cwe) {
        final CweType cweType = cwe.getType();
        if (mapping.containsKey(cweType)) {
            return mapping.get(cweType);
        } else {
            return defaultChain;
        }
    }
}
