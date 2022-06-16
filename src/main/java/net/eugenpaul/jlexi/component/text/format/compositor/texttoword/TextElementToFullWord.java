package net.eugenpaul.jlexi.component.text.format.compositor.texttoword;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import net.eugenpaul.jlexi.component.text.format.element.TextChar;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextSyllable;
import net.eugenpaul.jlexi.component.text.format.element.TextWord;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

@Builder
public class TextElementToFullWord<T extends TextElement> implements TextToWordsCompositor<T> {

    @Override
    public TextElementToFullWord<T> copy() {
        return new TextElementToFullWord<>();
    }

    @Override
    public List<TextWord> compose(Iterator<T> iterator, ResourceManager storage) {
        List<TextWord> responseWords = new LinkedList<>();
        TextWord currentWord = new TextWord();
        TextSyllable currentSyllable = new TextSyllable();

        boolean lastElementIsText = false;

        while (iterator.hasNext()) {
            T element = iterator.next();
            if (lastElementIsText) {

                boolean currentElementIsText = isText(element);

                if (currentElementIsText) {
                    currentSyllable.addElement(element);
                    lastElementIsText = true;
                } else {
                    responseWords.add(currentWord);
                    currentWord = new TextWord();
                    currentSyllable = new TextSyllable();
                    currentSyllable.addElement(element);
                    currentWord.addSyllable(currentSyllable);
                    lastElementIsText = false;
                }

            } else {
                if (!currentWord.isEmpty()) {
                    responseWords.add(currentWord);
                    currentWord = new TextWord();
                }

                currentSyllable = new TextSyllable();
                currentSyllable.addElement(element);
                currentWord.addSyllable(currentSyllable);

                lastElementIsText = isText(element);
            }
        }

        if (!currentWord.isEmpty()) {
            responseWords.add(currentWord);
        }

        return responseWords;
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
