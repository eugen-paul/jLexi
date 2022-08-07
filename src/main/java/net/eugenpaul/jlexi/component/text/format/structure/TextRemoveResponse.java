package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;

@AllArgsConstructor
@Getter
public class TextRemoveResponse {

    public static final TextRemoveResponse EMPTY = new TextRemoveResponse(//
            null, //
            null, //
            Collections.emptyList(), //
            Collections.emptyList() //
    );

    private TextElement removedElement;
    private TextPosition newCursorPosition;

    private List<List<TextStructure>> removedStructures;
    private List<TextStructure> newStructures;
}
