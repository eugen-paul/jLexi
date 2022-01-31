package net.eugenpaul.jlexi.component.text.formatting;

import java.util.Iterator;
import java.util.function.BiFunction;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorControl;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

/**
 * Assembles the component into a container.
 */
public abstract class TextCompositor<T extends TextPaneElement> extends Glyph implements CursorControl {

    protected BiFunction<Glyph, Size, TextContainer<T>> containerConstructor;

    protected Size maxSize;

    protected TextCompositor(Glyph parent) {
        super(parent);
    }

    public abstract void compose(Iterator<T> iterator);

    public abstract Drawable getPixels(Vector2d position, Size size);

    public abstract TextPaneElement getElementOnPosition(Vector2d position);

    @Override
    public void notifyUpdate(Glyph child) {
        if (null != parent) {
            parent.notifyUpdate(this);
        }
    }

    public void updateSize(Size size) {
        this.maxSize = size;
    }

    @Override
    public Vector2d getRelativPosition() {
        return relativPosition;
    }

    @Override
    public Size getSize() {
        return size;
    }

}
