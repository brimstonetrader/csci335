package learning.decisiontree;

import core.Duple;
import learning.core.Histogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DTTrainer<V,L, F, FV extends Comparable<FV>> {
	private ArrayList<Duple<V,L>> baseData;
	private boolean restrictFeatures;
	private Function<ArrayList<Duple<V,L>>, ArrayList<Duple<F,FV>>> allFeatures;
	private BiFunction<V,F,FV> getFeatureValue;
	private Function<FV,FV> successor;

	public DTTrainer(ArrayList<Duple<V, L>> data, Function<ArrayList<Duple<V, L>>, ArrayList<Duple<F,FV>>> allFeatures,
					 boolean restrictFeatures, BiFunction<V,F,FV> getFeatureValue, Function<FV,FV> successor) {
		baseData = data;
		this.restrictFeatures = restrictFeatures;
		this.allFeatures = allFeatures;
		this.getFeatureValue = getFeatureValue;
		this.successor = successor;
	}

	public DTTrainer(ArrayList<Duple<V, L>> data, Function<ArrayList<Duple<V,L>>, ArrayList<Duple<F,FV>>> allFeatures,
					 BiFunction<V,F,FV> getFeatureValue, Function<FV,FV> successor) {
		this(data, allFeatures, false, getFeatureValue, successor);
	}

	// TODO: Call allFeatures.apply() to get the feature list. Then shuffle the list, retaining
	//  only targetNumber features. Should pass DTTest.testReduced().
	public static <V,L, F, FV  extends Comparable<FV>> ArrayList<Duple<F,FV>>
	reducedFeatures(ArrayList<Duple<V,L>> data, Function<ArrayList<Duple<V, L>>, ArrayList<Duple<F,FV>>> allFeatures,
					int targetNumber) {
		ArrayList<Duple<F, FV>> list = allFeatures.apply(data);
		ArrayList<Duple<F, FV>> shuffledList = resample(list);
		ArrayList<Duple<F, FV>> part = new ArrayList<>();
		if (shuffledList.size() < 2) { return shuffledList; }
		for (int q = 0; q < Math.min(targetNumber, shuffledList.size()); q++) {
			part.add(shuffledList.get(q));
		}
		return part;
    }

	public DecisionTree<V,L,F,FV> train() {
		return train(baseData);
	}

	public static <V,L> int numLabels(ArrayList<Duple<V,L>> data) {
		return data.stream().map(Duple::getSecond).collect(Collectors.toUnmodifiableSet()).size();
	}

	private DecisionTree<V,L,F,FV> train(ArrayList<Duple<V,L>> data) {
		// TODO: Implement the decision tree learning algorithm
		if (numLabels(data) == 1) {
			// TODO: Return a leaf node consisting of the only label in data
			return new DTLeaf<>(data.get(0).getSecond());
		}
		if (data.size() == 0) {
			return null;
		}

		else {
			// TODO: Return an interior node.
			//  If restrictFeatures is false, call allFeatures.apply() to get a complete list
			//  of features and values, all of which you should consider when splitting.
			//  If restrictFeatures is true, call reducedFeatures() to get sqrt(# features)
			//  of possible features/values as candidates for the split. In either case,
			//  for each feature/value combination, use the splitOn() function to break the
			//  data into two parts. Then use gain() on each split to figure out which
			//  feature/value combination has the highest gain. Use that combination, as
			//  well as recursively created left and right nodes, to create the new
			//  interior node.
			//  Note: It is possible for the split to fail; that is, you can have a split
			//  in which one branch has zero elements. In this case, return a leaf node
			//  containing the most popular label in the branch that has elements.
			ArrayList<Duple<F, FV>> feat = allFeatures.apply(data);
			if (feat.size() == 0) { return new DTLeaf<>(data.get(0).getSecond()); }
			Duple<F,FV> maxF = feat.get(0);
			double maxGain = -3;
			Duple<ArrayList<Duple<V, L>>, ArrayList<Duple<V, L>>> maxSplit = new Duple<>(new ArrayList<>(), new ArrayList<>());
			if (restrictFeatures) { feat = reducedFeatures(data, allFeatures, (int) Math.pow(data.size(),0.5)); }
			for (Duple<F,FV> f : feat) {
				Duple<ArrayList<Duple<V, L>>, ArrayList<Duple<V, L>>> a = splitOn(data, f.getFirst(), f.getSecond(), getFeatureValue);
				if (a.getFirst().size() == 0 || a.getSecond().size() == 0) {
					continue;
				}
				double g = gain(data, a.getFirst(), a.getSecond());
				if (g > maxGain) {
					maxGain = g;
					maxSplit = a;
					maxF = f;
				}
			}
			if (maxSplit.getFirst().size() == 0 || maxSplit.getSecond().size() == 0)
			    { return new DTLeaf<>(data.get(0).getSecond()); }
			DecisionTree<V,L,F,FV> left  = train(maxSplit.getFirst());
			DecisionTree<V,L,F,FV> right = train(maxSplit.getSecond());
			return new DTInterior<>(maxF.getFirst(), maxF.getSecond(), left, right, getFeatureValue, successor);
		}
	}

	public static <V,L> L mostPopularLabelFrom(ArrayList<Duple<V, L>> data) {
		Histogram<L> h = new Histogram<>();
		for (Duple<V,L> datum: data) {
			h.bump(datum.getSecond());
		}
		return h.getPluralityWinner();
	}

	// TODO: Generates a new data set by sampling randomly with replacement. It should return
	//    an `ArrayList` that is the same length as `data`, where each element is selected randomly
	//    from `data`. Should pass `DTTest.testResample()`.
	public static <V,L> ArrayList<Duple<V,L>> resample(ArrayList<Duple<V,L>> data) {
		Collections.shuffle(data);
		return data;
	}

	public static <V, L> double getGini(ArrayList<Duple<V, L>> data) {
		double gini = 0.0;
		Histogram<L> labels = new Histogram<L>();

		for (Duple<V, L> d : data) {
			labels.bump(d.getSecond());
		}

		for (L l : labels) {
			double p_i = (double) labels.getCountFor(l) / (double) data.size();
			gini += p_i * p_i;
		}

		return 1 - gini;
	}

	public static <V,L> double gain(ArrayList<Duple<V,L>> parent, ArrayList<Duple<V,L>> child1,
									ArrayList<Duple<V,L>> child2) {
		// TODO: Calculate the gain of the split. Add the gini values for the children.
		//  Subtract that sum from the gini value for the parent. Should pass DTTest.testGain().
		return getGini(parent) - getGini(child1) - getGini(child2);
	}

	public static <V,L, F, FV  extends Comparable<FV>> Duple<ArrayList<Duple<V,L>>,ArrayList<Duple<V,L>>> splitOn
			(ArrayList<Duple<V,L>> data, F feature, FV featureValue, BiFunction<V,F,FV> getFeatureValue) {
		// TODO:
		//  Returns a duple of two new lists of training data.
		//  The first returned list should be everything from this set for which
		//  feature has a value less than or equal to featureValue. The second
		//  returned list should be everything else from this list.
		//  Should pass DTTest.testSplit().
		ArrayList<Duple<V,L>> lessSet = new ArrayList<>();
		ArrayList<Duple<V,L>> moreSet = new ArrayList<>();
		for (Duple<V,L> d : data) {
			if (getFeatureValue.apply(d.getFirst(), feature).compareTo(featureValue) > 0) {
				moreSet.add(d);
			}
			else {
				lessSet.add(d);
			}
		}
		return new Duple<>(lessSet, moreSet);
	}
}
