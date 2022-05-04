//************************************************************************
//  File: TestSet.java            Final Project
// 
//  Hannah Forbes, I'noli Hall, Garrett Ham, Aranza Rodriguez
//
//  Class: TestSet
//
//************************************************************************
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TestSet {

    final static String testsetFile = "testset.txt";
    final static String genresFile = "testsetgenres.txt";

    final static int TEST_SET_SIZE = 15;

    public static String[] bookTitles;
    public static HashMap<String, String> bookGenres;
    public static ArrayList<double[]> features;
    public static HashMap<String, double[]> bookFeatures;

    public static void main(String[] args) {
        // For testing
        TestSet test = new TestSet();
        for (int i = 0; i < bookTitles.length; i++) {
            System.out.println(test.bookTitles()[i]); 
        }
    }

    public TestSet() {
        loadTitlesAndGenres(testsetFile, genresFile);
    }

    private void loadTitlesAndGenres(String filename, String filename2) {
        try {
            File file = new File(filename);
            File file2 = new File(filename2);
            Scanner scan = new Scanner(file);
            Scanner scan2 = new Scanner(file2);

            bookTitles = new String[TEST_SET_SIZE];
            bookGenres = new HashMap<String, String>();

            for (int i = 0; i < TEST_SET_SIZE; i++) {
                bookTitles[i] = scan.nextLine();
                bookGenres.put(bookTitles[i], scan2.nextLine());
            }
            scan.close();
            scan2.close();
        } catch (IOException e) {
            System.out.println("Could not find " + filename);
            System.exit(1);
        }            
    }

    public String[] bookTitles() {
        return bookTitles;
    }

    public HashMap<String, String> bookGenres() {
        return bookGenres;
    }

    public ArrayList<double[]> getFeatures() {

        FeatureWordCounter[] testSet = new FeatureWordCounter[TEST_SET_SIZE];
        features = new ArrayList<double[]>();

        final File folder = new File("testFiles");
        File fileNames[] = folder.listFiles();

        for (int i = 0; i < TEST_SET_SIZE; i++) {
            String name = "" + fileNames[i];
            testSet[i] = new FeatureWordCounter(name);
            features.add(testSet[i].featureWordValues());
        }
        return features;
    }

    public HashMap<String, double[]> bookFeatures() {

        ArrayList<double[]> featuresData = getFeatures();
        bookFeatures = new HashMap<String, double[]>();

        for (int i = 0; i < TEST_SET_SIZE; i++) {
            bookFeatures.put(bookTitles[i], featuresData.get(i));
        }
        return bookFeatures;
    }

} // end of class