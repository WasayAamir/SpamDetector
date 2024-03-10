// Import necessary packages
package com.spamdetector.util;

// Import domain class
import com.spamdetector.domain.TestFile;

// Import IO and utility packages
import java.io.*;
import java.util.*;

// Class for spam detection functionality
public class SpamDetector {
    // Frequency maps for training data
    private Map<String, Integer> trainHamFreq;
    private Map<String, Integer> trainSpamFreq;
    // List to hold test files
    private List<TestFile> testFiles;

    // Method to train and test the spam detector
    public List<TestFile> trainAndTest(File mainDirectory) {
        // Initialize frequency maps and testFiles list
        trainHamFreq = new HashMap<>();
        trainSpamFreq = new HashMap<>();
        testFiles = new ArrayList<>();

        // Training phase
        trainModel(new File(mainDirectory, "train/ham"), trainHamFreq);
        trainModel(new File(mainDirectory, "train/ham2"), trainHamFreq);
        trainModel(new File(mainDirectory, "train/spam"), trainSpamFreq);

        // Testing phase
        testFiles.addAll(testModel(new File(mainDirectory, "test/ham"), false));
        testFiles.addAll(testModel(new File(mainDirectory, "test/spam"), true));

        return testFiles;
    }

    // Method to train the model using given directory and frequency map
    private void trainModel(File directory, Map<String, Integer> freqMap) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // Obtain word counts from the file
                    Map<String, Integer> wordCounts = getWordCounts(file);
                    // Update frequency map with word counts
                    for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                        String word = entry.getKey();
                        int count = entry.getValue();
                        freqMap.put(word, freqMap.getOrDefault(word, 0) + count);
                    }
                }
            }
        }
    }

    // Method to obtain word counts from a file
    private Map<String, Integer> getWordCounts(File file) {
        Map<String, Integer> wordCounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Read file line by line
            while ((line = reader.readLine()) != null) {
                // Split line into words
                String[] words = line.split("\\s+");
                // Update word counts
                for (String word : words) {
                    word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
                    if (!word.isEmpty()) {
                        wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordCounts;
    }

    // Method to test the model using given directory and spam flag
    private List<TestFile> testModel(File directory, boolean isSpam) {
        List<TestFile> testFiles = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                // Calculate spam probability for the file
                double spamProbability = calculateSpamProbability(getWordCounts(file).keySet());
                // Create TestFile object and add to list
                testFiles.add(new TestFile(fileName, spamProbability, isSpam ? "Spam" : "Ham"));
            }
        }
        return testFiles;
    }

    // Method to calculate spam probability based on given set of words
    private double calculateSpamProbability(Set<String> words) {
        double spamLogProbability = 0.0;
        for (String word : words) {
            // Get counts of word occurrences in ham and spam
            int hamCount = trainHamFreq.getOrDefault(word, 0);
            int spamCount = trainSpamFreq.getOrDefault(word, 0);
            // Calculate conditional probabilities
            double pWS = (spamCount + 1.0) / (spamCount + hamCount + 2.0);
            double pWH = (hamCount + 1.0) / (spamCount + hamCount + 2.0);
            double pSW = pWS / (pWS + pWH);
            if (pSW == 0) {
                pSW += 0.0000001; // Avoid division by zero
            }
            if (Double.isNaN(pSW) || Double.isInfinite(pSW)) {
                continue; // Skip NaN or infinite values
            }
            // Update spam log probability
            spamLogProbability += Math.log(1 - pSW) - Math.log(pSW);
        }
        // Adjust spam probability to prevent issues with logarithmic calculations
        return 1.0 / (1.0 + Math.pow(Math.E, spamLogProbability));
    }

    // Method to calculate precision of the spam detector
    public double calcPrecision() {
        int numTruePositive = 0;
        int numFalsePositive = 0;
        // Iterate over testFiles
        for (TestFile testFile : testFiles) {
            if (testFile.getActualClass().equals("Spam") && testFile.getSpamProbability() > calculateAverageSpamProbability()) {
                numTruePositive++; // Increment true positive count
            } else if (testFile.getActualClass().equals("Ham") && testFile.getSpamProbability() > calculateAverageSpamProbability()) {
                numFalsePositive++; // Increment false positive count
            }
        }
        // Calculate precision
        if (numTruePositive + numFalsePositive == 0) {
            return 0; // Default value if denominator is zero
        }
        return (double) numTruePositive / (numTruePositive + numFalsePositive);
    }

    // Method to calculate accuracy of the spam detector
    public double calcAccuracy() {
        int numTruePositive = 0;
        int numTrueNegative = 0;
        // Iterate over testFiles
        for (TestFile testFile : testFiles) {
            if (testFile.getActualClass().equals("Spam") && testFile.getSpamProbability() > calculateAverageSpamProbability()) {
                numTruePositive++; // Increment true positive count
            } else if (testFile.getActualClass().equals("Ham") && testFile.getSpamProbability() <= calculateAverageSpamProbability()) {
                numTrueNegative++; // Increment true negative count
            }
        }
        // Calculate accuracy
        int totalFiles = testFiles.size();
        if (numTruePositive + numTrueNegative == 0) {
            return 0; // Default value if denominator is zero
        }
        return (double) (numTruePositive + numTrueNegative) / totalFiles;
    }

    //Method to calculate average spam probability across test files
    public double calculateAverageSpamProbability() {
        double totalSpamProbability = 0.0;
        int numTestFiles = testFiles.size();
        // Iterate over testFiles to sum up spamProbability values
        for (TestFile testFile : testFiles) {
            totalSpamProbability += testFile.getSpamProbability();
        }
        // Calculate average spam probability
        return totalSpamProbability / numTestFiles;
    }
}