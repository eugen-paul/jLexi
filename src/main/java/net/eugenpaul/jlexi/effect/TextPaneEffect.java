package net.eugenpaul.jlexi.effect;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.draw.Drawable;

public abstract class TextPaneEffect implements Effect {

    @Getter
    private TextPaneElement glyph;

    protected TextPaneEffect(TextPaneElement glyph) {
        this.glyph = glyph;
    }

    public abstract void editDrawable(Drawable pixels);
}
