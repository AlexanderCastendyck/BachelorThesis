package castendyck.reporting.result.internal;

import de.castendyck.collections.ObjectListToStringCollector;
import castendyck.media.Media;
import castendyck.reporting.result.Result;

import java.util.Arrays;

public class ExceptionResultImpl implements Result {
    private final Exception exception;

    public ExceptionResultImpl(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void print(Media media) {
        final String stackTraceString = Arrays.stream(exception.getStackTrace())
                .map(stackTraceElement -> stackTraceElement.toString() + "\n")
                .collect(ObjectListToStringCollector.collectToString());
        media.starting("header")
                .withText("Analysis failed: ")
                .with("exceptionClass", exception.getClass().toString())
                .with("exceptionMessage", exception.getMessage())
                .ending("header")
                .with("stacktrace", stackTraceString);
    }
}
