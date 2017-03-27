package com.varm.main;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.varm.model.NaiveBayesKnowledgeBase;
import com.varm.service.NaiveBayesService;
import com.varm.util.FileReader;

public class Main {

	public static void main(String args[]) throws IOException {
		// map of dataset files
		Map<String, URL> trainingFiles = new HashMap<>();
		trainingFiles.put("English", Main.class.getResource("/datasets/training.language.en.txt"));
		trainingFiles.put("French", Main.class.getResource("/datasets/training.language.fr.txt"));
		trainingFiles.put("German", Main.class.getResource("/datasets/training.language.de.txt"));

		// loading examples in memory
		Map<String, String[]> trainingExamples = new HashMap<>();
		for (Map.Entry<String, URL> entry : trainingFiles.entrySet()) {
			trainingExamples.put(entry.getKey(), FileReader.readLines(entry.getValue()));

		}

		// train classifier
		NaiveBayesKnowledgeBase nb = new NaiveBayesKnowledgeBase();
		nb = NaiveBayesService.train(trainingExamples);
		// nb.train(trainingExamples);

		// get trained classifier knowledgeBase
		// NaiveBayesKnowledgeBase knowledgeBase =
		// NaiveBayesService.train(trainingExamples);

		// nb = null;
		// trainingExamples = null;
		// System.out.println(nb.toString());
		// Use classifier
		// nb = new NaiveBayesKnowledgeBase();
		String exampleEn = "I am English";
		String outputEn = NaiveBayesService.predict(exampleEn, nb);
		System.out.format("The sentense \"%s\" was classified as \"%s\".%n", exampleEn, outputEn);

		String exampleFr = "Je suis Français";
		String outputFr = NaiveBayesService.predict(exampleFr, nb);
		System.out.format("The sentense \"%s\" was classified as \"%s\".%n", exampleFr, outputFr);

		String exampleDe = "Ich bin Deutsch";
		String outputDe = NaiveBayesService.predict(exampleDe, nb);
		System.out.format("The sentense \"%s\" was classified as \"%s\".%n", exampleDe, outputDe);

		String exampleEn2 = "Bonjour le monde! Je suis un Naive Bayesian Network hopa";
		String outputEn2 = NaiveBayesService.predict(exampleEn2, nb);
		System.out.format("The sentense \"%s\" was classified as \"%s\".%n", exampleEn2, outputEn2);
 
		// System.out.println(nb.toString());
	}

}
