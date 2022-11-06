package net.eugenpaul.jlexi.component.text.format.compositor.texttoword;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextCharV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextSyllableV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextWordBreakV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextWordV2;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

@Builder
public class TextElementToSyllableWordV2 implements TextToWordsCompositorV2 {

    @Override
    public TextToWordsCompositorV2 copy() {
        return new TextElementToSyllableWordV2();
    }

    @Override
    public <T extends TextStructureV2> List<TextWordV2> compose(Iterator<T> iterator, ResourceManager storage) {
        List<TextWordV2> responseWords = new LinkedList<>();
        TextWordV2 currentWord = new TextWordV2();
        TextSyllableV2 currentSyllable = new TextSyllableV2();

        boolean lastElementIsText = false;
        boolean lastElementIsVowel = false;

        while (iterator.hasNext()) {
            TextStructureV2 element = iterator.next();

            boolean currentElementIsText = isText(element);
            boolean currentElementIsVowel = isVowel(element);

            boolean isNewWord = !lastElementIsText || !currentElementIsText;
            boolean isNewSyllable = isNewWord //
                    || (!currentElementIsVowel && lastElementIsVowel) // two vowels in a row
            ;

            if (isNewWord && !currentWord.isEmpty()) {
                responseWords.add(currentWord);
                currentWord = new TextWordV2();
            }

            if (!isNewWord && isNewSyllable) {
                // add word break
                TextWordBreakV2 wb = new TextWordBreakV2(//
                        storage, //
                        null //
                );
                currentSyllable.setWordBreak(wb);
            }

            if (isNewSyllable) {
                currentSyllable = new TextSyllableV2();
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

    private boolean isVowel(TextStructureV2 element) {
        if (element instanceof TextCharV2) {
            var textChar = (TextCharV2) element;
            var testC = Character.toLowerCase(textChar.getC());
            return testC == 'a' //
                    || testC == 'e' //
                    || testC == 'i' //
                    || testC == 'o' //
                    || testC == 'u' //
            ;
        }

        return false;
    }

    private boolean isText(TextStructureV2 element) {
        boolean currentElementIsText;
        if (element instanceof TextCharV2) {
            var textChar = (TextCharV2) element;
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
