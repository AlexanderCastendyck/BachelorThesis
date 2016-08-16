package castendyck.analyzing.analyzerchainproviding.source.internal;

import castendyck.analyzing.analyzerchain.AnalyzerChain;
import castendyck.analyzing.analyzerchain.internal.AnalyzerChainImpl;
import castendyck.analyzing.judging.Judge;
import castendyck.analyzing.judging.JudgeConfiguration;
import castendyck.analyzing.reducing.Reducer;
import castendyck.analyzing.reducing.ReducerConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AnalyzerChainBuilder {
    private Class<? extends Judge> judgeClass;
    private final List<Class<? extends Reducer>> reducerClassList;

    public AnalyzerChainBuilder() {
        this.reducerClassList = new ArrayList<>();
    }

    public static AnalyzerChainBuilder anAnalyzerChain() {
        return new AnalyzerChainBuilder();
    }

    public AnalyzerChainBuilder withReducer(Class<? extends Reducer> reducerClass) {
        reducerClassList.add(reducerClass);
        return this;
    }

    public AnalyzerChainBuilder withJudge(Class<? extends Judge> judgeClass) {
        this.judgeClass = judgeClass;
        return this;
    }

    public AnalyzerChain build(ReducerConfiguration reducerConfiguration, JudgeConfiguration judgeConfiguration) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final List<Reducer> initializedConstructors = new ArrayList<>();
        for (Class<? extends Reducer> reducerClass : reducerClassList) {
            final Constructor<? extends Reducer> constructor = reducerClass.getDeclaredConstructor(ReducerConfiguration.class);
            final Reducer reducer = constructor.newInstance(reducerConfiguration);
            initializedConstructors.add(reducer);
        }

        final Constructor<? extends Judge> constructor = judgeClass.getDeclaredConstructor(JudgeConfiguration.class);
        final Judge judge = constructor.newInstance(judgeConfiguration);

        return new AnalyzerChainImpl(initializedConstructors, judge);
    }
}
