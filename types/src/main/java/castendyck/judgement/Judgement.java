package castendyck.judgement;

import castendyck.printable.Printable;
import castendyck.reduction.Reduction;

public interface Judgement extends Printable {
    Reduction getReduction();
}
