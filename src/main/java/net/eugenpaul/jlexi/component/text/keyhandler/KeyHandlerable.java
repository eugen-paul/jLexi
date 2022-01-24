package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.effect.EffectHandler;
import net.eugenpaul.jlexi.effect.TextPaneEffect;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;

/**
 * Interface for Text-Panel-Elements that can handle a key input
 */
public interface KeyHandlerable {

    public FontStorage getFontStorage();

    public TextPaneEffect getCursor();

    public void setCursor(TextPaneEffect cursor);

    public EffectHandler getEffectHandler();

    public Glyph getParent();

    public Glyph getThis();
}
