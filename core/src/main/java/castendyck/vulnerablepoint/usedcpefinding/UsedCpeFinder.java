package castendyck.vulnerablepoint.usedcpefinding;

import castendyck.cpe.CPE;
import castendyck.maven.pomfile.PomFile;

import java.util.List;

public interface UsedCpeFinder {
    List<CPE> findUsedCpesIn(PomFile pomFile, List<CPE> cpes);
}
