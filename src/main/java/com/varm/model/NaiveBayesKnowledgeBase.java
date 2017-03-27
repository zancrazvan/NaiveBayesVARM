package com.varm.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author RazvanZanc
 *
 *         All the fields that the classifier learns during training.
 */
public class NaiveBayesKnowledgeBase {

	private int nrOfObservations;

	private int numberOfCategories;

	private int numberOffeatures;

	// log priors for log( P(c) )
	private Map<String, Double> logPriors = new HashMap<>();

	// log likelihood for log( P(x|c) )
	private Map<String, Map<String, Double>> logLikelihoods = new HashMap<>();

	public int getNrOfObservations() {
		return nrOfObservations;
	}

	public void setNrOfObservations(int nrOfObservations) {
		this.nrOfObservations = nrOfObservations;
	}

	public int getNumberOfCategories() {
		return numberOfCategories;
	}

	public void setNumberOfCategories(int numberOfCategories) {
		this.numberOfCategories = numberOfCategories;
	}

	public int getNumberOffeatures() {
		return numberOffeatures;
	}

	public void setNumberOffeatures(int numberOffeatures) {
		this.numberOffeatures = numberOffeatures;
	}

	public Map<String, Double> getLogPriors() {
		return logPriors;
	}

	public void setLogPriors(Map<String, Double> logPriors) {
		this.logPriors = logPriors;
	}

	public Map<String, Map<String, Double>> getLogLikelihoods() {
		return logLikelihoods;
	}

	public void setLogLikelihoods(Map<String, Map<String, Double>> logLikelihoods) {
		this.logLikelihoods = logLikelihoods;
	}

	@Override
	public String toString() {
		return "NaiveBayesKnowledgeBase [nrOfObservations=" + nrOfObservations + ", numberOfCategories="
				+ numberOfCategories + ", numberOffeatures=" + numberOffeatures + ", logPriors=" + logPriors
				+ ", logLikelihoods=" + logLikelihoods + "]";
	}

}
