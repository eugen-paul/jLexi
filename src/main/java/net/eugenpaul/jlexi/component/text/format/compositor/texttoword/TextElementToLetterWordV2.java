package net.eugenpaul.jlexi.component.text.format.compositor.texttoword;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import lombok.Builder;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextSyllableV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextWordV2;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

@Builder
public class TextElementToLetterWordV2 implements TextToWordsCompositorV2 {

    @Override
    public TextElementToLetterWordV2 copy() {
        return new TextElementToLetterWordV2();
    }

    @Override
    public <T extends TextStructureV2> List<TextWordV2> compose(ListIterator<T> iterator, ResourceManager storage) {
        List<TextWordV2> responseWords = new LinkedList<>();

        while (iterator.hasNext()) {
            T element = iterator.next();
            TextWordV2 currentWord = new TextWordV2();
            TextSyllableV2 currentSyllable = new TextSyllableV2();
            currentSyllable.addElement(element);
            currentWord.addSyllable(currentSyllable);
            responseWords.add(currentWord);
        }

        return responseWords;
    }
}
