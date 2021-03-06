package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Size;

public interface TextCompositor<T extends Glyph> {
    public List<TextRepresentation> compose(Iterator<T> iterator, Size maxSize);
}
