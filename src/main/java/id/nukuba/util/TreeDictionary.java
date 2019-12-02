package id.nukuba.util;

import java.util.List;

/**
 * Word Dictionary used of autocomplete task
 */
public interface TreeDictionary {

    /**
     * add word to dictionary
     *
     * @param word
     */
    void addWord(String word);

    /**
     * this method is used to retrieve data/word in dictionary
     * this method receive parameter of sub word ex: Ba in { Bandung, Balik Papan, dll}
     * return list of word that available which match the subword
     * return empty list if not found
     *
     * @param word
     * @return list of word
     */
    List<String> traverseWord(String word);

}
