package com.varm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author RazvanZanc
 *
 */
public class FileReader {

	/**
	 * Reads the all lines from a file and places it a String array. In each
	 * record in the String array we store a training example text.
	 * 
	 */
	public static String[] readLines(URL url) throws IOException {

		Reader fileReader = new InputStreamReader(url.openStream(), Charset.forName("UTF-8"));
		List<String> lines;
		try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
			lines = new ArrayList<>();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
		}
		return lines.toArray(new String[lines.size()]);
	}

}
