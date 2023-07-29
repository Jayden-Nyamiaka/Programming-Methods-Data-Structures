package edu.caltech.cs2.lab05;

import java.util.List;
import java.util.ArrayList;

public class AnagramGenerator {
    public static void printPhrases(String phrase, List<String> dictionary) {
        LetterBag bag = new LetterBag(phrase);
        List<String> current = new ArrayList<String>();

        if (bag.isEmpty()) {
            System.out.println(phraseToString(current));
            return;
        }

        for (int c = 'a'; c <= 'z'; c++){
            if (bag.hashCode( (char) c) > 0) {

            }
            System.out.println();
        }
    }

    //public static void collectPhrases(String s);

    public static void printWords(String word, List<String> dictionary) {

    }

    public static String phraseToString(List<String> phrase) {
        String out = "";
        for (int i = 0; i < phrase.size(); i++) {
            if (i != 0) {
                out += " ";
            }
            out += phrase.get(i);
        }
        return out;
    }
}
