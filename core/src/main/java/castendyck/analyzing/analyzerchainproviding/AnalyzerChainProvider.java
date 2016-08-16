package castendyck.analyzing.analyzerchainproviding;

import castendyck.analyzing.analyzerchain.AnalyzerChain;
import castendyck.cve.CVE;

public interface AnalyzerChainProvider {

    AnalyzerChain providerAnalyzerChainFor(CVE cve);

}
