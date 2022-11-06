package net.eugenpaul.jlexi.component.text.format.compositor.wordtorow;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextWordV2;
import net.eugenpaul.jlexi.utils.Size;

public interface TextWordsToRowCompositorV2 {
    public List<TextRepresentationV2> compose(List<TextWordV2> words, Size maxSize);

    public TextWordsToRowCompositorV2 copy();
}
