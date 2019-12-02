package id.nukuba.util;

import java.util.Map;

public class TreeNode {
    private String key;
    private Map<String, TreeNode> childNode;
    private boolean isWord;

    public TreeNode() {
    }

    public TreeNode(Map<String, TreeNode> childNode, boolean isWord) {
        this.childNode = childNode;
        this.isWord = isWord;
    }

    public TreeNode(String key, Map<String, TreeNode> childNode, boolean isWord) {
        this.key = key;
        this.childNode = childNode;
        this.isWord = isWord;
    }

    public Map<String, TreeNode> getChildNode() {
        return childNode;
    }

    public void setChildNode(Map<String, TreeNode> childNode) {
        this.childNode = childNode;
    }

    public boolean isWord() {
        return isWord;
    }

    public void setWord(boolean word) {
        isWord = word;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
