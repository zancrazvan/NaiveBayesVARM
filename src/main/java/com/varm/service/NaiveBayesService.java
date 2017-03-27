package com.varm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.varm.model.Document;
import com.varm.model.Feature;
import com.varm.model.NaiveBayesKnowledgeBase;
import com.varm.util.TextTokenizer;

/**
 * 
 * @author RazvanZanc
 *
 */
public class NaiveBayesService {

	/**
	 * Trains a Naive Bayes classifier by using the Multinomial Model by passing
	 * the trainingDataset and the prior probabilities.
	 * 
	 */
	public static NaiveBayesKnowledgeBase train(Map<String, String[]> trainingDataset,
			Map<String, Double> categoryPriors) throws IllegalArgumentException {
		// preprocess the given dataset
		List<Document> dataset = DocumentService.preprocessDataset(trainingDataset);

		// produce the feature stats and select the best features
		Feature featureStats = FeatureService.selectFeatures(dataset);

		// intiliaze the knowledgeBase of the classifier
		NaiveBayesKnowledgeBase knowledgeBase = new NaiveBayesKnowledgeBase();
		knowledgeBase.setNrOfObservations(featureStats.getNrOfObservations()); // number
																				// of
																				// observations

		knowledgeBase.setNumberOffeatures(featureStats.getFeatureCategoryJointCount().size()); // number
																								// of
																								// features

		// check is prior probabilities are given
		if (categoryPriors == null) {
			// if not estimate the priors from the sample
			knowledgeBase.setNumberOfCategories(featureStats.getCategoryCounts().size()); // number
																							// of
																							// cateogries
			System.out.println("In train nrOfCategories: " + knowledgeBase.getNumberOfCategories());
			knowledgeBase.setLogPriors(new HashMap<>());

			String category = new String();
			int count = 0;

			for (Map.Entry<String, Integer> entry : featureStats.getCategoryCounts().entrySet()) {
				category = entry.getKey();
				count = entry.getValue();

				Map<String, Double> logPriorsTemp = knowledgeBase.getLogPriors();
				logPriorsTemp.put(category, Math.log((double) count / knowledgeBase.getNrOfObservations()));

				knowledgeBase.setLogPriors(logPriorsTemp);

			}
		} else {
			// if they are provided then use the given priors
			knowledgeBase.setNumberOfCategories(categoryPriors.size());

			// make sure that the given priors are valid
			if (knowledgeBase.getNumberOfCategories() != featureStats.getCategoryCounts().size()) {
				throw new IllegalArgumentException(
						"Invalid priors Array: Make sure you pass a prior probability for every supported category.");
			}

			String category = new String();
			Double priorProbability = new Double(0);
			for (Map.Entry<String, Double> entry : categoryPriors.entrySet()) {
				category = entry.getKey();
				priorProbability = entry.getValue();
				if (priorProbability == null) {
					throw new IllegalArgumentException(
							"Invalid priors Array: Make sure you pass a prior probability for every supported category.");
				} else if (priorProbability < 0 || priorProbability > 1) {
					throw new IllegalArgumentException(
							"Invalid priors Array: Prior probabilities should be between 0 and 1.");
				}

				Map<String, Double> logPriorsTemp = knowledgeBase.getLogPriors();
				logPriorsTemp.put(category, Math.log(priorProbability));
				knowledgeBase.setLogPriors(logPriorsTemp);
			}
		}

		// We are performing laplace smoothing (also known as add-1). This
		// requires to estimate the total feature occurrences in each category
		Map<String, Double> featureOccurrencesInCategory = new HashMap<>();

		Integer occurrences;
		Double featureOccSum;
		for (String category : knowledgeBase.getLogPriors().keySet()) {
			featureOccSum = 0.0;
			for (Map<String, Integer> categoryListOccurrences : featureStats.getFeatureCategoryJointCount().values()) {
				occurrences = categoryListOccurrences.get(category);
				if (occurrences != null) {
					featureOccSum += occurrences;
				}
			}
			featureOccurrencesInCategory.put(category, featureOccSum);
		}

		// estimate log likelihoods
		String feature;
		Integer count;
		Map<String, Integer> featureCategoryCounts;
		double logLikelihood;

		for (String category : knowledgeBase.getLogPriors().keySet()) {
			for (Map.Entry<String, Map<String, Integer>> entry : featureStats.getFeatureCategoryJointCount()
					.entrySet()) {
				feature = entry.getKey();
				featureCategoryCounts = entry.getValue();

				count = featureCategoryCounts.get(category);
				if (count == null) {
					count = 0;
				}

				logLikelihood = Math.log((count + 1.0)
						/ (featureOccurrencesInCategory.get(category) + knowledgeBase.getNumberOffeatures()));
				if (knowledgeBase.getLogLikelihoods().containsKey(feature) == false) {
					knowledgeBase.getLogLikelihoods().put(feature, new HashMap<String, Double>());
				}

				Map<String, Map<String, Double>> temp1 = knowledgeBase.getLogLikelihoods();

				Map<String, Double> temp2 = temp1.get(feature);

				temp2.put(category, logLikelihood);

				knowledgeBase.setLogLikelihoods(temp1);

				// System.out.println("Temp1: " + temp1.toString());

				// knowledgeBase.getLogLikelihoods().get(feature).put(category,
				// logLikelihood);
			}
		}
		featureOccurrencesInCategory = null;

		System.out.println(knowledgeBase.toString());
		return knowledgeBase;
	}

	/**
	 * Wrapper method of train() which enables the estimation of the prior
	 * probabilities based on the sample.
	 * 
	 */
	public static NaiveBayesKnowledgeBase train(Map<String, String[]> trainingDataset) {
		return NaiveBayesService.train(trainingDataset, null);
	}

	/**
	 * Predicts the category of a text by using an already trained classifier
	 * and returns its category.
	 * 
	 * @param text
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static String predict(String text, NaiveBayesKnowledgeBase knowledgeBase) throws IllegalArgumentException {
		if (knowledgeBase == null) {
			throw new IllegalArgumentException(
					"Knowledge Bases missing: Make sure you train first a classifier before you use it.");
		}

		// Tokenizes the text and creates a new document
		Document doc = TextTokenizer.tokenize(text);

		String category;
		String feature;
		Integer occurrences;
		Double logprob;

		String maxScoreCategory = null;
		Double maxScore = Double.NEGATIVE_INFINITY;

		// Map<String, Double> predictionScores = new HashMap<>();
		for (Map.Entry<String, Double> entry1 : knowledgeBase.getLogPriors().entrySet()) {
			category = entry1.getKey();
			logprob = entry1.getValue(); // intialize the scores with the priors

			// foreach feature of the document
			for (Map.Entry<String, Integer> entry2 : doc.getTokens().entrySet()) {
				feature = entry2.getKey();

				if (!knowledgeBase.getLogLikelihoods().containsKey(feature)) {
					continue; // if the feature does not exist in the knowledge
								// base skip it
				}

				occurrences = entry2.getValue(); // get its occurrences in text

				logprob += occurrences * knowledgeBase.getLogLikelihoods().get(feature).get(category); // multiply
																										// loglikelihood
																										// score
																										// with
																										// occurrences
			}
			// predictionScores.put(category, logprob);

			if (logprob > maxScore) {
				maxScore = logprob;
				maxScoreCategory = category;
			}
		}

		return maxScoreCategory; // return the category with heighest score
	}
}
