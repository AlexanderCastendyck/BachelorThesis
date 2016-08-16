package castendyck.vulnerablepoint;

import castendyck.vulnerablepoint.cveimporting.CveImporter;
import castendyck.vulnerablepoint.cveimporting.CveImporterFactory;
import castendyck.vulnerablepoint.internal.VulnerablePointsProviderImpl;

public class VulnerablePointsProviderFactory {

    public static VulnerablePointsProvider newInstance(VulnerablePointsProviderConfiguration configuration) {
        final CveImporter cveImporter = CveImporterFactory.newInstance(configuration);
        return new VulnerablePointsProviderImpl(cveImporter, configuration);
    }
}
