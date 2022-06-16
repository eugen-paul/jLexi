package net.eugenpaul.jlexi.component.text.format.compositor.texttoword;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextSyllable;
import net.eugenpaul.jlexi.component.text.format.element.TextWord;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

@Builder
public class TextElementToLetterWord<T extends TextElement> implements TextToWordsCompositor<T> {

    @Override
    public TextElementToLetterWord<T> copy() {
        return new TextElementToLetterWord<>();
    }

    @Override
    public List<TextWord> compose(Iterator<T> iterator, ResourceManager storage) {
        List<TextWord> responseWords = new LinkedList<>();

        while (iterator.hasNext()) {
            T element = iterator.next();
            TextWord currentWord = new TextWord();
            TextSyllable currentSyllable = new TextSyllable();
            currentSyllable.addElement(element);
            currentWord.addSyllable(currentSyllable);
            responseWords.add(currentWord);
        }

        return responseWords;
    }
}
