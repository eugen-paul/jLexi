package net.eugenpaul.jlexi.component.text.format.compositor.wordtorow;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextWord;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Size;

public interface TextWordsToRowCompositor {
    public List<TextRepresentation> compose(List<TextWord> words, Size maxSize);

    public TextWordsToRowCompositor copy();
}
