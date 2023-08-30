package learning.markov;

import learning.core.Histogram;

import java.util.*;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class MarkovChain<L,S> {

    private LinkedHashMap<L, HashMap<Optional<S>, Histogram<S>>> label2symbol2symbol = new LinkedHashMap<>();

    // The outer hash map has as its key the language of the chain, and as its 
    // value an inner hash map. The inner hash map has as keys the first character
    // in a pair, and as its values a Histogram object. Each Histogram is basically a
    // wrapping of HashMap specialized for counting.

    public Set<L> allLabels() {return label2symbol2symbol.keySet();}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (L language: label2symbol2symbol.keySet()) {
            sb.append(language);
            sb.append('\n');
            for (Map.Entry<Optional<S>, Histogram<S>> entry: label2symbol2symbol.get(language).entrySet()) {
                sb.append("    ");
                sb.append(entry.getKey());
                sb.append(":");
                sb.append(entry.getValue().toString());
                sb.append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // Increase the count for the transition from prev to next.
    // Should pass SimpleMarkovTest.testCreateChains().
    public void count(Optional<S> prev, L label, S next) {
        if (label2symbol2symbol.get(label) == null) {
            HashMap<Optional<S>, Histogram<S>> nhm = new HashMap<>();
            label2symbol2symbol.put(label, nhm);
        }
        if (label2symbol2symbol.get(label).get(prev) == null) {
            Histogram<S> nhg = new Histogram<S>();
            label2symbol2symbol.get(label).put(prev, nhg);
        }
        label2symbol2symbol.get(label).get(prev).bump(next);
    }

    // Returns P(sequence | label)
    // Should pass SimpleMarkovTest.testSourceProbabilities() and MajorMarkovTest.phraseTest()
    //
    // HINT: Be sure to add 1 to both the numerator and denominator when finding the probability of a
    // transition. This helps avoid sending the probability to zero.

    public double probability(ArrayList<S> sequence, L label) {
        double pn = 1.0;
        double pd = 1.0;
        HashMap<Optional<S>, Histogram<S>> hm = label2symbol2symbol.get(label);
        for (int i=0;i<sequence.size()-1;i++) {
            if (hm.get(of(sequence.get(i))) != null) {
                Histogram<S> hg = hm.get(of(sequence.get(i)));
                pn *= (1 + hg.getCountFor(sequence.get(i+1)));
                pd *= (1 + hg.getTotalCounts());
            }
        }
        return pn/pd;
    }
    public double pLabelGivenSequence(ArrayList<S> sequence, L label) {
        double num   = 0.0;
        double denom = 0.0;
        for (L l : label2symbol2symbol.keySet()) {
            double x = probability(sequence,l);
            if (l == label) {num += x;}
            denom += x;
        }
        return num/denom;
    }

    // Return a map from each label to P(label | sequence).
    // Should pass MajorMarkovTest.testSentenceDistributions()
    public LinkedHashMap<L,Double> labelDistribution(ArrayList<S> sequence) {
        Set<L> ls    = label2symbol2symbol.keySet();
        LinkedHashMap<L,Double> ld = new LinkedHashMap<>();
        double denom = 0.0;
        for (L l : ls) {
            denom += pLabelGivenSequence(sequence,l);
        }
        for (L l : ls) {
            ld.put(l,pLabelGivenSequence(sequence,l)/denom);
        }
        return ld;
    }

    // Calls labelDistribution(). Returns the label with highest probability.
    // Should pass MajorMarkovTest.bestChainTest()
    public L bestMatchingChain(ArrayList<S> sequence) {
        LinkedHashMap<L, Double> ld = labelDistribution(sequence);
        double p = 0.0;
        for (Double d : ld.values()) {
            if (d > p) {p = d;}
        }
        for (L l : ld.keySet()) {
            if (ld.get(l) == p) {return l;}
        }
        return (L) "Unable to Determine Maximum";
    }
}




















