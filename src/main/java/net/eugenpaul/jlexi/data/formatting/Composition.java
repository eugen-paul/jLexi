package net.eugenpaul.jlexi.data.formatting;

import net.eugenpaul.jlexi.data.Glyph;

/**
 * Abstract Class for Objects that cann be formatet with Compositor
 */
public abstract class Composition<T> implements Glyph {

    protected Compositor<T> compositor;

    protected Composition(Compositor<T> compositor) {
        this.compositor = compositor;
    }

    // @Override
    // public void insert(Glyph glyph, int position) {
    // compositor.compose();
    // }
}
