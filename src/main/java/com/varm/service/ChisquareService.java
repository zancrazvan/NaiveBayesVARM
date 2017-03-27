package com.varm.service;

import java.util.HashMap;
import java.util.Map;

import com.varm.model.Feature;

/**
 * 
 * @author RazvanZanc
 * 
 *         Perform feature selection by using the chisquare non-parametrical
 *         statistical test.
 */
public class ChisquareService {

	public static Map<String, Double> chisquare(Feature stats, double criticalLevel) {
		Map<String, Double> selectedFeatures = new HashMap<>();

		String feature = new String();
		String category = new String();
		Map<String, Integer> categoryList = new HashMap<>();

		int N1dot, N0dot, N00, N01, N10, N11;
		double chisquareScore = 0.0;
		Double previousScore = new Double(0);
		for (Map.Entry<String, Map<String, Integer>> entry1 : stats.getFeatureCategoryJointCount().entrySet()) {
			feature = entry1.getKey();
			categoryList = entry1.getValue();

			// calculate the N1. (number of documents that have the feature)
			N1dot = 0;
			for (Integer count : categoryList.values()) {
				N1dot += count;
			}

			// also the N0. (number of documents that DONT have the feature)
			N0dot = stats.getNrOfObservations() - N1dot;

			for (Map.Entry<String, Integer> entry2 : categoryList.entrySet()) {

				category = entry2.getKey();

				// N11 is the number of documents that have the feature and
				// belong on the specific category
				N11 = entry2.getValue();

				// N01 is the total number of documents that do not have the
				// particular feature BUT they belong to the specific category
				N01 = stats.getCategoryCounts().get(category) - N11;

				// N00 counts the number of documents that
				// don't have the feature and don't belong to the specific
				// category
				N00 = N0dot - N01;

				// N10 counts the number of documents that
				// have the feature and don't belong to the specific category
				N10 = N1dot - N11;

				// calculate the chisquare score based on the above statistics
				chisquareScore = stats.getNrOfObservations() * Math.pow(N11 * N00 - N10 * N01, 2)
						/ ((N11 + N01) * (N11 + N10) * (N10 + N00) * (N01 + N00));

				// if the score is larger than the critical value then add it in
				// the list
				if (chisquareScore >= criticalLevel) {
					previousScore = selectedFeatures.get(feature);
					if (previousScore == null || chisquareScore > previousScore) {
						selectedFeatures.put(feature, chisquareScore);
					}
				}
			}
		}

		System.out.println("Chisquare Score: " + chisquareScore);
		return selectedFeatures;
	}

}
