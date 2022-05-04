//************************************************************************
//  File: TrainingSet.java            Final Project
// 
//  Hannah Forbes, I'noli Hall, Garrett Ham, Aranza Rodriguez
//
//  Class: TrainingSet
//
//************************************************************************
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TrainingSet {

    final static String trainingsetFile = "trainingset.txt";
    final static String genresFile = "trainingsetgenres.txt";

    final static int TRAIN_SET_SIZE = 90;

    public static String[] bookTitles;
    public static HashMap<String, String> bookGenres;
    public static ArrayList<double[]> features;
    public static HashMap<String, double[]> bookFeatures;

    public static void main(String[] args) {
        // For testing
        TrainingSet ts = new TrainingSet();
        for (int i = 0; i < bookTitles.length; i++) {
            System.out.println(ts.bookTitles()[i]); 
        }
    }

    public TrainingSet() {
        loadTitlesAndGenres(trainingsetFile, genresFile);
    }

    private void loadTitlesAndGenres(String filename, String filename2) {
        try {
            File file = new File(filename);
            File file2 = new File(filename2);
            Scanner scan = new Scanner(file);
            Scanner scan2 = new Scanner(file2);

            bookTitles = new String[TRAIN_SET_SIZE];
            bookGenres = new HashMap<String, String>();

            for (int i = 0; i < TRAIN_SET_SIZE; i++) {
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

        FeatureWordCounter[] trainSet = new FeatureWordCounter[TRAIN_SET_SIZE];
        features = new ArrayList<double[]>();

        final File folder = new File("trainingFiles");
        File fileNames[] = folder.listFiles();

        for (int i = 0; i < TRAIN_SET_SIZE; i++) {
            String name = "" + fileNames[i];
            trainSet[i] = new FeatureWordCounter(name);
            features.add(trainSet[i].featureWordValues());
        }
        return features;
    }

    public HashMap<String, double[]> bookFeatures() {

        ArrayList<double[]> featuresData = getFeatures();
        bookFeatures = new HashMap<String, double[]>();

        for (int i = 0; i < TRAIN_SET_SIZE; i++) {
            bookFeatures.put(bookTitles[i], featuresData.get(i));
        }
        return bookFeatures;
    }

} // end of class