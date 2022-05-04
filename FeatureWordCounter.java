//************************************************************************
//  File: FeatureWordCounter.java            Final Project
// 
//  Hannah Forbes, I'noli Hall, Garrett Ham, Aranza Rodriguez
//
//  Class: FeatureWordCounter
//
//************************************************************************
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FeatureWordCounter {

    final static String featurewordsFile = "featurewords.txt";

    ArrayList<String> documentWords;
    ArrayList<String> featureWords;

    public static void main(String[] args) {
        // For testing
        FeatureWordCounter wc = new FeatureWordCounter(args[0]);
        for (int i = 0; i < wc.featureWordValues().length; i++) {
            System.out.println(wc.featureWordValues()[i]);
        }
    }

    public FeatureWordCounter(String filename) {
        loadDocument(filename);
        loadFeatureWords(featurewordsFile);
    }

    private void loadDocument(String filename) {

        // load the document's words into an ArrayList
        try {
            File file = new File(filename);
            Scanner scan = new Scanner(file);

            documentWords = new ArrayList<String>();

            while (scan.hasNext()){
                String input = scan.next();
                input = input.toLowerCase().replaceAll("[^a-zA-Z]", "");
                documentWords.add(input);
            }
            scan.close();
        } catch (IOException e) {
            System.out.println("Could not find " + filename);
            System.exit(1);
        }
    }

    private void loadFeatureWords(String filename) {

        // load the feature words to an ArrayList
        try {
            File file = new File(filename);
            Scanner scan = new Scanner(file);

            featureWords = new ArrayList<String>();

            while (scan.hasNext()){
                featureWords.add(scan.next());
            }
            scan.close();
        } catch (IOException e) {
            System.out.println("Could not find " + filename);
            System.exit(1);
        }
    }

    public HashMap<String, Double> wordFrequency() {
    
        // create a HashMap
        HashMap<String, Double> wordFrequency = new HashMap<String, Double>();

        // count occurrences of feature words that appear in documentWords
        // store as proportion (# occurrences / total # words)
        for (String word : documentWords) {
            if (featureWords.contains(word)) {
                if (wordFrequency.containsKey(word)) {
                    wordFrequency.put(word, (wordFrequency.get(word)*documentWords.size() + 1)/documentWords.size());
                } else {
                    wordFrequency.put(word, 1.0/documentWords.size());
                }  
            }
        }
        // if feature word does not appear, set value to 0.0 in HashMap
        for (int i = 0; i < featureWords.size(); i++) {
            if (!wordFrequency.containsKey(featureWords.get(i))) {
                wordFrequency.put(featureWords.get(i), 0.0);
            }
        }
        // return the HashMap
        return wordFrequency;
    }

    public double[] featureWordValues() {

        HashMap<String, Double> wordFrequency = wordFrequency();

        // store feature values in a double array
        double[] featureValues = new double[featureWords.size()];

        for (int i = 0; i < featureWords.size(); i++) {
            featureValues[i] = wordFrequency.get(featureWords.get(i));
        }
        // return the double array
        return featureValues;
    }

} // end of class