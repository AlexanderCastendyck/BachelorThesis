package de.castendyck.enforcing;

public class NotNullConstraintEnforcer {

    public static void ensureNotNull(Object obj){
        if(obj == null){
            throw new ValueMustNotBeNullException();
        }
    }
    public static void ensureNotNullOrEmptyString(Object obj){
        ensureNotNull(obj);
        if(obj instanceof String){
            final String string = (String) obj;
            if(string.isEmpty()){
                throw new ValueMustNotBeNullException();
            }
        }
    }
}
