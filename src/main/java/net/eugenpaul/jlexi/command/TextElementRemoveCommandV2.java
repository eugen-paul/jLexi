package net.eugenpaul.jlexi.command;

import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponseV2;

public class TextElementRemoveCommandV2 implements TextCommandV2 {

    private TextPositionV2 positionBeforeRemove;
    private TextPositionV2 positionAfterRemove;
    private TextPositionV2 cursorPosition;

    private TextRemoveResponseV2 removedData;

    public TextElementRemoveCommandV2(TextPositionV2 cursorPosition) {
        this.positionBeforeRemove = cursorPosition;
        this.cursorPosition = cursorPosition;
        this.positionAfterRemove = null;
        this.removedData = TextRemoveResponseV2.EMPTY;
    }

    @Override
    public void execute() {
        if (this.removedData != TextRemoveResponseV2.EMPTY) {
            this.cursorPosition.replaceStructure( //
                    this.removedData.getOwner(), //
                    this.removedData.getRemovedStructures(), //
                    this.removedData.getNewStructures() //
            );
        } else {
            this.removedData = this.positionBeforeRemove.removeElement();

            if (this.removedData != TextRemoveResponseV2.EMPTY) {
                this.positionAfterRemove = this.removedData.getNewCursorPosition();
                this.cursorPosition = this.positionAfterRemove;
            }
        }

        if (this.removedData != TextRemoveResponseV2.EMPTY) {
            this.removedData.getOwner().replaceStructure(//
                    this.removedData.getOwner(), //
                    this.removedData.getRemovedStructures(), //
                    this.removedData.getNewStructures() //
            );
        }
    }

    @Override
    public void unexecute() {
        if (isEmpty()) {
            return;
        }

        this.cursorPosition.replaceStructure(//
                this.removedData.getOwner(), //
                this.removedData.getNewStructures(), //
                this.removedData.getRemovedStructures() //
        );

        this.cursorPosition = this.positionBeforeRemove;
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public TextPositionV2 getData() {
        return this.cursorPosition;
    }
}
