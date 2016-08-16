package castendyck.vulnerablepoint.vulnerablepointslocation.internal;

import castendyck.cve.CVE;
import castendyck.maven.pomfile.PomFile;
import de.castendyck.utils.ThisShouldNeverHappenException;
import castendyck.vulnerablepoint.VulnerablePoint;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpreter;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpretingResult;
import castendyck.vulnerablepoint.vulnerablepointslocation.VulnerablePointLocator;

import java.util.ArrayList;
import java.util.List;

public class VulnerablePointLocatorImpl implements VulnerablePointLocator {
    private final List<CveInterpreter> cveInterpreters;
    private final CveInterpreter finalInterpreter;

    public VulnerablePointLocatorImpl(List<CveInterpreter> cveInterpreters, CveInterpreter finalInterpreter) {
        this.cveInterpreters = cveInterpreters;
        this.finalInterpreter = finalInterpreter;
    }

    @Override
    public List<VulnerablePoint> locateVulnerablePoints(List<CVE> cves, PomFile pomFile) {
        final List<VulnerablePoint> vulnerablePoints = new ArrayList<>();
        for (CVE cve : cves) {
            final List<VulnerablePoint> vulnerablePointsOfCve = applyInterpreters(cve, pomFile);
            vulnerablePoints.addAll(vulnerablePointsOfCve);
        }
        return vulnerablePoints;
    }

    private List<VulnerablePoint> applyInterpreters(CVE cve, PomFile pomFile) {
        for (CveInterpreter cveInterpreter : cveInterpreters) {
            final CveInterpretingResult interpretingResult = cveInterpreter.interpret(cve, pomFile);
            if(interpretingResult.gotAResult()){
                final List<VulnerablePoint> vulnerablePoints = interpretingResult.getVulnerablePoints();
                return vulnerablePoints;
            }
        }

        final List<VulnerablePoint> vulnerablePoints = applyFinalInterpreterAsBackup(cve, pomFile);
        return vulnerablePoints;
    }

    private List<VulnerablePoint> applyFinalInterpreterAsBackup(CVE cve, PomFile pomFile) {
        final CveInterpretingResult interpretingResult = finalInterpreter.interpret(cve, pomFile);
        if(interpretingResult.gotAResult()) {
            final List<VulnerablePoint> vulnerablePoints = interpretingResult.getVulnerablePoints();
            return vulnerablePoints;
        }else{
            throw new ThisShouldNeverHappenException();
        }
    }
}
