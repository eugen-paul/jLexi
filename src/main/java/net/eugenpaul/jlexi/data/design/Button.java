package net.eugenpaul.jlexi.data.design;

import net.eugenpaul.jlexi.data.Glyph;

public abstract class Button implements Glyph {

    /**
     * Will be call by pressing the Button
     */
    public abstract void press();
}
