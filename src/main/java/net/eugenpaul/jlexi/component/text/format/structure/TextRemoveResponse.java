package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

@Getter
public class TextRemoveResponse {

    public static final TextRemoveResponse EMPTY = new TextRemoveResponse(//
            null, //
            null //
    );

    private TextElement removedElement;
    private TextPosition newCursorPosition;

    private List<List<TextStructure>> removedStructures;
    private List<List<TextStructure>> newStructures;

    public TextRemoveResponse(TextElement removedElement, TextPosition newCursorPosition) {
        this.removedElement = removedElement;
        this.newCursorPosition = newCursorPosition;
        this.removedStructures = Collections.emptyList();
        this.newStructures = Collections.emptyList();
    }

    public TextRemoveResponse(TextElement removedElement, TextPosition newCursorPosition,
            List<List<TextStructure>> removedStructures, List<List<TextStructure>> newStructures) {
        this.removedElement = removedElement;
        this.newCursorPosition = newCursorPosition;
        this.removedStructures = removedStructures;
        this.newStructures = newStructures;
    }

    public boolean isTextReplaced() {
        return !removedStructures.isEmpty();
    }
}
