package castendyck.vulnerablepoint;

import castendyck.cve.CVE;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.vulnerablepoint.internal.VulnerablePointImpl;

public class VulnerablePointBuilder {
    private FunctionIdentifier functionIdentifier;
    private CVE cve;


    public static VulnerablePointBuilder aVulnerablePoint() {
        return new VulnerablePointBuilder();
    }

    public VulnerablePointBuilder withFunctionIdentifier(FunctionIdentifier functionIdentifier){
        this.functionIdentifier = functionIdentifier;
        return this;
    }
    public VulnerablePointBuilder forCve(CVE cve){
        this.cve = cve;
        return this;
    }


    public VulnerablePoint build(){
        return new VulnerablePointImpl(functionIdentifier, cve);
    }

}
