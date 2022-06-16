package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;

public class TextSyllable {

    private List<TextElement> elements;

    private TextWordBreak wordBreakElement;

    public TextSyllable() {
        elements = new LinkedList<>();
        wordBreakElement = null;
    }

    public void addElement(TextElement element) {
        elements.add(element);
    }

    public void setWordBreak(TextWordBreak wb) {
        wordBreakElement = wb;
    }

    public boolean isWithWordBreak() {
        return wordBreakElement != null;
    }

    public int getHeight() {
        int maxHeight = 0;
        for (TextElement textElement : elements) {
            maxHeight = Math.max(maxHeight, textElement.getSize().getHeight());
        }
        return maxHeight;
    }

    private int getLengthWithoutWordBreak() {
        return elements.stream().mapToInt(v -> v.getSize().getWidth()).sum();
    }

    public int getLength() {
        if (wordBreakElement != null) {
            return getLengthWithoutWordBreak() + wordBreakElement.getSize().getWidth();
        }
        return getLengthWithoutWordBreak();
    }

    public List<TextElement> getElements() {
        List<TextElement> response = new LinkedList<>(elements);
        if (wordBreakElement != null) {
            response.add(wordBreakElement);
        }
        return response;
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }
}
