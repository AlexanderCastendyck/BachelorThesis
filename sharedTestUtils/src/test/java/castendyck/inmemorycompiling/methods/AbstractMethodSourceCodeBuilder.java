package castendyck.inmemorycompiling.methods;

public class AbstractMethodSourceCodeBuilder extends MethodSourceCodeBuilder {

    public static AbstractMethodSourceCodeBuilder sourceCodeForAnAbstractMethod() {
        return new AbstractMethodSourceCodeBuilder();
    }

    public String build() {
        final String method = access + " abstract void " + name + "();\n";
        return method;
    }
}
