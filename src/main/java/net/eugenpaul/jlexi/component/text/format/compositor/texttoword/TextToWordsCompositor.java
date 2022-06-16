package net.eugenpaul.jlexi.component.text.format.compositor.texttoword;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextWord;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public interface TextToWordsCompositor<T extends Glyph> {
    public List<TextWord> compose(Iterator<T> iterator, ResourceManager storage);

    public TextToWordsCompositor<T> copy();
}
