package net.eugenpaul.jlexi.command;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;

public class TextRemoveCommant implements TextCommand {

    private List<TextElement> text;
    private List<TextElement> removedText;
    @Getter
    private TextElement cursorPosition;

    public TextRemoveCommant(List<TextElement> text, TextElement cursorPosition) {
        this.text = text;
        this.removedText = new LinkedList<>();
        this.cursorPosition = cursorPosition;
    }

    public TextRemoveCommant(List<TextElement> text) {
        this(text, null);
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
            if (nextElement != currentElement) {
                removedText.add(currentElement);
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
        TextAddBeforeCommand command = new TextAddBeforeCommand(removedText, cursorPosition);
        command.execute();
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
