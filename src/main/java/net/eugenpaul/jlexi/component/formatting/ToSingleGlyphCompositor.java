package net.eugenpaul.jlexi.component.formatting;

import java.util.Iterator;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public interface ToSingleGlyphCompositor<T extends Glyph> {

    void setBackgroundColor(Color backgroundColor);

    Color getBackgroundColor();

    Glyph composeToSingle(Iterator<T> iterator, Size maxSize);
}
