package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;

@Getter
public class TextAddResponseV2 {

    public static final TextAddResponseV2 EMPTY = new TextAddResponseV2(//
            null //
    );

    private TextPositionV2 newCursorPosition;

    private TextStructureV2 owner;
    private TextStructureV2 removedStructures;
    private List<TextStructureV2> newStructures;

    public TextAddResponseV2(TextPositionV2 newCursorPosition) {
        this.newCursorPosition = newCursorPosition;
        this.owner = null;
        this.removedStructures = null;
        this.newStructures = Collections.emptyList();
    }

    public TextAddResponseV2(//
            TextStructureV2 owner, //
            TextStructureV2 removedStructures, //
            List<TextStructureV2> newStructures //
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
