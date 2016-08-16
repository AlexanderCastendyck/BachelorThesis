package castendyck.inmemorycompiling;

public class FieldSourceCodeBuilder {
    private String name;
    private String type;
    private String initializedWith;

    public static FieldSourceCodeBuilder sourceCodeForField() {
        return new FieldSourceCodeBuilder();
    }

    public FieldSourceCodeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public FieldSourceCodeBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public FieldSourceCodeBuilder initializedWithObject(String initializedWith) {
        this.initializedWith = initializedWith;
        return this;
    }

    public String build() {
        return "private " + type + " " + name + " = new " + initializedWith + "();";
    }

}
