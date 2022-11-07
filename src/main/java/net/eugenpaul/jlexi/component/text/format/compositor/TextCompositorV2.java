package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.List;
import java.util.ListIterator;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.utils.Size;

public interface TextCompositorV2<T extends Glyph> {
    public List<TextRepresentationV2> compose(ListIterator<T> iterator, Size maxSize);
}
