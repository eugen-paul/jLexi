package net.eugenpaul.jlexi.component.text.format.structure.textelements;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

public class TextWordV2 {

    @Getter
    private List<TextSyllableV2> syllables;

    public TextWordV2() {
        this.syllables = new LinkedList<>();
    }

    public void addSyllable(TextSyllableV2 syllable) {
        this.syllables.add(syllable);
    }

    public boolean isEmpty() {
        return this.syllables.isEmpty();
    }
}
