package castendyck.vulnerablepoint;

import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.repository.LocalRepository;

import java.nio.file.Path;

public class VulnerablePointsProviderConfiguration {
    private final LocalRepository localRepository;
    private final boolean owaspSkipTestScope;
    private final Path suppressionFile;

    public VulnerablePointsProviderConfiguration(LocalRepository localRepository, boolean owaspSkipTestScope, Path suppressionFile) {
        NotNullConstraintEnforcer.ensureNotNull(localRepository);
        this.localRepository = localRepository;
        NotNullConstraintEnforcer.ensureNotNull(owaspSkipTestScope);
        this.owaspSkipTestScope = owaspSkipTestScope;
        NotNullConstraintEnforcer.ensureNotNull(suppressionFile);
        this.suppressionFile = suppressionFile;
    }

    public LocalRepository getLocalRepository() {
        return localRepository;
    }

    public boolean getOwaspSkipTestScope() {
        return owaspSkipTestScope;
    }

    public Path getSuppressionFile() {
        return suppressionFile;
    }
}
