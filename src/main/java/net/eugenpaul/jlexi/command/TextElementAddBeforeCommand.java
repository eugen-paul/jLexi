package net.eugenpaul.jlexi.command;

import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;

public class TextElementAddBeforeCommand implements TextCommand {

    private TextElement addedElement;
    @Getter
    private TextPosition cursorPosition;

    private TextStructure owner;
    private TextStructure removedStructures;
    private List<TextStructure> newStructures;

    public TextElementAddBeforeCommand(TextElement addedElement, TextPosition cursorPosition) {
        this.addedElement = addedElement;
        this.cursorPosition = cursorPosition;
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
            // TODO
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

}
