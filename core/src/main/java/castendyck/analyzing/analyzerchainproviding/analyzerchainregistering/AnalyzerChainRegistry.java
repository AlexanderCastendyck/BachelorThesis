package castendyck.analyzing.analyzerchainproviding.analyzerchainregistering;

import castendyck.analyzing.analyzerchain.AnalyzerChain;
import castendyck.cwe.CWE;

public interface AnalyzerChainRegistry {

    AnalyzerChain getChainFor(CWE cwe);
}
