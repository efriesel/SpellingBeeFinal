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

    private static final int MAX_LETTERS = 5;

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.

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
        if (letters.length() == 0 || start == letters.length())
            return;
//        if (word.length() == MAX_LETTERS || start == letters.length())
//            return;
        if (word.length() >= 3 && word.charAt(word.length() - 1) == word.charAt(word.length() - 2) && word.charAt(word.length() - 2) == word.charAt(word.length() - 3))
            return;
        genWords(start + 1, word, letters);
        word = word + letters.charAt(start);
        words.add(word);
//        genWords(0, word, letters);
        genWords(0, word, letters.substring(0, start) + letters.substring(start + 1));
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        words = mergeSort(words);
    }
    public ArrayList<String> mergeSort(ArrayList<String> words){
        if (words.size() == 1)
            return words;
        int mid = words.size() / 2;
        ArrayList<String> list1 = mergeSort(new ArrayList<>(words.subList(0 , mid)));
        ArrayList<String> list2 = mergeSort(new ArrayList<>(words.subList(mid, words.size())));
        return merge(list1, list2);
    }
    public ArrayList<String> merge(ArrayList<String> list1, ArrayList<String> list2){
        ArrayList<String> words = new ArrayList<>();
        int i = 0, j = 0;
        while (i < list1.size() && j < list2.size()){
            if (list1.get(i).compareTo(list2.get(j)) > 0) {
                words.add(list2.get(j));
                j++;
            }
            else {
                words.add(list1.get(i));
                i++;
            }
        }
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

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        int i = 0;
        while (i < words.size()){
            //words.get(i).indexOf(letters.charAt(0)) == -1 || words.get(i).length() < 5 ||
            if (!binarySearch(words.get(i)))
                words.remove(i);
            else
                i++;
        }
    }
    public boolean binarySearch(String s){
        return search(s, 0, DICTIONARY_SIZE - 1);
    }

    public boolean search(String s, int low, int high){
        if (low > high){
            return false;
        }
        int med = (high + low) / 2;
        String current = DICTIONARY[med];
        if (current.equals(s))
            return true;
        if (current.compareTo(s) < 0)
            return search(s, med + 1, high);
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
