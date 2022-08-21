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

    private TextStructure owner;
    private TextStructure removedStructures;
    private List<TextStructure> newStructures;

    public TextAddResponse(TextPosition newCursorPosition) {
        this.newCursorPosition = newCursorPosition;
        this.owner = null;
        this.removedStructures = null;
        this.newStructures = Collections.emptyList();
    }

    public TextAddResponse(//
            TextStructure owner, //
            TextStructure removedStructures, //
            List<TextStructure> newStructures //
    ) {
        this.newCursorPosition = null;
        this.owner = owner;
        this.removedStructures = removedStructures;
        this.newStructures = newStructures;
    }

    public boolean isStructureChanged() {
        return owner != null;
    }
}
