package net.eugenpaul.jlexi.command;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;

public class TextRemoveBevorCommand implements TextCommand {

    @Getter
    private TextPosition cursorPosition;
    private TextElement removedElement;

    private TextRemoveResponse removedData;

    public TextRemoveBevorCommand(TextPosition cursorPosition) {
        this.cursorPosition = cursorPosition;
        this.removedElement = null;
        this.removedData = TextRemoveResponse.EMPTY;
    }

    @Override
    public void execute() {

        if (this.removedData.isTextReplaced()) {
            this.cursorPosition.replaceStructure(//
                    this.removedData.getOwner(), //
                    this.removedData.getRemovedStructures(), //
                    this.removedData.getNewStructures() //
            );
        } else {
            var previousPosition = this.cursorPosition.getPreviousPosition();
            if (previousPosition != null) {
                this.removedData = previousPosition.removeElement();

                if (removedData != TextRemoveResponse.EMPTY) {
                    this.cursorPosition = this.removedData.getNewCursorPosition();
                    this.removedElement = this.removedData.getRemovedElement();
                }
            }
        }

    }

    @Override
    public void unexecute() {
        if (isEmpty()) {
            return;
        }

        if (this.removedData.isTextReplaced()) {
            this.cursorPosition.replaceStructure(//
                    this.removedData.getOwner(), //
                    this.removedData.getNewStructures(), //
                    this.removedData.getRemovedStructures() //
            );
        } else {
            this.cursorPosition.addBefore(this.removedElement);
        }
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.removedElement == null;
    }

}
