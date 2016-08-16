package de.castendyck.utils;

public class ThisShouldNeverHappenException extends RuntimeException {
    public ThisShouldNeverHappenException(Throwable throwable) {
        super(throwable);
    }

    public ThisShouldNeverHappenException() {
    }
}
