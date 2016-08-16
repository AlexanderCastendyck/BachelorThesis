package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.cve.CVE;
import castendyck.maven.pomfile.PomFile;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpreter;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpretingResult;

public class CveInterpreterTestExample {
    private final CveInterpreter interpreter;
    private final CVE cve;
    private final PomFile pomFile;
    private final CveInterpretingResult expectedResult;

    public CveInterpreterTestExample(CveInterpreter interpreter, CVE cve, PomFile pomFile, CveInterpretingResult expectedResult) {
        this.interpreter = interpreter;
        this.cve = cve;
        this.pomFile = pomFile;
        this.expectedResult = expectedResult;
    }


    public CveInterpreter getInterpreter() {
        return interpreter;
    }

    public CVE getCve() {
        return cve;
    }

    public PomFile getPomFile() {
        return pomFile;
    }

    public CveInterpretingResult getExpectedResult() {
        return expectedResult;
    }
}
