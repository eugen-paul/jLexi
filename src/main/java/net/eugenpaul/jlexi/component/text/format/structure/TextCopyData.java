package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

public final class TextCopyData {
    @Getter
    private List<TextStructureV2> elements;

    public TextCopyData() {
        this.elements = new LinkedList<>();
    }

    public TextCopyData(TextStructureV2 data) {
        this();
        this.elements.add(data);
    }

    public void add(TextStructureV2 data) {
        elements.add(data);
    }
}
