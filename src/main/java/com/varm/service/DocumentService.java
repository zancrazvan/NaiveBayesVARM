package com.varm.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.varm.model.Document;
import com.varm.util.TextTokenizer;

public class DocumentService {

	/**
	 * Preprocesses the original dataset and converts it to a List of Documents.
	 * 
	 */
	public static List<Document> preprocessDataset(Map<String, String[]> trainingDataset) {
		List<Document> dataset = new ArrayList<>();

		String category = new String();
		String[] examples;

		Document doc;

		Iterator<Map.Entry<String, String[]>> it = trainingDataset.entrySet().iterator();

		// loop through all the categories and training examples
		while (it.hasNext()) {
			Map.Entry<String, String[]> entry = it.next();
			category = entry.getKey();
			examples = entry.getValue();

			for (int i = 0; i < examples.length; ++i) {
				// for each example in the category tokenize its text and
				// convert it into a Document object.
				doc = TextTokenizer.tokenize(examples[i]);
				doc.setCategory(category);
				dataset.add(doc);

				// examples[i] = null; //try freeing some memory
			}

			// it.remove(); //try freeing some memory
		}

		return dataset;
	}

}
