package net.eugenpaul.jlexi.component.text.format.compositor.texttoword;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import net.eugenpaul.jlexi.component.text.format.element.TextChar;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextWord;

@Builder
public class TextElementToSyllableWord<T extends TextElement> implements TextToWordsCompositor<T> {

    @Override
    public TextToWordsCompositor<T> copy() {
        return new TextElementToSyllableWord<>();
    }

    // TODO
    @Override
    public List<TextWord> compose(Iterator<T> iterator) {
        List<TextWord> responseWords = new LinkedList<>();
        TextWord currentWord = new TextWord();
        List<TextElement> currentSyllable = new LinkedList<>();

        boolean lastElementIsText = false;

        while (iterator.hasNext()) {
            T element = iterator.next();
            if (lastElementIsText) {

                boolean currentElementIsText = false;
                if (element instanceof TextChar) {
                    TextChar textChar = (TextChar) element;
                    currentElementIsText = isLetterText(textChar);
                } else {
                    currentElementIsText = false;
                }

                if (currentElementIsText) {
                    currentSyllable.add(element);
                } else {
                    responseWords.add(currentWord);
                    currentWord = new TextWord();
                    currentSyllable = new LinkedList<>();
                    currentSyllable.add(element);
                    currentWord.addSyllable(currentSyllable);
                }

            } else {
                if (!currentWord.isEmpty()) {
                    responseWords.add(currentWord);
                    currentWord = new TextWord();
                }

                currentSyllable = new LinkedList<>();
                currentSyllable.add(element);
                currentWord.addSyllable(currentSyllable);

                if (element instanceof TextChar) {
                    TextChar textChar = (TextChar) element;
                    lastElementIsText = isLetterText(textChar);
                } else {
                    lastElementIsText = false;
                }
            }
        }

        if (!currentWord.isEmpty()) {
            responseWords.add(currentWord);
        }

        return responseWords;
    }

    private boolean isVowel(TextChar c) {
        char testC = Character.toLowerCase(c.getC());
        return testC == 'a' //
                || testC == 'e' //
                || testC == 'i' //
                || testC == 'o' //
                || testC == 'u' //
        ;
    }

    private boolean isLetterText(TextChar c) {
        return Character.isLetter(c.getC()) || Character.isDigit(c.getC());
    }
}
