package castendyck.inmemorycompiling.methods;

public abstract class MethodSourceCodeBuilder {
    protected String name;
    protected String access = "public";

    public MethodSourceCodeBuilder named(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public MethodSourceCodeBuilder withAccess(String access) {
        this.access = access;
        return this;
    }


    public abstract String build();
}
