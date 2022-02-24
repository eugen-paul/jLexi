package net.eugenpaul.jlexi.component.text.format.representation;

import java.awt.geom.Area;
import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.field.TextField;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextStructureForm extends Glyph {

    private List<TextField> children;

    protected TextStructureForm(Glyph parent) {
        super(parent);
        // TODO Auto-generated constructor stub
    }

    public abstract void notifyUpdate(TextElement element, Drawable draw, Area area);

    public abstract void notifyResize(TextElement element);

    public abstract void getCorsorElementAt(Vector2d pos);

    public abstract void getFirstChild();

    public abstract void getLastChild();
}
