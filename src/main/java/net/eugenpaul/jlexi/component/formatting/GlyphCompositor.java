package net.eugenpaul.jlexi.component.formatting;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public interface GlyphCompositor<T extends Glyph> {

    void setBackgroundColor(Color backgroundColor);

    Color getBackgroundColor();

    List<Glyph> compose(Iterator<T> iterator, Size maxSize);
}
