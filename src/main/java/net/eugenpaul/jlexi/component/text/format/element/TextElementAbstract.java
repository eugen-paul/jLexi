package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.Glyph;

public abstract class TextElementAbstract extends TextElement {

    protected TextElementAbstract(Glyph parent) {
        super(parent);
    }

    @Override
    public boolean isCursorHoldable() {
        return false;
    }

}
