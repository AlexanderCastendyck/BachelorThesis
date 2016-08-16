package castendyck.inmemorycompiling.methods;

public class InterfaceMethodSourceCodeBuilder extends MethodSourceCodeBuilder {

    public static InterfaceMethodSourceCodeBuilder sourceCodeForAnInterfaceMethod() {
        return new InterfaceMethodSourceCodeBuilder();
    }

    public String build() {
        final String method = "void " + name + "();\n";
        return method;
    }
}
