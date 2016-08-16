package castendyck.inmemorycompiling.methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NormalMethodSourceCodeBuilder extends MethodSourceCodeBuilder {
    private List<String> calls = new ArrayList<>();
    private String modifier = "";
    private String arbitraryCode;
    private String parameters = "()";

    public static NormalMethodSourceCodeBuilder sourceCodeForAMethod() {
        return new NormalMethodSourceCodeBuilder();
    }

    @Override
    public NormalMethodSourceCodeBuilder named(String name) {
        super.named(name);
        return this;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public NormalMethodSourceCodeBuilder withAccess(String access) {
        super.withAccess(access);
        return this;
    }

    public NormalMethodSourceCodeBuilder withParameters(String parameters) {
        this.parameters = parameters;
        return this;
    }

    public NormalMethodSourceCodeBuilder callingOnField(String objectName, String methodName) {
        final String call = objectName + "." + methodName + "();";
        calls.add(call);
        return this;
    }

    public NormalMethodSourceCodeBuilder callingUsingTempObject(String objectName, String methodName) {
        final int random = (int) (Math.random() * 1000 + 1);
        final String objectTempName = objectName + "_" + random;
        final String call = objectName + " " + objectTempName + " = new " + objectName + "();\n" +
                objectTempName + "." + methodName + "();";
        calls.add(call);
        return this;
    }

    public NormalMethodSourceCodeBuilder callingWithinSameClass(String methodName) {
        final String call = methodName + "();";
        calls.add(call);
        return this;
    }

    public NormalMethodSourceCodeBuilder callingWithStringParameters(String objectName, String methodName, String... params) {
        final String paramsFormatted = Arrays.stream(params)
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(", "));
        final String call = objectName + "." + methodName + "(" + paramsFormatted + ");";
        calls.add(call);
        return this;
    }

    public NormalMethodSourceCodeBuilder withArbitraryCodeBeforeCall(String arbitraryCode) {
        this.arbitraryCode = arbitraryCode;
        return this;
    }

    public NormalMethodSourceCodeBuilder witModifier(String modifier) {
        this.modifier = modifier;
        return this;
    }

    public String build() {
        StringBuilder stringBuilder = new StringBuilder();
        final String methodSignature = access + " " + modifier + " void " + name + parameters + "{\n";
        stringBuilder.append(methodSignature);

        if (arbitraryCode != null) {
            stringBuilder.append(arbitraryCode)
                    .append("\n");
        }
        for (String call : calls) {
            stringBuilder.append(call)
                    .append("\n");
        }
        stringBuilder.append("}");

        final String methodSource = stringBuilder.toString();
        return methodSource;
    }
}
