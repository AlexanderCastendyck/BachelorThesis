package castendyck.vulnerablepoint.vulnerablepointslocation;

import castendyck.cve.CVE;
import castendyck.maven.pomfile.PomFile;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;

public interface VulnerablePointLocator {

    List<VulnerablePoint> locateVulnerablePoints(List<CVE> cves, PomFile pomFile);
}
