package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

public class TextWord {

    @Getter
    private List<TextSyllable> syllables;

    public TextWord() {
        this.syllables = new LinkedList<>();
    }

    public void addSyllable(TextSyllable syllable) {
        this.syllables.add(syllable);
    }

    public boolean isEmpty() {
        return this.syllables.isEmpty();
    }
}
