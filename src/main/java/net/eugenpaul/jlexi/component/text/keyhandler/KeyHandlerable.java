package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.effect.EffectHandler;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;

/**
 * Interface for Text-Panel-Elements that can handle a key input
 */
public interface KeyHandlerable {

    public FontStorage getFontStorage();

    public EffectHandler getEffectHandler();

    public void keyUpdate();

    public Glyph getThis();

    public Cursor getMouseCursor();
    public void setMouseCursor(Cursor cursor);

    public void doCursorMove(CursorMove cursorMove);
}
