package net.eugenpaul.jlexi.component.text.formatting;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.BiFunction;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorControl;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

/**
 * Assembles the elements into a container.
 */
public abstract class TextCompositor<T extends TextPaneElement> extends Glyph implements CursorControl {

    protected BiFunction<Glyph, Size, TextContainer<T>> containerConstructor;

    protected Size maxSize;

    protected boolean cached;
    protected Drawable cachedDrawable;

    protected LinkedList<Glyph> updatedChildren;
    protected LinkedList<Area> updatedAreas;

    protected TextCompositor(Glyph parent) {
        super(parent);
        cached = false;
        updatedChildren = new LinkedList<>();
        updatedAreas = new LinkedList<>();
    }

    public abstract void compose(Iterator<T> iterator);

    public abstract Drawable getPixels(Vector2d position, Size size);

    public abstract TextPaneElement getElementOnPosition(Vector2d position);

    @Override
    public void notifyUpdate(Glyph child) {
        if (null != parent) {
            parent.notifyUpdate(this);
        }
        cached = false;
        updatedChildren.clear();
        updatedAreas.clear();
    }

    @Override
    public void notifyRedraw(Glyph child, Vector2d position, Size size) {
        cached = false;
        updatedChildren.add(child);
        updatedAreas.add(new Area(position, size));

        parent.notifyRedraw(this, child.getRelativPosition().addNew(position), size);
    }

    public void updateSize(Size size) {
        this.maxSize = size;
        cached = false;
        updatedChildren.clear();
        updatedAreas.clear();
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
