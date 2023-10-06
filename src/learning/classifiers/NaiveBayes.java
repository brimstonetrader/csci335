package learning.classifiers;

import core.Duple;
import learning.core.Classifier;
import learning.core.Histogram;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Function;

public class NaiveBayes<V,L,F,FV> implements Classifier<V,L> {
    // Each entry represents P(Feature | Label)
    // We want to know P(Label | Features). That means calculating P(Feature | Label) * P(Label) / P(Feature)
    // We calculate 1 / sum(P(Feature|Label)) for P(Label) / P(Feature)
    private LinkedHashMap<L,Histogram<Duple<F,FV>>> featureCounts = new LinkedHashMap<>();
    private Function<V,ArrayList<Duple<F,FV>>> allFeaturesFrom;

    public NaiveBayes(Function<V,ArrayList<Duple<F,FV>>> allFeaturesFrom) {
        this.allFeaturesFrom = allFeaturesFrom;
    }

    // TODO:
    //   Find the product of P(Feature | Label) for all labels and features of the given example.
    //   To find the best label, divide each product by the sum(P(Feature | Label)) for all labels.
    @Override
    public L classify(V example) {
        return null;
    }

    // TODO:
    //  For each data item
    //    For each feature of the data item
    //      Increase the histogram count by 1 for the data item's label for that feature
    @Override
    public void train(ArrayList<Duple<V, L>> data) {
        // Your code here
    }
}
