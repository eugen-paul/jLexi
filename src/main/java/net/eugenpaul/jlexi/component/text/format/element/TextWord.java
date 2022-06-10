package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

public class TextWord {

    @Getter
    private List<List<TextElement>> syllables;

    public TextWord() {
        syllables = new LinkedList<>();
    }

    public void addSyllable(List<TextElement> syllable) {
        syllables.add(syllable);
    }

    public boolean isEmpty() {
        return syllables.isEmpty();
    }
}
