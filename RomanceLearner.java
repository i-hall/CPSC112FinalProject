//************************************************************************
//  File: RomanceLearner.java            Final Project
// 
//  Hannah Forbes, I'noli Hall, Garrett Ham, Aranza Rodriguez
//
//  Class: RomanceLearner
//
//************************************************************************
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class RomanceLearner {

    ArrayList<String> documentWords;
    HashSet<String> stopWords;

    public static void main(String[] args) throws FileNotFoundException {
        // For testing
        args = new String[1];
        RomanceLearner wc = new RomanceLearner(args[0]);
        for (Entry<String, Integer> word : wc.getTopN(100)) {
            System.out.println(word);
        }
    }

    public RomanceLearner(String filename) throws FileNotFoundException {
        loadDocument();
        loadStopWords(filename);
    }

    private void loadDocument() throws FileNotFoundException {

        documentWords = new ArrayList<>();
        final File folder = new File("Romance");
        File fileNames[] = folder.listFiles();

        for (int i = 0; i < 35; i++) {
            try (Scanner scanner = new Scanner(fileNames[i])) {
                while (scanner.hasNext()) {
                    String temp = scanner.next().replaceAll("[^a-zA-Z]", "").toLowerCase();
                    if (temp != "") {
                        documentWords.add(temp);
                    }
                }
            }
        }
    }

    private void loadStopWords(String filename) throws FileNotFoundException {

        stopWords = new HashSet<String>();
        filename = "stopwords.txt";

        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNext()) {
            stopWords.add(scanner.next().toLowerCase());
        }
    }

    public HashMap<String, Integer> wordFrequency() {

        HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
        ArrayList<String> updatedList = new ArrayList<String>();

        for (int i = 1; i < documentWords.size(); i++) {
            if (stopWords.contains(documentWords.get(i))) {
            } else {
                updatedList.add(documentWords.get(i));
            }
        }

        for (int i = 0; i < updatedList.size(); i++) {
            if (wordCount.containsKey(updatedList.get(i))) {
                wordCount.put(updatedList.get(i), wordCount.get(updatedList.get(i)) + 1);
            } else {
                wordCount.put(updatedList.get(i), 1);
            }
        }
        return wordCount;
    }

    private Entry<String, Integer> getMaxEntry(HashMap<String, Integer> counts) {

        Entry<String, Integer> max = counts.entrySet().iterator().next();

        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            if (e.getValue() > max.getValue()) {
                max = e;
            }
        }
        return max;
    }

    public ArrayList<Entry<String, Integer>> getTopN(int n) {
        HashMap<String, Integer> counts = wordFrequency();
        ArrayList<Entry<String, Integer>> mostWords = new ArrayList<Entry<String, Integer>>();

        for (int i = 0; i < n; i++) {
            mostWords.add(getMaxEntry(counts));
            mostWords.get(i).setValue(n - i);
            counts.remove(getMaxEntry(counts).getKey());
        }

        return mostWords;
    }
}