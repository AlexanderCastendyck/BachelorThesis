package castendyck.endtoendtests;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.castendyck.collections.ObjectListToStringCollector.collectToString;

public class ConsoleOutputMatcher extends TypeSafeDiagnosingMatcher<File> {
    private final List<String> lines;
    private List<String> remainingLines;

    public ConsoleOutputMatcher(List<String> lines) {
        this.lines = lines;
    }

    public static ConsoleOutputMatcher contains(String... lines) {
        final List<String> list = Arrays.asList(lines);
        return new ConsoleOutputMatcher(list);
    }


    @Override
    protected boolean matchesSafely(File file, Description description) {
        remainingLines = new ArrayList<>(lines);
        final BufferedReader reader = createBufferedReader(file);

        reader.lines()
                .forEach(this::checkAgainstExpectedLines);

        if (remainingLines.isEmpty()) {
            return true;
        } else {
            final String notMatchedLines = remainingLines.stream()
                    .collect(collectToString());
            final BufferedReader reader2 = createBufferedReader(file);
            reader2.lines()
                    .forEach(System.out::println);
            final String message = "Not matched Lines: " + notMatchedLines;
            throw new AssertionError(message);
        }
    }

    private BufferedReader createBufferedReader(File file) {
        try {
            final FileReader fileReader = new FileReader(file);
            return new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            final String message = e.getMessage();
            throw new AssertionError(message);
        }
    }

    private void checkAgainstExpectedLines(String currentLine) {
        final String trimmedLine = trim(currentLine);
        if (remainingLines.contains(trimmedLine)) {
            remainingLines.remove(trimmedLine);
        }
        if (remainingLines.contains(currentLine)) {
            remainingLines.remove(currentLine);
        }
    }

    private String trim(String currentLine) {
        String trimmedLine = currentLine.trim();
        if (trimmedLine.endsWith("\n")) {
            final int indexOfSecondLastCharacter = trimmedLine.length() - 2;
            trimmedLine = trimmedLine.substring(0, indexOfSecondLastCharacter);
        }
        return trimmedLine;
    }

    @Override
    public void describeTo(Description description) {

    }
}
