package de.castendyck.collections;

import java.util.List;

public class OneUniqueElementExpectedException extends RuntimeException {
    private List receivedElements;

    public OneUniqueElementExpectedException(String message, List receivedElements) {
        super(message);
        this.receivedElements = receivedElements;
    }

    public List getReceivedElements() {
        return receivedElements;
    }
}
