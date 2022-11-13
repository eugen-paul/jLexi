package net.eugenpaul.jlexi.component.text.format.structure.textelements;

import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;
import net.eugenpaul.jlexi.utils.Size;

public class TextSyllableV2 {

    private List<TextStructureV2> elements;

    private TextWordBreakV2 wordBreakElement;

    public TextSyllableV2() {
        this.elements = new LinkedList<>();
        this.wordBreakElement = null;
    }

    public void addElement(TextStructureV2 element) {
        this.elements.add(element);
    }

    public void setWordBreak(TextWordBreakV2 wb) {
        this.wordBreakElement = wb;
    }

    public boolean isWithWordBreak() {
        return this.wordBreakElement != null;
    }

    public int getHeight() {
        int maxHeight = 0;
        for (var textElement : getElements()) {
            for (var r : textElement.getRepresentation(Size.MAX)) {
                maxHeight = Math.max(maxHeight, r.getSize().getHeight());
            }
        }
        return maxHeight;
    }

    private int getLengthWithoutWordBreak() {
        int length = 0;
        for (var textElement : getElements()) {
            for (var r : textElement.getRepresentation(Size.MAX)) {
                length += r.getSize().getWidth();
            }
        }
        return length;
    }

    public int getLength() {
        if (this.wordBreakElement != null) {
            return getLengthWithoutWordBreak() + this.wordBreakElement.getSize().getWidth();
        }
        return getLengthWithoutWordBreak();
    }

    public List<TextStructureV2> getElements() {
        List<TextStructureV2> response = new LinkedList<>(this.elements);
        if (this.wordBreakElement != null) {
            response.add(this.wordBreakElement);
        }
        return response;
    }

    public boolean isEmpty() {
        return this.elements.isEmpty();
    }
}
