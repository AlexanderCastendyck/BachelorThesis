package castendyck.reporting;

import castendyck.analysisresult.AnalysisResult;
import castendyck.analysisresult.AnalysisResultBuilder;
import castendyck.analyzedcve.AnalyzedCve;
import castendyck.analyzedcve.AnalyzedCveTestBuilder;
import castendyck.media.ErrorLogLevel;
import castendyck.media.Media;
import castendyck.media.ResultLogLevel;
import castendyck.media.internal.MavenLoggingMediaImpl;
import castendyck.reporting.result.Result;

public class StandardResultToConsoleLoggerReporterTestExamples {
    public static ReportingExample emptyAnalysisResult() {
        final AnalysisResult analysisResult = AnalysisResultBuilder.anAnalysisResult()
                .build();
        final Result result = StandardResultBuilder.aResult()
                .forAnalysisResult(analysisResult)
                .build();
        final MockedLogger mockedLogger = new MockedLogger();
        final Media media = new MavenLoggingMediaImpl(mockedLogger, ResultLogLevel.ALL, ErrorLogLevel.ALL);
        return new ReportingExample(result, media, mockedLogger);
    }

    public static ReportingExample resultWith(AnalyzedCveTestBuilder analyzedCveTestBuilder) {
        final AnalyzedCve analyzedCve = analyzedCveTestBuilder.build();
        final AnalysisResult analysisResult = AnalysisResultBuilder.anAnalysisResult()
                .withAnAnalyzedCve(analyzedCve)
                .build();
        final Result result = StandardResultBuilder.aResult()
                .forAnalysisResult(analysisResult)
                .build();
        final MockedLogger mockedLogger = new MockedLogger();
        final Media media = new MavenLoggingMediaImpl(mockedLogger, ResultLogLevel.ALL, ErrorLogLevel.ALL);
        return new ReportingExample(result, media, mockedLogger);
    }
}
