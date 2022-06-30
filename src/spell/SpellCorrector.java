// Benjamin Duncan
// Created June 28, 2022
// for CS 240
// spell.SpellCorrector.java

package spell;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class SpellCorrector implements ISpellCorrector {
    private ITrie dictionary;
    private final char ALPHA_A = 'a';

    public SpellCorrector() {
        this.dictionary = new Trie();
    }

    // USE IOEXCEPTION HERE!
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        Scanner scanner = new Scanner(new File(dictionaryFileName));

        // parse dictionary with scanner
        while (scanner.hasNext()) {
            String word = scanner.next();

            // add word to trie
            dictionary.add(word);
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();
        INode endNode = null;

        // check if word is there
        endNode = dictionary.find(inputWord);
        if (endNode != null && endNode.getValue() > 0) {
            return reconstructFromEndNode(endNode);
        }

        // check 1 edit distance matches
        String words = generateOneEditDistanceWords(inputWord);
        Scanner scanner = new Scanner(words);

        String winningString = "";
        while (scanner.hasNext()) {
            String w = scanner.next();
            INode node = dictionary.find(w);

            if (node != null) {
                if (endNode == null) {
                    endNode = node;
                    winningString = w;
                } else if (node.getValue() > endNode.getValue()) {
                    endNode = node;
                    winningString = w;
                } else if (node.getValue() == endNode.getValue() && w.compareTo(winningString) < 0) {
                    endNode = node;
                    winningString = w;
                }
            }
        }

        if (endNode != null && endNode.getValue() > 0) {
            return winningString;
        }

        // check for 2 edit distance matches
        words = generateOneEditDistanceWords(words);
        scanner = new Scanner(words);

        winningString = "";
        while (scanner.hasNext()) {
            String w = scanner.next();
            INode node = dictionary.find(w);

            if (node != null) {
                if (endNode == null) {
                    endNode = node;
                    winningString = w;
                } else if (node.getValue() > endNode.getValue()) {
                    endNode = node;
                    winningString = w;
                } else if (node.getValue() == endNode.getValue() && w.compareTo(winningString) < 0) {
                    endNode = node;
                    winningString = w;
                }
            }
        }

        if (endNode != null && endNode.getValue() > 0) {
            return winningString;
        }

        // if no matches, return null
        return null;
    }

    public String generateOneEditDistanceWords(String words) {
        Scanner scanner = new Scanner(words);
        StringBuilder builder = new StringBuilder();

        // iterate through strings
        while (scanner.hasNext()) {
            String str = scanner.next();

            // do deletion distance words
            for (int i = 0; i < str.length(); i++) {
                builder.append(str.substring(0, i));
                builder.append(str.substring(i + 1));
                builder.append(" ");
            }
            builder.append("\n");

            // do transposition distance words
            for (int i = 0; i < str.length() - 1; i++) {
                builder.append(str.substring(0, i)); // will this break it?
                builder.append(str.charAt(i + 1));
                builder.append(str.charAt(i));
                builder.append(str.substring(i + 2)); // will this break it?
                builder.append(" ");
            }
            builder.append("\n");

            // do alteration distance words
            for (int i = 0; i < str.length(); i++) {
                for (int j = 0; j < 26; j++) {
                    builder.append(str.substring(0, i));
                    builder.append((char) (ALPHA_A + j));
                    builder.append(str.substring(i + 1));
                    builder.append(" ");
                }
            }
            builder.append("\n");

            // do insertion distance wrods
            for (int i = 0; i < str.length() + 1; i++) {
                for (int j = 0; j < 26; j++) {
                    builder.append(str.substring(0, i));
                    builder.append((char) (ALPHA_A + j));
                    if (i < str.length()) {
                        builder.append(str.substring(i));
                    }
                    builder.append(" ");
                }
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    private String reconstructFromEndNode(INode endNode) {
        TrieNode node = (TrieNode) endNode;

        // rebuild string with last node
        StringBuilder suggestedWord = new StringBuilder();
        TrieNode parentNode = (TrieNode) node.getParent();
        while (parentNode != null) {
            // iterate through nodes and check if equals.
            int i = 0;
            while (parentNode.getChildren()[i] == null || !parentNode.getChildren()[i].equals(node)) {
                i++;
            }

            // append to stringbuilder
            suggestedWord.append((char) (i + ALPHA_A));

            // assign new parent
            node = parentNode;
            parentNode = (TrieNode) node.getParent();
        }

        // reverse return
        return suggestedWord.reverse().toString();
    }
}
