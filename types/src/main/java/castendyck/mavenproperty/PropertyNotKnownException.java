package castendyck.mavenproperty;

public class PropertyNotKnownException extends RuntimeException {
    public PropertyNotKnownException(String propertyName) {
        super("Property not known: "+propertyName);
    }
}
