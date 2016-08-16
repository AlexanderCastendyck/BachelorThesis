package castendyck.reporting;

public class Given {
    public static When given(ReportingExample example) {
        return new When(example);
    }
}
