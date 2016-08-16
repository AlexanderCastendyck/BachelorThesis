package de.castendyck.enforcing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypePatternEnforcer {
    public static void ensureInputMatchesPattern(String input, Pattern pattern){
        final Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            throw new InvalidTypeValueException("Input doesn't match expected Pattern. Invalid parameter: " + input);
        }
    }
}
