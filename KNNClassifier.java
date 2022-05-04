//************************************************************************
//  File: KNNClassifier.java            Final Project
// 
//  Hannah Forbes, I'noli Hall, Garrett Ham, Aranza Rodriguez
//
//  Class: KNNClassifier
//
//************************************************************************
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class KNNClassifier {

    public static String[] bookTitles;
    public static HashMap<String, String> bookGenres;
    public static HashMap<String, double[]> bookFeatures;

    public static double[] testFeatures;
    public static HashMap<String, Double> bookDistances;
    public static ArrayList<Entry<String, Double>> nearestNeighbors;

    public static void main(String[] args) {

        KNNClassifier kNN = new KNNClassifier();

        // test file name
        String testFilename = args[0]; 
        // number of nearest neighbors
        int k = Integer.parseInt(args[1]);

        // get test file features        
        FeatureWordCounter wc = new FeatureWordCounter(testFilename);
        testFeatures = wc.featureWordValues();

        // calculate distances between test book and training books
        kNN.euclideanDistance(testFeatures);
        
        // determine the most common genre from the k nearest neighbors
        nearestNeighbors = kNN.getTopK(k);
        System.out.println("Classified Genre: " + kNN.getGenre(nearestNeighbors));

        // determine the names of nearest neighbors for recommendations
        System.out.println(k + " recommendations for similar books:");
        String[] bookRecs = kNN.getRecommendations(nearestNeighbors);
        for (int i = 0; i < k; i++) {
            System.out.println(bookRecs[i]);
        }
    }

    public KNNClassifier() {
        // get training set data
        TrainingSet ts = new TrainingSet();
        bookTitles = ts.bookTitles();
        bookGenres = ts.bookGenres();
        bookFeatures = ts.bookFeatures();

    }

    public void euclideanDistance(double[] testFeatures) {

        bookDistances = new HashMap<String, Double>();

        // initialize sum 
        double totalSum = 0;

        for (int i = 0; i < bookTitles.length; i++) { // each training book
            for (int j = 0; j < bookFeatures.get(bookTitles[i]).length; j++) { // each feature
                // features of current training book
                double[] trainFeatures = bookFeatures.get(bookTitles[i]);
                // calculate squared difference and sum all features together
                double distance = (testFeatures[j] - trainFeatures[j]) * (testFeatures[j] - trainFeatures[j]);
                totalSum += distance;
            }
            // store distance (sqrt of sum) in HashMap & reinitialize totalSum for next book
            bookDistances.put(bookTitles[i], Math.sqrt(totalSum));
            totalSum = 0;
        }
    }

    private Entry<String, Double> getMinEntry(HashMap<String, Double> distances) {
    
        // arbitrary starting point
        Entry<String, Double> min = distances.entrySet().iterator().next();

        // Find and return entry with the smallest value
        for (Entry<String, Double> e: distances.entrySet()) {
            if (e.getValue() < min.getValue()) {
                min = e;
            }
        }
        return min;
    }

    // sort distances and store information on the topK smallest distances
    public ArrayList<Entry<String, Double>> getTopK(int k) {

        // initialize array list for top N inputs
        ArrayList<Entry<String, Double>> closestEntries = new ArrayList<Entry<String, Double>>();

        HashMap<String, Double> distances = bookDistances;

        // loop k times, remove the min entry from the distances hashmap and
        // add it to an ArrayList each time
        for (int i = 0; i < k; i++) {
            Entry<String, Double> minEntry = getMinEntry(distances);
            closestEntries.add(minEntry);
            distances.remove(minEntry.getKey());
        }
        // return the ArrayList
        return closestEntries;
    }

    // find the most common genre among the topK smallest distances
    public String getGenre(ArrayList<Entry<String, Double>> closestEntries) {

        // keep count of genres in nearest neighbors
        int adventure = 0;
        int romance = 0;
        int horror = 0;
        
        for (int i = 0; i < closestEntries.size(); i++) {
            String title = closestEntries.get(i).getKey();
            String genre = bookGenres.get(title);
            if (genre.equals("Adventure")) {
                adventure++;
            } else if (genre.equals("Romance")) {
                romance++;
            } else {
                horror++;
            }
        }

        Random rand = new Random();
        // if genre counts are equal, pick random genre
        if (adventure == romance && adventure == horror) {
            int value = rand.nextInt(3);
            if (value == 0) {
                return "Adventure";
            } else if (value == 1) {
                return "Romance";
            } else {
                return "Horror";
            }
        }

        if (adventure > romance && adventure > horror) {
            // classified genre is adventure
            return "Adventure";
        } else if (romance > adventure && romance > horror) {
            // classifier genre is romance
            return "Romance";
        } else if (horror > adventure && horror > romance) {
            // classifier genre is horror
            return "Horror";
        } else if (adventure == romance) {
            // top genre tied between adv and rom
            int value = rand.nextInt(2);
            if (value == 0) {
                return "Adventure";
            } else {
                return "Romance"; 
            }
        } else if (adventure == horror) {
            // top genre tied between adv and hor
            int value = rand.nextInt(2);
            if (value == 0) {
                return "Adventure";
            } else {
                return "Horror"; 
            }
        } else {
            // top genre tied between rom and hor
            int value = rand.nextInt(2);
            if (value == 0) {
                return "Romance";
            } else {
                return "Horror"; 
            } 
        }
    }

    public String[] getRecommendations(ArrayList<Entry<String, Double>> closestEntries) {
        String[] bookRecs = new String[closestEntries.size()];

        for (int i = 0; i < closestEntries.size(); i++) {
            bookRecs[i] = closestEntries.get(i).getKey();
        }
        return bookRecs;
    }

} // end of class