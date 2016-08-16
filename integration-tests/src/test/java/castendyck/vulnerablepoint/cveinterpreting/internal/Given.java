package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.cve.CVE;
import castendyck.maven.pomfile.PomFile;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpreter;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpretingResult;

public class Given {
    public static When given(CveInterpreterTestExample example) {
        final CveInterpreter interpreter = example.getInterpreter();
        final CVE cve = example.getCve();
        final PomFile pomFile = example.getPomFile();
        final CveInterpretingResult expectedResult = example.getExpectedResult();
        return new When(interpreter, cve, pomFile, expectedResult);
    }

    public static When given(CveStateBuilder stateBuilder) {
        final CveInterpreterTestExample state = stateBuilder.build();
        return given(state);
    }

}
