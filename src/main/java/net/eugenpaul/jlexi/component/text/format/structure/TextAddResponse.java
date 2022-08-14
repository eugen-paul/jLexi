package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

@Getter
public class TextAddResponse {

    public static final TextAddResponse EMPTY = new TextAddResponse(//
            null //
    );

    private TextPosition newCursorPosition;

    private List<List<TextStructure>> newStructures;
    private List<List<TextStructure>> removedStructures;

    public TextAddResponse(TextPosition newCursorPosition) {

        this.newCursorPosition = newCursorPosition;
        this.removedStructures = Collections.emptyList();
        this.newStructures = Collections.emptyList();
    }

    public TextAddResponse(//
            TextPosition newCursorPosition, //
            List<List<TextStructure>> newStructures, //
            List<List<TextStructure>> removedStructures //
    ) {
        this.newCursorPosition = newCursorPosition;
        this.removedStructures = removedStructures;
        this.newStructures = newStructures;
    }

    public boolean isTextReplaced() {
        return !removedStructures.isEmpty();
    }
}
