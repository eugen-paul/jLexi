package net.eugenpaul.jlexi.component.text.format.compositor.texttoword;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextCharV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextSyllableV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextWordV2;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

@Builder
public class TextElementToFullWordV2 implements TextToWordsCompositorV2 {

    @Override
    public TextElementToFullWordV2 copy() {
        return new TextElementToFullWordV2();
    }

    @Override
    public <T extends TextStructureV2> List<TextWordV2> compose(Iterator<T> iterator, ResourceManager storage) {
        List<TextWordV2> responseWords = new LinkedList<>();
        TextWordV2 currentWord = new TextWordV2();
        TextSyllableV2 currentSyllable = new TextSyllableV2();

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
                    currentWord = new TextWordV2();
                    currentSyllable = new TextSyllableV2();
                    currentSyllable.addElement(element);
                    currentWord.addSyllable(currentSyllable);
                    lastElementIsText = false;
                }

            } else {
                if (!currentWord.isEmpty()) {
                    responseWords.add(currentWord);
                    currentWord = new TextWordV2();
                }

                currentSyllable = new TextSyllableV2();
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

    private boolean isText(TextStructureV2 element) {
        boolean currentElementIsText;
        if (element instanceof TextCharV2) {
            TextCharV2 textChar = (TextCharV2) element;
            currentElementIsText = isLetterText(textChar);
        } else {
            currentElementIsText = false;
        }
        return currentElementIsText;
    }

    private boolean isLetterText(TextCharV2 c) {
        return Character.isLetter(c.getC()) || Character.isDigit(c.getC());
    }
}
