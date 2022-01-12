package net.eugenpaul.jlexi.data.formatting;

import net.eugenpaul.jlexi.data.Glyph;

/**
 * Abstract Class for Objects that cann be formatet with Compositor
 */
public abstract class Composition implements Glyph {

    private Compositor compositor;

    protected Composition(Compositor compositor) {
        this.compositor = compositor;
    }

    @Override
    public void insert(Glyph glyph, int position) {
        compositor.compose();
    }
}
