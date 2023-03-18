import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Ethan Friesel
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];


    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    /**
     * This function will generate all words that use only chars from the letters string
     * Note - Each letter can only be used once
     */
    public void generate() {
        // Call the recursive function genWords, which will generate all words that use chars from the letters String
        // once, starting at the beginning recursive method of a blank word with a full letters string
        genWords(0, "", letters);
    }

    /**
     * This function will be called recursively to generate all words that use only chars from the letters string
     * @param start (current index)
     * @param word (the word that will be added to (or not if at end of string letters or string letters is empty))
     * @param letters (the remaining characters in the inputted string of characters)
     */
    public void genWords(int start, String word, String letters){
        // base case if all letters have been used or the index is past the length
        if (letters.length() == 0 || start == letters.length())
            return;
        // have one path skip the letter in the letters and redeclare genWords with start + 1
        genWords(start + 1, word, letters);
        // have one path add the current letter to the current word and add the word to words
        // Then, recall genWords with a string that has the current letter removed (so it is not reused)
        word = word + letters.charAt(start);
        words.add(word);
        genWords(0, word, letters.substring(0, start) + letters.substring(start + 1));
    }

    /**
     * this method will call a recursive method that will return a sorted ArrayList of Strings
     */
    public void sort() {
        //the returned ArrayList will be assigned to the words instance variable
        words = mergeSort(words);
    }

    /**
     * This function will recursively call itself until the list is sorted
     * Each call, the base case will be when an inputted ArrayList is of size one
     * The "mid" integer variable will split the inputted ArrayList into two different ArrayLists
     * Each ArrayList will call the mergeSort method on itself
     * When the two split ArrayLists are returned, they are merged and returned
     * @param words (an ArrayList of Strings)
     * @return (An ArrayList that is either of size one or merged)
     */
    public ArrayList<String> mergeSort(ArrayList<String> words){
        // base case
        if (words.size() == 1)
            return words;
        // reused variable
        int mid = words.size() / 2;
        // split two arrays in half and recursively call method
        ArrayList<String> list1 = mergeSort(new ArrayList<>(words.subList(0 , mid)));
        ArrayList<String> list2 = mergeSort(new ArrayList<>(words.subList(mid, words.size())));
        // for non-base case scenarios, after all recursive methods complete, increasingly merge each array
        return merge(list1, list2);
    }

    /**
     * This method will return a single ArrayList of Strings consisting of two inputted ArrayLists
     * @param list1 (One inputted list)
     * @param list2 (Another inputted list)
     * @return (the returned ArrayList will be sorted from least to greatest Strings using the compareTo function)
     */
    public ArrayList<String> merge(ArrayList<String> list1, ArrayList<String> list2){
        // create a new ArrayList that will house the combined sorted values of both inputted ArrayLists
        ArrayList<String> words = new ArrayList<>();
        int i = 0, j = 0;
        // while both lists still have elements
        while (i < list1.size() && j < list2.size()){
            // whichever list's current element is larger given the compareTo function
            // add that element to the grand list
            if (list1.get(i).compareTo(list2.get(j)) > 0) {
                words.add(list2.get(j));
                // after the element is added, only increment the list that had an element used in the grand list
                j++;
            }
            else {
                words.add(list1.get(i));
                i++;
            }
        }
        // after one list is used up, a while loop will iterate through the rest of the not-empty list
        while (i < list1.size()){
            words.add(list1.get(i));
            i++;
        }
        while (j < list2.size()){
            words.add(list2.get(j));
            j++;
        }
        return words;
    }


    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    /**
     * This function will check if each word in the dictionary array, and remove the word iff the word is not present
     */
    public void checkWords() {
        int i = 0;
        // use a while loop due to the fluctuating size of words ArrayList
        while (i < words.size()){
            if (!binarySearch(words.get(i)))
                words.remove(i);
            // only increment if a word was not removed
            else
                i++;
        }
    }

    /**
     * This function will call the recursive method search, which will use binary search
     * The search will find if the inputted String is present in the Dictionary Array.
     * @param s (the inputted String)
     * @return (a boolean that will return true iff the inputted String is in the dictionary)
     */
    public boolean binarySearch(String s){
        return search(s, 0, DICTIONARY_SIZE - 1);
    }

    /**
     * This function will use binary search to find if the inputted word is in the sorted dictionary array
     * @param s (the inputted string)
     * @param low (the low index of the range of possible words in the dictionary)
     * @param high (the high index of the range of possible words in the dictionary)
     * @return (a boolean true iff the target word is found in the array)
     */
    public boolean search(String s, int low, int high){
        // base case if the word is not present and the range of indexes is < 1)
        if (low > high){
            return false;
        }
        // set global med integer for no reused equations
        int med = (high + low) / 2;
        if (DICTIONARY[med].equals(s))
            return true;
        // if the compare to is greater, set the minimum index of the range to one greater than the mid
        if (DICTIONARY[med].compareTo(s) < 0)
            return search(s, med + 1, high);
        // else, make the high the med - 1
        else
            return search(s, low, med - 1);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
