package learning.sentiment.learners;

import learning.core.Histogram;
import learning.decisiontree.RandomForest;
import learning.sentiment.core.SentimentAnalyzer;

public class SentimentForest5 extends RandomForest<Histogram<String>, String, String, Integer> {
    public SentimentForest5() {
        super(5, SentimentAnalyzer::allFeatures, Histogram::getCountFor, i -> i + 1);
    }
}
