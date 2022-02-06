package net.eugenpaul.jlexi.component.text.structure;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.utils.Size;

public interface TextCompositorConstructor<T extends TextPaneElement> {
    public TextCompositor<T> create(Glyph parent, Size maxSize);
}
