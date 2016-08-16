package castendyck.inmemorycompiling.classes;

import castendyck.inmemorycompiling.methods.MethodSourceCodeBuilder;

public class InterfaceClassSourceCodeBuilder extends ClassSourceCodeBuilder {

    public static InterfaceClassSourceCodeBuilder sourceCodeForAnInterface() {
        return new InterfaceClassSourceCodeBuilder();
    }

    @Override
    public String build() {
        final StringBuilder stringBuilder = new StringBuilder();
        final String classSignature = "public interface " + name + " {\n";
        stringBuilder.append(classSignature);

        for (MethodSourceCodeBuilder method : methods) {
            final String methodSourceCode = method.build();
            stringBuilder.append(methodSourceCode)
                    .append("\n\n");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
