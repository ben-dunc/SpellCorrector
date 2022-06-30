// Benjamin Duncan
// Created June 28, 2022
// for CS 240
// spell.Node.java

package spell;

import java.util.Arrays;
import java.util.Objects;

public class TrieNode implements INode {
    private int count;
    private INode [] children;
    private final int NUM_ALPHA = 26;
    private INode parentNode;

    public TrieNode(INode parent) {
        children = new TrieNode[NUM_ALPHA];
        count = 0;
        parentNode = parent;
    }

    @Override
    public int getValue() {
        return count;
    }

    public INode getParent() {
        return parentNode;
    }

    @Override
    public void incrementValue() {
        count++;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrieNode node = (TrieNode) o;
        if (count != node.count) {
            return false;
        }

        // iterate through childeren
        for (int i = 0; i < 26; i++) {
            if (children[i] == null && ((TrieNode) o).getChildren()[i] == null) {
                continue;
            } else if (children[i] == null) {
                return false;
            } else if (((TrieNode) o).getChildren()[i] == null) {
                return false;
            }

            if (!children[i].equals(((TrieNode) o).getChildren()[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = count * 31;

        for (int i = 0; i < 26; i++) {
            INode child = children[i];
            if (child != null) {
                hash = hash + i;
                hash = hash + child.hashCode();
            }
        }

        return hash;
    }

    @Override
    public String toString() {
        return "spell.Node{" +
                "count=" + count +
                ", children=" + Arrays.toString(children) +
                ", NUM_ALPHA=" + NUM_ALPHA +
                '}';
    }
}
