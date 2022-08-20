package net.eugenpaul.jlexi.command;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;

public class TextElementRemoveSelectedCommand implements TextCommand {

    private TextPosition positionBeforeRemove;
    private TextPosition positionAfterRemove;
    @Getter
    private TextPosition cursorPosition;

    private List<TextElement> selectedText;
    private LinkedList<TextElement> removedText;

    public TextElementRemoveSelectedCommand(List<TextElement> selectedText) {
        this.positionBeforeRemove = selectedText.get(0).getTextPosition();
        this.cursorPosition = null;
        this.positionAfterRemove = null;
        this.selectedText = selectedText;
    }

    @Override
    public void execute() {
        removedText = new LinkedList<>();
        for (var element : selectedText) {
            TextRemoveResponse removedData = element.getTextPosition().removeElement();

            if (removedData != TextRemoveResponse.EMPTY) {
                removedText.add(element);

                positionAfterRemove = removedData.getNewCursorPosition();
                cursorPosition = positionAfterRemove;
            }
        }
    }

    @Override
    public void unexecute() {
        var invertIterator = removedText.descendingIterator();
        while (invertIterator.hasNext()) {
            var element = invertIterator.next();
            TextElementAddBeforeCommand command = new TextElementAddBeforeCommand(element, positionAfterRemove);
            command.execute();
            cursorPosition = positionBeforeRemove;
            positionAfterRemove = element.getTextPosition();
        }
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return removedText.isEmpty();
    }
}
