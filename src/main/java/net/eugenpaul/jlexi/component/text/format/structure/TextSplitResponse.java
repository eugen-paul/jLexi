package net.eugenpaul.jlexi.component.text.format.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TextSplitResponse {
    public static final TextSplitResponse EMPTY = new TextSplitResponse(null, null);

    private TextStructureV2 first;
    private TextStructureV2 last;
}
