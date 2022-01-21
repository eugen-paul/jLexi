package net.eugenpaul.jlexi.data.formatting;

import lombok.Setter;
import net.eugenpaul.jlexi.data.Glyph;

/**
 * Abstract Class for Objects that cann be formatet with Compositor
 */
public abstract class Composition<T> extends Glyph {

    @Setter(lombok.AccessLevel.PROTECTED)
    protected Compositor<T> compositor;

    protected Composition(Glyph parent, Compositor<T> compositor) {
        super(parent);
        this.compositor = compositor;
    }

    protected Composition(Glyph parent) {
        super(parent);
        this.compositor = null;
    }

    // @Override
    // public void insert(Glyph glyph, int position) {
    // compositor.compose();
    // }
}
