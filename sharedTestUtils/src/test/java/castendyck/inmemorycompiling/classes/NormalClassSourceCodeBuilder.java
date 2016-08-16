package castendyck.inmemorycompiling.classes;

import castendyck.inmemorycompiling.FieldSourceCodeBuilder;
import castendyck.inmemorycompiling.methods.MethodSourceCodeBuilder;
import castendyck.inmemorycompiling.methods.NormalMethodSourceCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class NormalClassSourceCodeBuilder extends ClassSourceCodeBuilder{
    private final List<FieldSourceCodeBuilder> fields = new ArrayList<>();
    private String modifier = "";
    private String inheritanceModifier = "";

    public static NormalClassSourceCodeBuilder sourceCodeForAClass() {
        return new NormalClassSourceCodeBuilder();
    }

    public NormalClassSourceCodeBuilder named(String name) {
        super.named(name);
        return this;
    }

    public NormalClassSourceCodeBuilder withAMethod(MethodSourceCodeBuilder methodSourceCodeBuilder) {
        super.withAMethod(methodSourceCodeBuilder);
        return this;
    }

    public NormalClassSourceCodeBuilder withAField(FieldSourceCodeBuilder fieldSourceCodeBuilder) {
        fields.add(fieldSourceCodeBuilder);
        return this;
    }

    public String getName() {
        return name;
    }

    public NormalClassSourceCodeBuilder withModifier(String modifier) {
        this.modifier = modifier;
        return this;
    }

    public NormalClassSourceCodeBuilder extending(String extendedClass) {
        this.inheritanceModifier = "extends "+extendedClass;
        return this;
    }

    public NormalClassSourceCodeBuilder implementing(String interfaceClass) {
        this.inheritanceModifier = "implements "+interfaceClass;
        return this;
    }

    public String build() {
        final StringBuilder stringBuilder = new StringBuilder();
        final String classSignature = "public " + modifier + " class " + name + " " + inheritanceModifier + " {\n";
        stringBuilder.append(classSignature);

        for (FieldSourceCodeBuilder field : fields) {
            final String fieldSourceCode = field.build();
            stringBuilder.append(fieldSourceCode)
                    .append("\n");
        }

        for (MethodSourceCodeBuilder method : methods) {
            final String methodSourceCode = method.build();
            stringBuilder.append(methodSourceCode)
                    .append("\n\n");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
