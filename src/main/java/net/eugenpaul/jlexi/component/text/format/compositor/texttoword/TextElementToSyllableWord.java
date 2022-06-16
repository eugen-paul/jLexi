package net.eugenpaul.jlexi.component.text.format.compositor.texttoword;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import net.eugenpaul.jlexi.component.text.format.element.TextChar;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.element.TextSyllable;
import net.eugenpaul.jlexi.component.text.format.element.TextWord;
import net.eugenpaul.jlexi.component.text.format.element.TextWordBreak;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

@Builder
public class TextElementToSyllableWord<T extends TextElement> implements TextToWordsCompositor<T> {

    @Override
    public TextToWordsCompositor<T> copy() {
        return new TextElementToSyllableWord<>();
    }

    @Override
    public List<TextWord> compose(Iterator<T> iterator, ResourceManager storage) {
        List<TextWord> responseWords = new LinkedList<>();
        TextWord currentWord = new TextWord();
        TextSyllable currentSyllable = new TextSyllable();

        boolean lastElementIsText = false;
        boolean lastElementIsVowel = false;

        while (iterator.hasNext()) {
            T element = iterator.next();

            boolean currentElementIsText = isText(element);
            boolean currentElementIsVowel = isVowel(element);

            boolean isNewWord = !lastElementIsText || !currentElementIsText;
            boolean isNewSyllable = isNewWord //
                    || (!currentElementIsVowel && lastElementIsVowel) // two vowels in a row
            ;

            if (isNewWord && !currentWord.isEmpty()) {
                responseWords.add(currentWord);
                currentWord = new TextWord();
            }

            if (!isNewWord && isNewSyllable) {
                // add word break
                TextWordBreak wb = new TextWordBreak(//
                        null, //
                        storage, //
                        null, //
                        TextFormat.DEFAULT, //
                        TextFormatEffect.DEFAULT_FORMAT_EFFECT, //
                        element //
                );
                currentSyllable.setWordBreak(wb);
            }

            if (isNewSyllable) {
                currentSyllable = new TextSyllable();
                currentWord.addSyllable(currentSyllable);
            }
            currentSyllable.addElement(element);

            lastElementIsText = currentElementIsText;
            lastElementIsVowel = currentElementIsVowel;
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
