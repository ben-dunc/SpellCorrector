// Benjamin Duncan
// Created June 28, 2022
// for CS 240
// spell.Dictionary.java

package spell;

import java.util.Objects;

public class Trie implements ITrie {
    private INode root;

    private int wordCount;
    private int nodeCount;

    private final char ALPHA_A = 'a';

    public Trie() {
        wordCount = 0;
        nodeCount = 1;
        root = new TrieNode(null);
    }

    public INode getRoot() {
        return root;
    }

    @Override
    public void add(String word) {
        // adding algorithm - described in videos
        // make the word lower case
        word = word.toLowerCase();

        // iterate through the string
        INode node = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            // check to see if there is not a node there already
            // if not, then create one
            if (node.getChildren()[c - ALPHA_A] == null) {
                node.getChildren()[c - ALPHA_A] = new TrieNode(node);
                nodeCount++;
            }

            node = node.getChildren()[c - ALPHA_A];
        }

        node.incrementValue();
        if (node.getValue() == 1) {
            wordCount++;
        }
    }

    // NEEDS TO BE IMPLEMENTED
    @Override
    public INode find(String word) {
        INode node = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            node = node.getChildren()[c - ALPHA_A];

            // if node is null, we need to suggest some things
            if (node == null) {
                break;
            }

            // if we find the exact word, great!
            if (i == word.length() - 1 && node.getValue() > 0) {
                return node;
            }
        }

        return null;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trie that = (Trie) o;
        if (!(wordCount == that.wordCount && nodeCount == that.nodeCount)) {
            return false;
        }

        return root.equals(((Trie) o).getRoot());
    }

    @Override
    public int hashCode() {
        int hash = nodeCount * wordCount * 31;
        for (int i = 0; i < 26; i++) {
            if (root.getChildren()[i] != null) {
                hash += i * 31;
            }
        }
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder currentWord = new StringBuilder();
        StringBuilder output = new StringBuilder();


        toString_Helper(root, currentWord, output);

        return output.toString();
    }

    private void toString_Helper(INode n, StringBuilder currentWord, StringBuilder output) {
        if (n.getValue() > 0) {
            // append the node's word to the output
            output.append(currentWord.toString());
            output.append("\n");
        }

        // recurse on all the children
        for (int i = 0; i < n.getChildren().length; i++) {
            INode child = n.getChildren()[i];
            if (child != null) {
                currentWord.append((char) (i + ALPHA_A));
                toString_Helper(child, currentWord, output);
                currentWord.deleteCharAt(currentWord.length() - 1);
            }
        }
    }
}
