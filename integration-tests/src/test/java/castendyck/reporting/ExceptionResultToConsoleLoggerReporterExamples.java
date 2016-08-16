package castendyck.reporting;

import castendyck.media.ErrorLogLevel;
import castendyck.media.Media;
import castendyck.media.ResultLogLevel;
import castendyck.media.internal.MavenLoggingMediaImpl;
import castendyck.reporting.result.Result;

public class ExceptionResultToConsoleLoggerReporterExamples {
    public static ExceptionResultToConsoleLoggerReporterExamples anErroneousResult() {
        return new ExceptionResultToConsoleLoggerReporterExamples();
    }

    public ReportingExample withErrorMessage(String message) {
        final Exception exception = new Exception(message);
        final Result result = ExceptionResultBuilder.anErroneousResult()
                .forError(exception)
                .build();
        final MockedLogger mockedLogger = new MockedLogger();
        final Media media = new MavenLoggingMediaImpl(mockedLogger, ResultLogLevel.ALL, ErrorLogLevel.ALL);
        return new ReportingExample(result, media, mockedLogger);
    }
}
