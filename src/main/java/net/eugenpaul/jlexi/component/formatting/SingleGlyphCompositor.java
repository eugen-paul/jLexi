package net.eugenpaul.jlexi.component.formatting;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public interface SingleGlyphCompositor<T extends Glyph> {

    void setBackgroundColor(Color backgroundColor);

    Color getBackgroundColor();

    Glyph compose(T element, Size maxSize);
}
