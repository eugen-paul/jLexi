package net.eugenpaul.jlexi.component.formatting;

import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;

/**
 * Abstract Class for Objects that cann be formatet with Compositor
 */
public abstract class Composition<T extends Glyph> extends Glyph {

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
}
