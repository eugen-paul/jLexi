package net.eugenpaul.jlexi.command;

import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.iterator.PreOrderLeafIterator;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;

public abstract class TextElementChangeFormatCommandV2 implements TextCommandV2 {

    private final TextStructureV2 selectedText;
    private final List<TextFormat> oldFormat;
    private TextPositionV2 position;

    protected TextElementChangeFormatCommandV2(TextStructureV2 selectedText) {
        this.selectedText = selectedText;
        this.position = null;

        this.oldFormat = new LinkedList<>();
        var iterator = new PreOrderLeafIterator<>(this.selectedText);
        while (iterator.hasNext()) {
            var element = iterator.next();
            if (element instanceof TextElementV2) {
                var textElement = (TextElementV2) element;
                this.oldFormat.add(textElement.getFormat());
                this.position = textElement.getTextPosition();
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return selectedText.isEmpty();
    }

    protected abstract void setNewFormat(TextElementV2 element);

    @Override
    public void execute() {
        if (isEmpty()) {
            return;
        }

        var iterator = new PreOrderLeafIterator<>(this.selectedText);
        while (iterator.hasNext()) {
            var element = iterator.next();
            if (element instanceof TextElementV2) {
                var textElement = ((TextElementV2) element);
                setNewFormat(textElement);
            }
        }
    }

    @Override
    public void unexecute() {
        if (isEmpty()) {
            return;
        }

        var iterator = new PreOrderLeafIterator<>(this.selectedText);
        var oldFormatIterator = this.oldFormat.iterator();
        while (iterator.hasNext() && oldFormatIterator.hasNext()) {
            var element = iterator.next();
            if (element instanceof TextElementV2) {
                var textElement = ((TextElementV2) element);
                var format = oldFormatIterator.next();
                textElement.updateFormat(format);
            }
        }
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public TextPositionV2 getData() {
        return this.position;
    }

}
