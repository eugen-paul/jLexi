package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;

@Getter
public class TextRemoveResponseV2 {

    public static final TextRemoveResponseV2 EMPTY = new TextRemoveResponseV2(//
            null //
    );

    private TextPositionV2 newCursorPosition;

    private TextStructureV2 owner;
    private List<TextStructureV2> removedStructures;
    private List<TextStructureV2> newStructures;

    public TextRemoveResponseV2(TextPositionV2 newCursorPosition) {
        this.newCursorPosition = newCursorPosition;
        this.removedStructures = Collections.emptyList();
        this.newStructures = Collections.emptyList();
    }

    public TextRemoveResponseV2(TextPositionV2 newCursorPosition, TextStructureV2 owner,
            List<TextStructureV2> removedStructures, List<TextStructureV2> newStructures) {
        this.newCursorPosition = newCursorPosition;
        this.owner = owner;
        this.removedStructures = removedStructures;
        this.newStructures = newStructures;
    }
}
