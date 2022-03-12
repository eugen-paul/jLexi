package net.eugenpaul.jlexi.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;

public class TextRemoveCommant implements Command {

    private List<TextElement> text;
    private TextElement position;

    public TextRemoveCommant(List<TextElement> text, TextElement position) {
        this.text = text;
        this.position = position;
    }

    @Override
    public void execute() {
        Map<TextStructure, Boolean> parents = new HashMap<>();
        text.stream().forEach(element -> {
            var parentStructure = element.getStructureParent();
            if (null == parentStructure) {
                return;
            }
            parentStructure.removeElement(element);

            parents.computeIfAbsent(parentStructure, v -> true);
        });

        parents.keySet().stream()//
                .forEach(parent -> parent.notifyChange(true));
    }

    @Override
    public void unexecute() {
        TextAddBeforeCommand command = new TextAddBeforeCommand(text, position);
        command.execute();
    }

    @Override
    public boolean reversible() {
        return true;
    }

}
