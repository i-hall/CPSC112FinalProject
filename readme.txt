CPSC 112 Final Project
Hannah Forbes, I'noli Hall, Garrett Ham, Aranza Rodriguez

Title: Book Genre Classifier & Book Recommender using K Nearest Neighbors

Instructions for Compiling, Running, and Using our Program:
1. Compile: BookGenreGUI.java, our GUI
2. Select book name and number of recommendations from drop down menus
3. Press Go Button
4. View your classified genre and recommendations
5. Press Reset Button to try a different book


Total Time Spent on This Project:
- Garrett - 15 hrs
- Hannah - 24 hrs
- I'noli - 27 hrs
- Aranza - 20 hrs
- TOTAL = 86 hrs


Information on the files:

stopwords.txt   
    .txt file of stop words to not consider in most common words

AdventureLearner.java
    DEPENDENCIES:
    - Adventure File (folder with .txt files of Adventure Books for training)

    INPUT: stopwords.txt
    OUTPUT: top 100 most common words to appear in Adventure File .txt files

RomanceLearner.java
    DEPENDENCIES:
    - Romance File (folder with .txt files of Romance Books for training)

    INPUT: stopwords.txt
    OUTPUT: top 100 most common words to appear in Romance File .txt files

HorrorLearner.java
    DEPENDENCIES:
    - Horror File (folder with .txt files of Horror Books for training)

    INPUT: stopwords.txt
    OUTPUT: top 100 most common words to appear in Horror File .txt files

featurewords.txt    
    List of feature words to count in our training books and test book
    We used the Learner .java files above to pick the best feature words to differentiate our genres

FeatureWordCounter.java
    DEPENDENCIES:
    - featurewords.txt

    INPUT: .txt file name
    This class takes in a book .txt file, counts the occurrences of each feature word, and divides
    the counts by the total number of words in the file to give us the proportion. The proportion
    for each feature word is stored in a double array for use in later classes.

TrainingSet.java
    DEPENDENCIES:
    - trainingFiles (folder with 90 book .txt files)
    - trainingset.txt
    - trainingsetgenres.txt

    The trainingFiles folder stores 90 book .txt files used by TrainingSet.java. For each book, the 
    title and genre are stored, and FeatureWordCounter.java is used to return the proportion data for
    our feature words. An array list with feature word values for each training book is created for 
    access in later classes.

TestSet.java
    DEPENDENCIES:
    - testFiles (folder with 15 book .txt files)
    - testset.txt
    - testsetgenres.txt

    TestSet.java is the same format as TrainingSet.java, but for a set of 15 books that will be used to
    test the accuracy of our classifier. Similarly it stores title, genre, and feature word values data.

KNNClassifierEvaluator.java
    DEPENDENCIES:
    - TrainingSet.java
    - TestSet.java
    - FeatureWordCounter.java

    INPUT: value for k, desired number of nearest neighbors
    OUTPUT: accuracy of classifier (in %)

    This classifier evaluator calls on the data from TrainingSet.java and TestSet.java. For each book in 
    the TestSet, it's euclidean distance with each training set book is calculated. There is a getTopK 
    method to return the nearest neighbors with the shortest euclidean distance. The genre with the majority
    in this group of nearest neighbors is the classified genre. The classified genre is compared to the true
    genre for each test book, and the accuracy is calculated: (# matching / Total # book in test set) * 100.


KNNClassifier.java
    DEPENDENCIES:
    - TrainingSet.java
    - FeatureWordCounter.java

    INPUT: .txt file name; integer value for k, number of nearest neighbors
    OUTPUT: classified genre based on nearest neighbors; book recommendations (titles of nearest neighbors)

    This classifier takes in a .txt file for a book, calculates the euclidean distance between it and
    all the books in the training set, get the top k books (i.e., k training books with the smallest euclidean 
    distances), finds the most common genre among these nearest neighbors in order to classify the genre (if 
    there's a tie between two genres, it picks one randomly), and returns the names of these nearest neighbors
    as a recommendation list for similar readings.


BookGenreGUI.java (OUR GUI)
    DEPENDENCIES:
    - KNNClassifier.java
    - FeatureWordCounter.java
    - bookGenres.txt
    - bookImage3.jpeg
    - RandomWord.java
    - testFiles_filenames.txt
    
    This Graphic User Interface provides a visual for inputting a .txt file name and integer value for k. It 
    then provides an output screen with the classified genre and k nearest neighbor book recommendations. 
    It has a reset button to reset the classifier and try again with a different book and different k value.

    A list of genres were used in an artistic word design, in addition to a background image.

    We created a JFrame with JPanels and added JComponents such as JLabel, JButton, and JComboBox. In order
    to position the components, we utilized a BoxLayout with center alignment. We adjusted the sizes to fit
    the text comfortably. We adjusted the opacity level to view our background image and get our desired
    visual effect.

    We used action listeners for the "Go" and "Reset" buttons. It was challenging connecting the action
    listeners to our KNNClassifier (i.e., knowing the right time to implement code for our desired results). 