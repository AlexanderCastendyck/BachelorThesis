package castendyck.analyzing.analyzerchainproviding.source.internal;

import castendyck.analyzing.analyzerchain.AnalyzerChain;
import castendyck.analyzing.analyzerchainproviding.analyzerchainregistering.AnalyzerChainRegistry;
import castendyck.analyzing.analyzerchainproviding.analyzerchainregistering.internal.SimpleAnalyzerChainRegistryImpl;
import castendyck.analyzing.judging.JudgeConfiguration;
import castendyck.analyzing.judging.JudgeConfigurationBuilder;
import castendyck.analyzing.reducing.ReducerConfiguration;
import castendyck.analyzing.reducing.ReducerConfigurationBuilder;
import castendyck.cwe.CWE;
import castendyck.cwe.CweType;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class AnalyzerChainRegistryBuilder {
    private final Map<CweType, AnalyzerChainBuilder> analyzerChainBuilders;
    private ReducerConfigurationBuilder reducerConfigurationBuilder;
    private JudgeConfigurationBuilder judgeConfigurationBuilder;
    private AnalyzerChainBuilder defaultChainBuilder;

    public AnalyzerChainRegistryBuilder() {
        this.analyzerChainBuilders = new HashMap<>();
    }

    public static AnalyzerChainRegistryBuilder anAnalyzerChainRegistry() {
        return new AnalyzerChainRegistryBuilder();
    }

    public AnalyzerChainRegistryBuilder registeredFor(CweType cwe, AnalyzerChainBuilder analyzerChainBuilder) {
        analyzerChainBuilders.put(cwe, analyzerChainBuilder);
        return this;
    }

    public AnalyzerChainRegistryBuilder withDefaultChain(AnalyzerChainBuilder defaultChain) {
        this.defaultChainBuilder = defaultChain;
        return this;
    }
    public AnalyzerChainRegistryBuilder configuredWith(ReducerConfigurationBuilder reducerConfigurationBuilder) {
        this.reducerConfigurationBuilder = reducerConfigurationBuilder;
        return this;
    }
    public AnalyzerChainRegistryBuilder configuredWith(JudgeConfigurationBuilder judgeConfigurationBuilder) {
        this.judgeConfigurationBuilder = judgeConfigurationBuilder;
        return this;
    }

    public AnalyzerChainRegistry build() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final ReducerConfiguration reducerConfiguration = reducerConfigurationBuilder.build();
        final JudgeConfiguration judgeConfiguration = judgeConfigurationBuilder.build();

        final Map<CweType, AnalyzerChain> mapping = new HashMap<>();
        for (Map.Entry<CweType, AnalyzerChainBuilder> entry : analyzerChainBuilders.entrySet()) {
            final CweType cwe = entry.getKey();
            final AnalyzerChainBuilder analyzerChainBuilder = entry.getValue();
            final AnalyzerChain analyzerChain = analyzerChainBuilder.build(reducerConfiguration, judgeConfiguration);
            mapping.put(cwe, analyzerChain);
        }

        final AnalyzerChain defaultChain = defaultChainBuilder.build(reducerConfiguration, judgeConfiguration);
        return new SimpleAnalyzerChainRegistryImpl(mapping, defaultChain);
    }
}
