package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.component.Glyph;

/**
 * Interface for all GUI Components like Button, Label, Menu, Panel, ...
 */
public interface GuiComponent extends MouseClickable, Resizeable, KeyPressable, Focusable {
    public Glyph getGlyph();
}
