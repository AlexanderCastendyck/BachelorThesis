package de.castendyck.enforcing;

public class NullStringEscaper {

    public static String escapeNullStringWith(String stringToCheck, String replacement) {
        if (stringToCheck == null) {
            return replacement;
        } else {
            return stringToCheck;
        }
    }
}
