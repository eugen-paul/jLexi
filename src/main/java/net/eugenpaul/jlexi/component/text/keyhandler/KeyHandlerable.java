package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.effect.EffectController;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

/**
 * Interface for Text-Panel-Elements that can handle a key input
 */
public interface KeyHandlerable {

    public ResourceManager getStorage();

    public EffectController getEffectHandler();

    public void keyUpdate();

    public Glyph getThis();

    public Cursor getMouseCursor();

    public void doCursorMove(CursorMove cursorMove);

    public TextStructureForm getTextStructureForm();
}
