package net.eugenpaul.jlexi.component.text.formatting;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorControl;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public interface TextContainer<T extends TextPaneElement> extends CursorControl {

    public boolean isEmpty();

    public Drawable getPixels();

    public Drawable getPixels(Vector2d position, Size size);

    public boolean addIfPossible(T element);

    public void updateSize(Size size);

    public Vector2d getRelativPosition();

    public Size getSize();

    public TextPaneElement getElementOnPosition(Vector2d position);

    public void setParent(Glyph parent);
}