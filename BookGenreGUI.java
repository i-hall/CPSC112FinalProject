//************************************************************************
//  File: BookGenreGUI.java            Final Project
// 
//  Hannah Forbes, I'noli Hall, Garrett Ham, Aranza Rodriguez
//
//  Class: BookGenreGUI
//
//************************************************************************
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;

import javax.swing.*;
import java.util.*;

public class BookGenreGUI extends JFrame implements ActionListener 
{
    public static JFrame frame; 
    public static JPanel panel, wordPanel, genrePanel;
    public static JComboBox dropDownText, dropDownRec;
    public static JButton resetButton;

    private static DefaultListCellRenderer listRenderer;

    public static final int SIZEX = 900;
    public static final int SIZEY = 900;

    public static String wordString; 
    public static int fontSize;

    public static final String TEXT_FILES = "testFiles_filenames.txt";
    public static String textNames[];
    public static String[] kValues = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public static String testFileName;
    public static int k;
    public static double[] testFeatures;
    public static ArrayList<Entry<String, Double>> nearestNeighbors;

    public static void main (String[] args) throws FileNotFoundException {
        readFile(TEXT_FILES);
        BookGenreGUI cp = new BookGenreGUI();
    }

    // Constructor 
    public BookGenreGUI() throws FileNotFoundException {
        // initialize panels and frame
        wordPanel = new JPanel();
        panel = new JPanel();
        frame = new JFrame();

        // genre word cloud panel
        loadBackground();
        wordPanel.setBackground(new Color(255, 255, 255, 75));
        wordPanel.setLayout(new FlowLayout());
        wordPanel.setBounds(0, 275, 900, 625);

        // initialize listRenderer
        listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);

        // Create title
        JLabel title = newLabel("Discover Your Book Genre", 50, true);
        title.setForeground(new Color(13, 148, 106, 255));

        // Create user functions
        JLabel book = newLabel("Please select a book.", 20, true);
        book.setForeground(new Color(102, 0, 206, 255));
        book.setMaximumSize(new Dimension(400, 100));

        dropDownText = new JComboBox(textNames);
        dropDownText.setRenderer(listRenderer);
        dropDownText.setMaximumSize(new Dimension(325, 80));
 
        JLabel neighbor = newLabel("How many recommendations would you like?", 20, true);
        neighbor.setForeground(new Color(102, 0, 206, 255));
        neighbor.setMaximumSize(new Dimension(400, 100));

        dropDownRec = new JComboBox(kValues);
        dropDownRec.setRenderer(listRenderer);
        dropDownRec.setMaximumSize(new Dimension(80, 80));

        JButton goButton = new JButton("Go");
        goButton.setPreferredSize(new Dimension(75,35));
        goButton.addActionListener(this);

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        book.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(book);
        panel.add(dropDownText);
        neighbor.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(neighbor);
        panel.add(dropDownRec);
        goButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(goButton);
        
        // Format panel
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBounds(0, 0, 900, 274);

        JLabel image = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("bookImage3.jpeg")));
        
        // frame.add(panel, null);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(wordPanel, null);
        frame.add(image); 
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SIZEX,SIZEY);
        frame.setTitle("Book Genre Classifier");
        frame.setVisible(true);
        frame.setLayout(null);
    }

    public static void loadBackground() throws FileNotFoundException {
        RandomWord randomWord = new RandomWord();
        HashMap<String, Integer> words = randomWord.accessHashMap();
        
        for (Map.Entry<String, Integer> textWord : words.entrySet()) {

            JLabel word = randWord(textWord);

            // Set the position of the label here 
            Dimension size = word.getPreferredSize();

            word.setBounds(randCoordinate(SIZEX - size.width), randCoordinate(SIZEY - size.height), size.width, size.height);

            wordPanel.add(word);
        }
    }

    //Creates the JLabel of each word and places it somewhere random on the screen 
    public static JLabel randWord(Map.Entry<String, Integer> textWord) throws FileNotFoundException {

        String wordString = textWord.getKey();
        int fontSize = textWord.getValue();

        JLabel word = newLabel(wordString, fontSize, false);

        return word; 
    }

    // Creates random coordinate
    public static int randCoordinate(int max) {
        Random random = new Random();
        return random.nextInt(max + 1);
    }
 
    // Creates a new label 
    public static JLabel newLabel(String label, int fontSize, boolean center) {
        JLabel jlabel = new JLabel(label);
        jlabel.setFont(new Font("Serif", Font.BOLD, fontSize));
        jlabel.setOpaque(false);

        if (center) {
            jlabel.setHorizontalAlignment(JLabel.CENTER);
        } else {
            jlabel.setForeground(randomColor());
        }
        return jlabel;
    }

    public static Color randomColor() {
        Random rand = new Random();
        int r = rand.nextInt(255) + 1;
        int g = rand.nextInt(255) + 1;
        int b = rand.nextInt(255) + 1;
        return new Color (r, g, b);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        testFileName = "testFiles/" + (String) dropDownText.getSelectedItem();
        k = Integer.parseInt((String) dropDownRec.getSelectedItem());;
    
        // get test file features        
        FeatureWordCounter wc = new FeatureWordCounter(testFileName);
        testFeatures = wc.featureWordValues();
    
        // calculate distances between test book and training books
        KNNClassifier kNN = new KNNClassifier();
        kNN.euclideanDistance(testFeatures);
            
        // determine the k nearest neighbors
        nearestNeighbors = kNN.getTopK(k);
         
        // Keep the frame as it is until we get valid responses
        frame.dispose();

        // initialize new JPanel and JFrame
        genrePanel = new JPanel();
        frame = new JFrame();

        String genreClassification = "Your classified genre is: " + kNN.getGenre(nearestNeighbors);
        JLabel genreLabel = newLabel(genreClassification, 50, true);

        // determine the names of nearest neighbors for recommendations
        JLabel recLabel = newLabel("Here are " + k + " similar books:", 30, true);
        String[] bookRecs = kNN.getRecommendations(nearestNeighbors);
        JLabel[] bookRecsLabel = new JLabel[k];
        for (int i = 0; i < k; i++) {
            bookRecsLabel[i] = newLabel(bookRecs[i], 20, true);
        }

        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(80,35));
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                try {
                    readFile(TEXT_FILES);
                    BookGenreGUI cp = new BookGenreGUI();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        genrePanel.add(resetButton);

        genreLabel.setForeground(new Color(13, 148, 106, 255));
        genreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        genreLabel.setMaximumSize(new Dimension(900, 100));
        genrePanel.add(genreLabel);

        recLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        recLabel.setMaximumSize(new Dimension(900, 100));
        genrePanel.add(recLabel);
        for (int i = 0; i < k; i++) {
            bookRecsLabel[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            bookRecsLabel[i].setMaximumSize(new Dimension(900, 50));
            genrePanel.add(bookRecsLabel[i]);
        }

        genrePanel.setOpaque(false);
        genrePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        genrePanel.setLayout(new BoxLayout(genrePanel, BoxLayout.PAGE_AXIS));
        genrePanel.setBounds(0, 100, 900, 900);

        JLabel image = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("bookImage3.jpeg")));

        frame.add(genrePanel, BorderLayout.CENTER);
        frame.add(image); 

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SIZEX,SIZEY);
        frame.setTitle("Book Genre Classifier");
        frame.setVisible(true);
        frame.setLayout(null);
      
    }

    public static void readFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scan = new Scanner(file);

        int arraySize = 0; 
        while (scan.hasNextLine()) {
            arraySize++; 
            scan.nextLine();
        }   

        //Sends the pointer back to the top 
        scan = new Scanner(file);
        
        textNames = new String[arraySize];

        for (int i = 0; i < arraySize; i++) {
            textNames[i] = scan.nextLine();
        }
    }

} // end of class