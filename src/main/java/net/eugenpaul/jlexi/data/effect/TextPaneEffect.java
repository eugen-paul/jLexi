package net.eugenpaul.jlexi.data.effect;

import lombok.Getter;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.stucture.TextPaneElement;

public abstract class TextPaneEffect implements Effect {

    @Getter
    private TextPaneElement glyph;

    protected TextPaneEffect(TextPaneElement glyph) {
        this.glyph = glyph;
    }

    public abstract void editDrawable(Drawable pixels);
}
