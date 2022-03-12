package net.eugenpaul.jlexi.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;

public class TextRemoveCommant implements TextCommand {

    private List<TextElement> text;
    @Getter
    private TextElement cursorPosition;

    public TextRemoveCommant(List<TextElement> text, TextElement cursorPosition) {
        this.text = text;
        this.cursorPosition = cursorPosition;
    }

    public TextRemoveCommant(List<TextElement> text) {
        this.text = text;
        this.cursorPosition = null;
    }

    @Override
    public void execute() {
        Map<TextStructure, Boolean> parents = new HashMap<>();

        var iterator = text.iterator();
        TextElement lastPosition = null;
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            var parentStructure = currentElement.getStructureParent();
            if (null == parentStructure) {
                return;
            }
            var nextElement = parentStructure.removeElement(currentElement);
            if (null != nextElement) {
                lastPosition = nextElement;
            }

            parents.computeIfAbsent(parentStructure, v -> true);
        }

        if (cursorPosition == null) {
            cursorPosition = lastPosition;
        }

        parents.keySet().stream()//
                .forEach(parent -> parent.notifyChange(true));
    }

    @Override
    public void unexecute() {
        TextAddBeforeCommand command = new TextAddBeforeCommand(text, cursorPosition);
        command.execute();
    }

    @Override
    public boolean reversible() {
        return true;
    }

}
