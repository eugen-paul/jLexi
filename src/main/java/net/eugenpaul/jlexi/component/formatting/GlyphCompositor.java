package net.eugenpaul.jlexi.component.formatting;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.utils.Size;

public interface GlyphCompositor<T extends Glyph> {
    public List<Glyph> compose(Iterator<T> iterator, Size maxSize);
}
