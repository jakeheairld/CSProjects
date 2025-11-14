package comprehensive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

/**
 * Represents the word probability model and chain for determining text
 * generation.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version April 21, 2024
 */
public class WordProbabilityModel {

	private Map<String, Map<String, Integer>> markovChain;
	private Random random;

	/**
	 * Creates the word probability model using a text file.
	 * 
	 * @param file - The text file used to create the model
	 */
	public WordProbabilityModel(String file) {
		markovChain = generateChain(file);
		random = new Random();
	}

	/**
	 * Generates the output text for the default output type.
	 * 
	 * @param seed     - The seed word
	 * @param numWords - The number of words to generate
	 * @return The line of text for the default output type
	 */
	public String generateText(String seed, int numWords) {
		StringBuilder textBuilder = new StringBuilder();
		PriorityQueue<Map.Entry<String, Integer>> mostProbableWords = getKMostProbableWords(seed, numWords);
		if (mostProbableWords == null || mostProbableWords.isEmpty())
			return "";
		boolean firstWord = true;
		for (Map.Entry<String, Integer> entry : mostProbableWords) {
			if (firstWord) {
				textBuilder.append(entry.getKey());
				firstWord = false;
			} else {
				textBuilder.append(" ").append(entry.getKey());
			}
		}
		return textBuilder.toString();
	}

	/**
	 * Generates the output text for the all output type.
	 * 
	 * @param seed     - The seed word
	 * @param numWords - The number of words to generate
	 * @return The line of text for the all output type
	 */
	public String generateTextAll(String seed, int numWords) {
		StringBuilder textBuilder = new StringBuilder(seed);
		String currentWord = seed;
		for (int i = 0; i < numWords - 1; i++) {
			String nextWord = calculateNextWordAll(currentWord);
			if (nextWord == null) {
				nextWord = seed;
			}
			textBuilder.append(" ").append(nextWord);
			currentWord = nextWord;
		}
		return textBuilder.toString();
	}

	/**
	 * Generates the output text for the one output type.
	 * 
	 * @param seed     - The seed word
	 * @param numWords - The number of words to generate
	 * @return The line of text for the one output type
	 */
	public String generateTextOne(String seed, int numWords) {
		StringBuilder textBuilder = new StringBuilder(seed);
		String currentWord = seed;
		for (int i = 0; i < numWords - 1; i++) {
			String nextWord = calculateNextWordOne(currentWord);
			if (nextWord == null) {
				nextWord = seed;
			}
			textBuilder.append(" ").append(nextWord);
			currentWord = nextWord;
		}
		return textBuilder.toString();
	}

	/**
	 * Finds the k most probable subsequent words to the word passed.
	 * 
	 * @param seed - The word used to find most probable subsequent words
	 * @param k    - The number of most probable subsequent words to return
	 * @return The k most probable subsequent words
	 */
	private PriorityQueue<Map.Entry<String, Integer>> getKMostProbableWords(String seed, int k) {
		Map<String, Integer> subsequentWords = markovChain.get(seed);
		if (subsequentWords == null || subsequentWords.isEmpty())
			return null;
		PriorityQueue<Map.Entry<String, Integer>> mostProbableWords = new PriorityQueue<Map.Entry<String, Integer>>(
				new ProbabilityComparator());
		for (Map.Entry<String, Integer> entry : subsequentWords.entrySet()) {
			mostProbableWords.offer(entry);
		}
		if (mostProbableWords.size() <= k) {
			return mostProbableWords;
		}
		PriorityQueue<Map.Entry<String, Integer>> topWords = new PriorityQueue<Map.Entry<String, Integer>>(k,
				new ProbabilityComparator());
		for (int i = 0; i < k; i++) {
			topWords.offer(mostProbableWords.poll());
		}
		return topWords;
	}

	/**
	 * Custom comparator used to sort subsequent words based on their occurrence
	 * count, if occurrence count is equal, compares lexicographically.
	 */
	private class ProbabilityComparator implements Comparator<Map.Entry<String, Integer>> {
		public int compare(Entry<String, Integer> entry1, Entry<String, Integer> entry2) {
			int comparison = entry2.getValue().compareTo(entry1.getValue());
			if (comparison == 0)
				return entry1.getKey().compareTo(entry2.getKey());
			return comparison;
		}
	}

	/**
	 * Calculates the next word to print after the current word following the one
	 * output type.
	 * 
	 * @param currentWord - The word being used to calculate the next word
	 * @return The next word using the one output type
	 */
	private String calculateNextWordOne(String currentWord) {
		Map<String, Integer> subsequentWords = markovChain.get(currentWord);
		if (subsequentWords == null || subsequentWords.isEmpty())
			return null;
		String nextWord = null;
		int maxOccurrence = -1;
		for (Map.Entry<String, Integer> entry : subsequentWords.entrySet()) {
			if (entry.getValue() > maxOccurrence
					|| (entry.getValue() == maxOccurrence && entry.getKey().compareTo(nextWord) < 0)) {
				maxOccurrence = entry.getValue();
				nextWord = entry.getKey();
			}
		}
		return nextWord;
	}

	/**
	 * Calculates the next word to print after the current word following the all
	 * output type.
	 * 
	 * @param currentWord - The word being used to calculate the next word
	 * @return The next word using the all output type
	 */
	private String calculateNextWordAll(String currentWord) {
		Map<String, Integer> subsequentWords = markovChain.get(currentWord);
		if (subsequentWords == null || subsequentWords.isEmpty())
			return null;
		int totalOccurrences = 0;
		for (int numOccurrences : subsequentWords.values()) {
			totalOccurrences += numOccurrences;
		}
		int randomValue = random.nextInt(totalOccurrences);
		double cumulativeTracker = 0;
		for (Map.Entry<String, Integer> entry : subsequentWords.entrySet()) {
			cumulativeTracker += entry.getValue();
			if (randomValue < cumulativeTracker) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * Generates the chain using the given text file.
	 * 
	 * @param file - The file used to create the chain
	 * @return The chain created using the text file
	 */
	public Map<String, Map<String, Integer>> generateChain(String file) {
		Map<String, Map<String, Integer>> chain = new HashMap<String, Map<String, Integer>>();
		try {
			Scanner scanner = new Scanner(new File(file));
			String previousWord = null;
			while (scanner.hasNext()) {
				String currentWord = scanner.next().toLowerCase();
				if (!currentWord.matches("^[a-z0-9_]+$"))
					currentWord = findUsedWord(currentWord);
				if (previousWord != null && currentWord.length() > 0) {
					chain.putIfAbsent(previousWord, new HashMap<>());
					Map<String, Integer> subsequentWords = chain.get(previousWord);
					subsequentWords.put(currentWord, subsequentWords.getOrDefault(currentWord, 0) + 1);
				}
				previousWord = currentWord;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file);
		}
		return chain;
	}

	/**
	 * Finds the word to use in the case of special and unwanted characters.
	 * 
	 * @param word - The word to filter out unwanted characters
	 * @return The filtered word
	 */
	private String findUsedWord(String word) {
		String tempWord = "";
		for (int j = 0; j < word.length() - 1; j++) {
			if (word.substring(j, j + 1).matches("^[a-z0-9_]+$")) {
				tempWord += word.substring(j, j + 1);
			} else {
				break;
			}
		}
		return tempWord;
	}
}
