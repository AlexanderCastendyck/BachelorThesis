package castendyck.inmemorycompiling.classes;

import castendyck.inmemorycompiling.methods.MethodSourceCodeBuilder;
import castendyck.inmemorycompiling.methods.NormalMethodSourceCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassSourceCodeBuilder {
    protected final List<MethodSourceCodeBuilder> methods = new ArrayList<>();
    protected String name;


    public ClassSourceCodeBuilder named(String name) {
        this.name = name;
        return this;
    }

    public ClassSourceCodeBuilder withAMethod(MethodSourceCodeBuilder methodSourceCodeBuilder) {
        methods.add(methodSourceCodeBuilder);
        return this;
    }

    public String getName() {
        return name;
    }


    public abstract String build();

}
