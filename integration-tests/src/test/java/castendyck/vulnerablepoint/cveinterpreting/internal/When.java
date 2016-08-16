package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.cve.CVE;
import castendyck.maven.pomfile.PomFile;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpreter;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpretingResult;

public class When {
    private final CveInterpreter cveInterpreter;
    private final CVE cve;
    private final PomFile pomFile;
    private final CveInterpretingResult expectedResult;

    public When(CveInterpreter cveInterpreter, CVE cve, PomFile pomFile, CveInterpretingResult expectedResult) {
        this.cveInterpreter = cveInterpreter;
        this.cve = cve;
        this.pomFile = pomFile;
        this.expectedResult = expectedResult;
    }

    public Then whenCveIsInterpreted() {
        final CveInterpretingResult result = cveInterpreter.interpret(cve, pomFile);
        return new Then(result, expectedResult);
    }
}
