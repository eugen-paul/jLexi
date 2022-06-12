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

    @Override
    public List<TextWord> compose(Iterator<T> iterator) {
        List<TextWord> responseWords = new LinkedList<>();
        TextWord currentWord = new TextWord();
        List<TextElement> currentSyllable = new LinkedList<>();

        boolean lastElementIsText = false;
        boolean lastElementIsVowel = false;

        while (iterator.hasNext()) {
            T element = iterator.next();
            if (lastElementIsText) {

                boolean currentElementIsText = isText(element);
                boolean currentElementIsVowel = isVowel(element);

                boolean isNewWord = !lastElementIsText || !currentElementIsText;
                boolean isNewSyllable = isNewWord //
                        || (!currentElementIsVowel && lastElementIsVowel) // two vowels in a row
                ;

                if (isNewWord) {
                    responseWords.add(currentWord);
                    currentWord = new TextWord();
                }

                if (isNewSyllable) {
                    currentSyllable = new LinkedList<>();
                    currentWord.addSyllable(currentSyllable);
                }
                currentSyllable.add(element);

                lastElementIsText = currentElementIsText;
                lastElementIsVowel = currentElementIsVowel;

            } else {
                if (!currentWord.isEmpty()) {
                    responseWords.add(currentWord);
                    currentWord = new TextWord();
                }

                currentSyllable = new LinkedList<>();
                currentSyllable.add(element);
                currentWord.addSyllable(currentSyllable);

                lastElementIsText = isText(element);
                lastElementIsVowel = isVowel(element);
            }
        }

        if (!currentWord.isEmpty()) {
            responseWords.add(currentWord);
        }

        return responseWords;
    }

    private boolean isVowel(T element) {
        if (element instanceof TextChar) {
            TextChar textChar = (TextChar) element;
            char testC = Character.toLowerCase(textChar.getC());
            return testC == 'a' //
                    || testC == 'e' //
                    || testC == 'i' //
                    || testC == 'o' //
                    || testC == 'u' //
            ;
        }

        return false;
    }

    private boolean isText(T element) {
        boolean currentElementIsText;
        if (element instanceof TextChar) {
            TextChar textChar = (TextChar) element;
            currentElementIsText = isLetterText(textChar);
        } else {
            currentElementIsText = false;
        }
        return currentElementIsText;
    }

    private boolean isLetterText(TextChar c) {
        return Character.isLetter(c.getC()) || Character.isDigit(c.getC());
    }
}
