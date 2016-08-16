package castendyck.dependencygraphing;

public class Given {
    private final DependencyStateBuilder.DependencyState dependencyState;

    public Given(DependencyStateBuilder.DependencyState dependencyState) {
        this.dependencyState = dependencyState;
    }

    public static Given given(DependencyStateBuilder dependencyStateBuilder) throws Exception {
        final DependencyStateBuilder.DependencyState build = dependencyStateBuilder.build();
        return new Given(build);
    }

    public When when(){
        return new When(dependencyState);
    }
}
