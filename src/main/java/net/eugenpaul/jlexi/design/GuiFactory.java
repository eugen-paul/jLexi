package net.eugenpaul.jlexi.design;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.border.Border;
import net.eugenpaul.jlexi.component.button.TextButton;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.component.label.Label;
import net.eugenpaul.jlexi.component.menubar.MenuBar;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public interface GuiFactory {

    TextButton createTextButton(Glyph parent, String text, ResourceManager storage);

    Label createLabel(Glyph parent, String text, ResourceManager storage);

    MenuBar createMenuBar(Glyph parent, GuiComponent component, Size size);

    Border createBorder(Glyph parent, GuiComponent component);
}
