package net.eugenpaul.jlexi.design;

import net.eugenpaul.jlexi.component.Glyph;

public abstract class Button extends Glyph {

    protected Button(Glyph parent) {
        super(parent);
    }

    /**
     * Will be call by pressing the Button
     */
    public abstract void press();
}
