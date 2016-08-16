package castendyck.reporting;

import castendyck.media.Media;
import castendyck.reporting.result.Result;

public class ReportingExample {
    private final Result result;
    private final Media media;
    private final MockedLogger mockedLogger;

    ReportingExample(Result result, Media media, MockedLogger mockedLogger) {
        this.result = result;
        this.media = media;
        this.mockedLogger = mockedLogger;
    }

    public Media getMedia() {
        return media;
    }

    public MockedLogger getMockedLogger() {
        return mockedLogger;
    }

    public Result getResult() {
        return result;
    }
}
