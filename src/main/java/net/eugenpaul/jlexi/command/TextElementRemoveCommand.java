package net.eugenpaul.jlexi.command;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;

public class TextElementRemoveCommand implements TextCommand {

    private TextElement removedElement;
    private TextPosition positionBeforeRemove;
    private TextPosition positionAfterRemove;
    @Getter
    private TextPosition cursorPosition;

    private TextRemoveResponse removedData;

    public TextElementRemoveCommand(TextPosition cursorPosition) {
        this.positionBeforeRemove = cursorPosition;
        this.cursorPosition = cursorPosition;
        this.positionAfterRemove = null;
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
            this.removedData = this.positionBeforeRemove.removeElement();

            if (this.removedData != TextRemoveResponse.EMPTY) {
                this.positionAfterRemove = this.removedData.getNewCursorPosition();
                this.cursorPosition = this.positionAfterRemove;
                this.removedElement = this.removedData.getRemovedElement();
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
            this.positionAfterRemove.addBefore(this.removedElement);
        }
        this.cursorPosition = this.positionBeforeRemove;
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
