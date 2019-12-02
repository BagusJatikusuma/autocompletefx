package id.nukuba;

import id.nukuba.util.TreeDictionary;
import id.nukuba.util.TreeDictionaryMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.*;

/**
 * This class is a TextField which implements an "autocomplete" functionality, based on a supplied list of entries.
 * @author Caleb Brinkman
 *
 * https://gist.github.com/floralvikings/10290131
 * revision : https://stackoverflow.com/questions/36861056/javafx-textfield-auto-suggestions
 *
 */
public class AutoCompleteTextField extends TextField {

    /** The existing autocomplete entries. */
    private final List<String> entries;
    /** dictionary used for autocomplete **/
    private TreeDictionary dictionary;

    /** The popup used to select an entry. */
    private ContextMenu entriesPopup;

    public AutoCompleteTextField() {
        super();
        entries = new ArrayList<>();
        dictionary = new TreeDictionaryMap();
        entriesPopup = new ContextMenu();

        dictionary.addWord( "Bandung" );
        dictionary.addWord( "Balik Papan" );
        dictionary.addWord( "Banjarmasin" );
        dictionary.addWord( "Jakarta" );

        textProperty().addListener((observableValue, previouseText, newText) -> {
            String enteredText = getText();

            if ( enteredText == null || enteredText.isEmpty() ) {
                entriesPopup.hide();
            }
            else {
                //filter all possible suggestions depends on "Text", case insensitive
                List<String> filteredEntries = dictionary.traverseWord(enteredText);
                //some suggestions are found
                if (!filteredEntries.isEmpty()) {
                    //build popup - list of "CustomMenuItem"
                    populatePopup(filteredEntries, enteredText);

                    if (!entriesPopup.isShowing()) { //optional
                        entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0); //position of popup
                    }
                    //no suggestions -> hide
                } else {
                    entriesPopup.hide();
                }

            }

        });

        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                entriesPopup.hide();
            }
        });

    }

    /**
     * Populate the entry set with the given search results. Display is limited to 10 entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult, String searchRequest) {
        //List of "suggestions"
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //List size - 10 or founded suggestions count
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        System.out.println("count "+count);

        //Build list as set of labels
        for (int i = 0; i < count; i++) {
            final String result = searchResult.get(i);

            //label with graphic (text flow) to highlight founded subtext in suggestions
            Label entryLabel = new Label();
            entryLabel.setGraphic(buildTextFlow(result, searchRequest));
            entryLabel.setPrefHeight(10);  //don't sure why it's changed with "graphic"
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            menuItems.add(item);

            //if any suggestion is select set it into text and close popup
            item.setOnAction(actionEvent -> {
                setText(result);
                positionCaret(result.length());
                entriesPopup.hide();
            });

        }

        //"Refresh" context menu
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

    /**
     * Build TextFlow with selected text. Return "case" dependent.
     *
     * @param text - string with text
     * @param filter - string to select in text
     * @return - TextFlow
     */
    public TextFlow buildTextFlow(String text, String filter) {
        int filterIndex = text.toLowerCase().indexOf(filter.toLowerCase());

        Text textBefore = new Text(text.substring(0, filterIndex));
        Text textAfter = new Text(text.substring(filterIndex + filter.length()));
        Text textFilter = new Text(text.substring(filterIndex,  filterIndex + filter.length())); //instead of "filter" to keep all "case sensitive"
        textFilter.setFill(Color.BLUE);
        textFilter.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        return new TextFlow(textBefore, textFilter, textAfter);
    }

}
