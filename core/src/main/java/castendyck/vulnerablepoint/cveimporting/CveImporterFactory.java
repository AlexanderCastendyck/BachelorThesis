package castendyck.vulnerablepoint.cveimporting;

import castendyck.vulnerablepoint.VulnerablePointsProviderConfiguration;
import castendyck.vulnerablepoint.cveimporting.internal.CveImporterImpl;

public class CveImporterFactory {
    public static CveImporter newInstance(VulnerablePointsProviderConfiguration configuration) {
        return new CveImporterImpl(configuration);
    }
}
