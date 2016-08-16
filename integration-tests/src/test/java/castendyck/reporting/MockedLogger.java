package castendyck.reporting;

import org.apache.maven.plugin.logging.Log;

public class MockedLogger implements Log {
    private final StringBuilder stringBuilder;

    protected MockedLogger() {
        this.stringBuilder = new StringBuilder();
    }

    public String getOutput() {
        return stringBuilder.toString();
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void debug(CharSequence charSequence) {
        stringBuilder.append(charSequence);
    }

    @Override
    public void debug(CharSequence charSequence, Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void debug(Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public void info(CharSequence charSequence) {
        stringBuilder.append(charSequence);
    }

    @Override
    public void info(CharSequence charSequence, Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void info(Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(CharSequence charSequence) {
        stringBuilder.append(charSequence);
    }

    @Override
    public void warn(CharSequence charSequence, Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void warn(Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(CharSequence charSequence) {
        stringBuilder.append(charSequence);
    }

    @Override
    public void error(CharSequence charSequence, Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(Throwable throwable) {
        throw new UnsupportedOperationException();
    }
}
