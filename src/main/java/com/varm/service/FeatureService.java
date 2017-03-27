package com.varm.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.varm.model.Document;
import com.varm.model.Feature;

public class FeatureService {

	public static Feature extractFeatureStats(List<Document> dataset) {
		Feature stats = new Feature();

		Integer categoryCount = new Integer(0);
		Integer featureCategoryCount = new Integer(0);
		String category = new String();
		String feature = new String();
		Map<String, Integer> featureCategoryCounts = new HashMap<>();

		for (Document document : dataset) {

			stats.setNrOfObservations(stats.getNrOfObservations() + 1); // increase
																		// the
																		// number
																		// of
																		// observations
			category = document.getCategory();

			// increase the category counter by one
			categoryCount = stats.getCategoryCounts().get(category);
			if (categoryCount == null) {
				Map<String, Integer> categoryCountsTemp = stats.getCategoryCounts();

				categoryCountsTemp.put(category, 1);
				stats.setCategoryCounts(categoryCountsTemp);

			} else {
				Map<String, Integer> categoryCountsTemp = stats.getCategoryCounts();

				categoryCountsTemp.put(category, categoryCount + 1);
				stats.setCategoryCounts(categoryCountsTemp);
			}

			for (Map.Entry<String, Integer> entry : document.getTokens().entrySet()) {
				feature = entry.getKey();

				// get the counts of the feature in the categories
				featureCategoryCounts = stats.getFeatureCategoryJointCount().get(feature);
				if (featureCategoryCounts == null) {
					// initialize it if it does not exist
					Map<String, Map<String, Integer>> featureCategoryJointCountTemp = stats
							.getFeatureCategoryJointCount();

					featureCategoryJointCountTemp.put(feature, new HashMap<String, Integer>());
					stats.setFeatureCategoryJointCount(featureCategoryJointCountTemp);

				}

				featureCategoryCount = stats.getFeatureCategoryJointCount().get(feature).get(category);
				if (featureCategoryCount == null) {
					featureCategoryCount = 0;
				}

				// increase the number of occurrences of the feature in the
				// category
				stats.getFeatureCategoryJointCount().get(feature).put(category, ++featureCategoryCount);
			}
		}

		System.out.println("Stats: " + stats.getFeatureCategoryJointCount().size());

		return stats;
	}

	/**
	 * Gathers the required counts for the features and performs feature
	 * selection on the above counts. It returns a FeatureStats object that is
	 * later used for calculating the probabilities of the model.
	 * 
	 */
	public static Feature selectFeatures(List<Document> dataset) {

		// the FeatureStats object contains statistics about all the features
		// found in the documents
		// extract the stats of the dataset
		Feature stats = FeatureService.extractFeatureStats(dataset);

		// we pass this information to the feature selection algorithm and we
		// get a list with the selected features
		Map<String, Double> selectedFeatures = ChisquareService.chisquare(stats, 10.83);


		// clip from the stats all the features that are not selected
		Iterator<Map.Entry<String, Map<String, Integer>>> it = stats.getFeatureCategoryJointCount().entrySet()
				.iterator();
		while (it.hasNext()) {
			String feature = it.next().getKey();

			if (selectedFeatures.containsKey(feature) == false) {
				// if the feature is not in the selectedFeatures list remove it
				it.remove();
			}
		}

		return stats;
	}

}
