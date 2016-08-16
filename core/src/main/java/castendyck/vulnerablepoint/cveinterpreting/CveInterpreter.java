package castendyck.vulnerablepoint.cveinterpreting;

import castendyck.cve.CVE;
import castendyck.maven.pomfile.PomFile;

public interface CveInterpreter {

    CveInterpretingResult interpret(CVE cve, PomFile pomFile);

}
