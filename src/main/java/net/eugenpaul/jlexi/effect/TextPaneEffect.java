package net.eugenpaul.jlexi.effect;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.Drawable;

public abstract class TextPaneEffect implements Effect {

    @Getter
    private TextElement glyph;

    protected TextPaneEffect(TextElement glyph) {
        this.glyph = glyph;
    }

    public abstract void editDrawable(Drawable pixels);
}
