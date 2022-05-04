//************************************************************************
//  File: KNNClassifierEvaluator.java            Final Project
// 
//  Hannah Forbes, I'noli Hall, Garrett Ham, Aranza Rodriguez
//
//  Class: KNNClassifierEvaluator
//
//************************************************************************
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class KNNClassifierEvaluator {

    public static String[] trainBookTitles;
    public static HashMap<String, String> trainBookGenres;
    public static HashMap<String, double[]> trainBookFeatures;

    public static String[] testBookTitles;
    public static HashMap<String, String> testBookGenres;
    public static HashMap<String, double[]> testBookFeatures;

    public static HashMap<String, Double> bookDistances;

    public static int genresCorrect;
    public static double classifierAccuracy;

    public static void main(String[] args) {

        // get training set data
        TrainingSet train = new TrainingSet();
        trainBookTitles = train.bookTitles();
        trainBookGenres = train.bookGenres();
        trainBookFeatures = train.bookFeatures();

        // get test set data
        TestSet test = new TestSet();
        testBookTitles = test.bookTitles();
        testBookGenres = test.bookGenres();
        testBookFeatures = test.bookFeatures();
 
        // number of nearest neighbors
        int k = Integer.parseInt(args[0]);

        // classify test book with training books
        String[] classifiedGenres = new String[testBookTitles.length];
        String[] trueGenres = new String[testBookTitles.length];
        genresCorrect = 0;
        for (int i = 0; i < testBookTitles.length; i++) {
            euclideanDistance(testBookFeatures.get(testBookTitles[i]));
            classifiedGenres[i] = getGenre(getTopK(k));
            trueGenres[i] = testBookGenres.get(testBookTitles[i]);
            System.out.println(classifiedGenres[i] + ", " + trueGenres[i]);
            if (classifiedGenres[i].equals(trueGenres[i])) {
                genresCorrect++;
            }
        }

        // calculate accuracy of classifier
        classifierAccuracy = 100.0 * genresCorrect / testBookTitles.length;
        System.out.printf("Classifier Accuracy: %.2f%% \n", classifierAccuracy);
    }

    public static void euclideanDistance(double[] testFeatures) {

        bookDistances = new HashMap<String, Double>();

        // initialize sum 
        double totalSum = 0;

        for (int i = 0; i < trainBookTitles.length; i++) { // each training book
            for (int j = 0; j < trainBookFeatures.get(trainBookTitles[i]).length; j++) { // each feature
                // features of current training book
                double[] trainFeatures = trainBookFeatures.get(trainBookTitles[i]);
                // calculate squared difference and sum all features together
                double distance = (testFeatures[j] - trainFeatures[j]) * (testFeatures[j] - trainFeatures[j]);
                totalSum += distance;
            }
            // store distance (sqrt of sum) in HashMap & reinitialize totalSum for next book
            bookDistances.put(trainBookTitles[i], Math.sqrt(totalSum));
            totalSum = 0;
        }
    }

    private static Entry<String, Double> getMinEntry(HashMap<String, Double> distances) {
    
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
    public static ArrayList<Entry<String, Double>> getTopK(int k) {

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
    public static String getGenre(ArrayList<Entry<String, Double>> closestEntries) {

        // keep count of genres in nearest neighbors
        int adventure = 0;
        int romance = 0;
        int horror = 0;
        
        for (int i = 0; i < closestEntries.size(); i++) {
            String title = closestEntries.get(i).getKey();
            String genre = trainBookGenres.get(title);
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

} // end of class