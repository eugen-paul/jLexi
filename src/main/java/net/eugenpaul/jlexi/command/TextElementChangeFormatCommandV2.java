package net.eugenpaul.jlexi.command;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;

public class TextElementChangeFormatCommandV2 implements TextCommandV2 {

    private final List<TextElementV2> selectedText;
    private final List<TextFormat> newFormat;
    private final List<TextFormat> oldFormat;

    public TextElementChangeFormatCommandV2(List<TextElementV2> selectedText, List<TextFormat> newFormat) {
        this.selectedText = new LinkedList<>(selectedText);
        this.newFormat = new LinkedList<>(newFormat);
        this.oldFormat = this.selectedText.stream()//
                .map(TextElementV2::getFormat)//
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

    private void updateFormat(Iterator<TextElementV2> iteratorText, Iterator<TextFormat> iteratorNewFormat) {
        while (iteratorText.hasNext() && iteratorNewFormat.hasNext()) {
            var elem = iteratorText.next();
            var format = iteratorNewFormat.next();

            // TODO
            // elem.updateFormat(format);
        }

        var firstTextElement = selectedText.get(0);
        // TODO
        // firstTextElement.redraw();
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
    public TextPositionV2 getData() {
        if (isEmpty()) {
            return null;
        }
        return this.selectedText.get(0).getTextPosition();
    }

}
