//************************************************************************
//  File: RandomWord.java            Final Project
// 
//  Hannah Forbes, I'noli Hall, Garrett Ham, Aranza Rodriguez
//
//  Class: RandomWord
//
//************************************************************************
import java.util.*;
import java.io.*;

public class RandomWord extends HashMap{


    static HashMap<String, Integer> words;
    static String wordsFile = "bookGenres.txt";

    public static void main (String[] args) throws FileNotFoundException
    {
        RandomWord rw = new RandomWord();
        for(Map.Entry<String, Integer> textWord : words.entrySet())
        {
            System.out.println("Word: " + textWord.getKey() + " " + "Font size: " + textWord.getValue());
        }
    }

    public RandomWord() throws FileNotFoundException
    {
        readFile(wordsFile);
    }

    static void readFile(String filename) throws FileNotFoundException
    {   
        // Scan document's words
        Scanner scan = new Scanner(new File(filename));
        String word; 

        words = new HashMap<String, Integer>();

        while (scan.hasNext()) {
            // Initialize var for the words
            word = scan.next();
    
            // Creates an entry with the string value and the font size 
            words.put(word, randomSize(20, 70));
        }
    }

    // Generates random font size 
    public static int randomSize(int min, int max)
    {
        Random random = new Random();
        return random.nextInt(min, max +1);
    }

    public static String accessKey(Map.Entry<String, Integer> textWord)
    {
        return textWord.getKey();
    }

    public static Integer accessValue(Map.Entry<String, Integer> textWord)
    {
        return textWord.getValue();
    }

    public HashMap<String, Integer> accessHashMap()
    {
        return words; 
    }

}