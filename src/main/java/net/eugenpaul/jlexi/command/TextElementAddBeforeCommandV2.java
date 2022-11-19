package net.eugenpaul.jlexi.command;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;

public class TextElementAddBeforeCommandV2 implements TextCommandV2 {

    private TextElementV2 addedElement;
    private TextPositionV2 cursorPosition;

    private TextStructureV2 owner;
    private TextStructureV2 removedStructures;
    private List<TextStructureV2> newStructures;

    public TextElementAddBeforeCommandV2(TextElementV2 addedElement, TextPositionV2 cursorPosition) {
        this.addedElement = addedElement;
        this.cursorPosition = cursorPosition;
        this.owner = null;
        this.removedStructures = null;
        this.newStructures = null;
    }

    @Override
    public void execute() {
        if (newStructures == null || removedStructures == null) {
            var response = cursorPosition.addBefore(addedElement);
            if (response.isStructureChanged()) {
                this.owner = response.getOwner();
                this.removedStructures = response.getRemovedStructures();
                this.newStructures = response.getNewStructures();
            }
        } else {
            cursorPosition.replaceStructure(owner, List.of(removedStructures), newStructures);
        }
    }

    @Override
    public void unexecute() {
        if (newStructures == null || removedStructures == null) {
            addedElement.getTextPosition().removeElement();
        } else {
            cursorPosition.replaceStructure(owner, newStructures, List.of(removedStructures));
        }
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
