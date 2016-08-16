package castendyck.analyzing.analyzerchain;

import castendyck.analyzing.analyzerchain.internal.AnalyzerChainImpl;
import castendyck.analyzing.judging.Judge;
import castendyck.analyzing.reducing.Reducer;

import java.util.List;

public class AnalyzerChainFactory {

    public static AnalyzerChain newInstance(List<Reducer> reducerList, Judge judge) {
        return new AnalyzerChainImpl(reducerList, judge);
    }
}
