package net.eugenpaul.jlexi.command;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

public class TextElementChangeFormatCommand implements TextCommand {

    private final List<TextElement> selectedText;
    private final List<TextFormat> newFormat;
    private final List<TextFormat> oldFormat;

    public TextElementChangeFormatCommand(List<TextElement> selectedText, List<TextFormat> newFormat) {
        this.selectedText = new LinkedList<>(selectedText);
        this.newFormat = new LinkedList<>(newFormat);
        this.oldFormat = this.selectedText.stream()//
                .map(TextElement::getFormat)//
                .collect(Collectors.toList());
    }

    @Override
    public boolean isEmpty() {
        return selectedText.isEmpty();
    }

    @Override
    public void execute() {
        if (isEmpty()) {
            return;
        }

        var iteratorText = selectedText.iterator();
        var iteratorNewFormat = newFormat.iterator();

        updateFormat(iteratorText, iteratorNewFormat);
    }

    private void updateFormat(Iterator<TextElement> iteratorText, Iterator<TextFormat> iteratorNewFormat) {
        while (iteratorText.hasNext() && iteratorNewFormat.hasNext()) {
            var elem = iteratorText.next();
            var format = iteratorNewFormat.next();

            elem.updateFormat(format);
        }

        var firstTextElement = selectedText.get(0);
        firstTextElement.redraw();
    }

    @Override
    public void unexecute() {
        if (isEmpty()) {
            return;
        }

        var iteratorText = selectedText.iterator();
        var iteratorOldFormat = oldFormat.iterator();

        updateFormat(iteratorText, iteratorOldFormat);
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public TextPosition getData() {
        if (isEmpty()) {
            return null;
        }
        return this.selectedText.get(0).getTextPosition();
    }

}
