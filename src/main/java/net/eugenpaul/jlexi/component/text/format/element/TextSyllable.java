package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;

public class TextSyllable {

    private List<TextElement> elements;

    private TextWordBreak wordBreakElement;

    public TextSyllable() {
        this.elements = new LinkedList<>();
        this.wordBreakElement = null;
    }

    public void addElement(TextElement element) {
        this.elements.add(element);
    }

    public void setWordBreak(TextWordBreak wb) {
        this.wordBreakElement = wb;
    }

    public boolean isWithWordBreak() {
        return this.wordBreakElement != null;
    }

    public int getHeight() {
        int maxHeight = 0;
        for (TextElement textElement : this.elements) {
            maxHeight = Math.max(maxHeight, textElement.getSize().getHeight());
        }
        return maxHeight;
    }

    private int getLengthWithoutWordBreak() {
        return this.elements.stream().mapToInt(v -> v.getSize().getWidth()).sum();
    }

    public int getLength() {
        if (this.wordBreakElement != null) {
            return getLengthWithoutWordBreak() + this.wordBreakElement.getSize().getWidth();
        }
        return getLengthWithoutWordBreak();
    }

    public List<TextElement> getElements() {
        List<TextElement> response = new LinkedList<>(this.elements);
        if (this.wordBreakElement != null) {
            response.add(this.wordBreakElement);
        }
        return response;
    }

    public boolean isEmpty() {
        return this.elements.isEmpty();
    }
}
