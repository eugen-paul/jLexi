package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.List;
import java.util.ListIterator;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Size;

public interface TextCompositor<T extends Glyph> {
    public List<TextRepresentation> compose(ListIterator<T> iterator, Size maxSize);
}
