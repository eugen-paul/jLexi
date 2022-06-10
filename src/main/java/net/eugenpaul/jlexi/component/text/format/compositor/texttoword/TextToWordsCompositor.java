package net.eugenpaul.jlexi.component.text.format.compositor.texttoword;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextWord;

public interface TextToWordsCompositor<T extends Glyph> {
    public List<TextWord> compose(Iterator<T> iterator);
}
