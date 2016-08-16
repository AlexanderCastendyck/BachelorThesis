package castendyck.reporting;

import castendyck.reporting.result.Result;
import castendyck.reporting.result.internal.ExceptionResultImpl;

public class ExceptionResultBuilder {
    private Exception exception;

    public static ExceptionResultBuilder anErroneousResult() {
        return new ExceptionResultBuilder();
    }

    public ExceptionResultBuilder forError(Exception exception) {
        this.exception = exception;
        return this;
    }

    public Result build() {
        return new ExceptionResultImpl(exception);
    }
}
