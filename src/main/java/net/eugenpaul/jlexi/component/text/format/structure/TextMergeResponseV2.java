package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;

@Getter
public class TextMergeResponseV2 {

    public static final TextMergeResponseV2 EMPTY = new TextMergeResponseV2(//
            null, //
            Collections.emptyList() //
    );

    private TextPositionV2 newCursorPosition;

    private List<TextStructureV2> newStructures;

    public TextMergeResponseV2(//
            TextPositionV2 newCursorPosition, //
            List<TextStructureV2> newStructures //
    ) {
        this.newCursorPosition = newCursorPosition;
        this.newStructures = newStructures;
    }
}
