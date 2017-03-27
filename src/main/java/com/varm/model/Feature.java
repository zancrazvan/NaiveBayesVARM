package com.varm.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author RazvanZanc
 * 
 */
public class Feature {

	private int nrOfObservations;

	private Map<String, Map<String, Integer>> featureCategoryJointCount;

	private Map<String, Integer> categoryCounts;

	public Feature() {
		this.nrOfObservations = 0;
		this.featureCategoryJointCount = new HashMap<>();
		this.categoryCounts = new HashMap<>();
	}

	public int getNrOfObservations() {
		return nrOfObservations;
	}

	public void setNrOfObservations(int nrOfObservations) {
		this.nrOfObservations = nrOfObservations;
	}

	public Map<String, Map<String, Integer>> getFeatureCategoryJointCount() {
		return featureCategoryJointCount;
	}

	public void setFeatureCategoryJointCount(Map<String, Map<String, Integer>> featureCategoryJointCount) {
		this.featureCategoryJointCount = featureCategoryJointCount;
	}

	public Map<String, Integer> getCategoryCounts() {
		return categoryCounts;
	}

	public void setCategoryCounts(Map<String, Integer> categoryCounts) {
		this.categoryCounts = categoryCounts;
	}

	@Override
	public String toString() {
		return "Feature [nrOfObservations=" + nrOfObservations + ", featureCategoryJointCount="
				+ featureCategoryJointCount + ", categoryCounts=" + categoryCounts + "]";
	}

}
