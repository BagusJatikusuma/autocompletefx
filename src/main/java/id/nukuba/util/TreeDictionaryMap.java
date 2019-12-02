package id.nukuba.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Word Dictionary used of autocomplete task
 * use HashMap for word collection and custom TRIE structure to get the words
 *
 * @author Bagus Jatikusuma
 *
 */
public class TreeDictionaryMap implements TreeDictionary {
    private Map<String, TreeNode> roots;

    public TreeDictionaryMap() {
        roots = new HashMap<>();
    }

    public void addWord( String word ) {

        TreeNode node = roots.get( word.substring(0,1) );

        if ( node == null ) {
            node = new TreeNode(word.substring(0, 1), new HashMap<>(), word.length() == 1);
            roots.put( word.substring( 0, 1), node );
        }

        if ( word.length() > 1 )
            addNode( node, word.substring( 1, word.length() ) );

    }

    /**
     *
     * create node and link it with its childs to build the tree of words
     *
     * @param node
     * @param word
     */
    private void addNode( TreeNode node, String word ) {

        String key = word.substring(0,1);
        TreeNode nodeTemp = node.getChildNode().get( key );

        if ( nodeTemp != null ) {
            if ( word.length() > 1)
                addNode( nodeTemp, word.substring( 1, word.length() ) );
        }
        else {
            nodeTemp = new TreeNode(
                    key,
                    new HashMap<>(),
                    word.length() == 1 );
            node.getChildNode().put(
                    key,
                    nodeTemp );

            if ( word.length() > 1)
                addNode( nodeTemp, word.substring( 1, word.length() ) );
        }

    }

    /**
     * @<code>node</code> is used for testing only
     *
     * @param node
     */
    private void traverseNode( TreeNode node, String previousWord, List<String> wordsResult ) {
        //check if node is the leave of the tree
        if ( node.getChildNode().isEmpty() ) {
            wordsResult.add(previousWord);
            return;
        }

        //iterate over node child
        node.getChildNode()
                .forEach((s, tn) -> {
                    String s2 = previousWord.concat(s);

                    traverseNode(tn, s2, wordsResult);

                });
    }

    /**
     *
     * this method is used to get starting node/level based of sub word
     *
     * @param word
     * @param previousNode
     * @return
     */
    private TreeNode getStartingNode( String word, TreeNode previousNode ) {
        if ( word.length() > 1 ) {
            //if found
            if ( previousNode.getChildNode().get(word.substring(1, 2)) != null ) {
                return getStartingNode(
                            word.substring(1, word.length()),
                            previousNode.getChildNode().get(word.substring(1, 2)));
            }
            //if not found
            else {
                return null;
            }

        }
        return previousNode;

    }

    public List<String> traverseWord( String word ) {

        List<String> wordAvailable = new ArrayList<>();
        //get first letter of word
        //get the starting node
        TreeNode node = roots.get( word.substring( 0, 1 ) );
        node = getStartingNode( word, node );

        if ( node != null ) {
            traverseNode( node, word, wordAvailable );
        }

        return wordAvailable;

    }

}
