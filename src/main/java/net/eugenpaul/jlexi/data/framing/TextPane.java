package net.eugenpaul.jlexi.data.framing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.DrawableImpl;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Point;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.formatting.Composition;
import net.eugenpaul.jlexi.data.formatting.text.RowCompositor;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;
import net.eugenpaul.jlexi.utils.ImageArrays;

/**
 * Display Rows.
 */
public class TextPane extends Composition<Glyph> implements Resizeable {

    private List<Glyph> children;

    private Size size;

    public TextPane() {
        super(new RowCompositor());
        size = Size.ZERO_SIZE;
        children = new ArrayList<>();
        compositor.setComposition(this);
    }

    @Override
    public Drawable getPixels() {
        List<Glyph> textField = compositor.compose(children, size.getWidth());

        List<Drawable> childDrawable = textField.stream()//
                .map(Glyph::getPixels)//
                .collect(Collectors.toList());

        int width = 0;
        int hight = 0;

        for (Drawable drawable : childDrawable) {
            width = Math.max(width, drawable.getPixelSize().getWidth());
            hight += drawable.getPixelSize().getHight();
        }

        int[] pixels = new int[width * hight];
        Size pixelsSize = new Size(width, hight);

        int positionX = 0;
        int positionY = 0;
        for (Drawable drawable : childDrawable) {
            ImageArrays.copyRectangle(//
                    drawable.getPixels(), //
                    drawable.getPixelSize(), //
                    pixels, //
                    pixelsSize, //
                    positionX, //
                    positionY//
            );
            positionY += drawable.getPixelSize().getHight();
        }

        return new DrawableImpl(pixels, pixelsSize);
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public boolean isIntersects(Point point) {
        return false;
    }

    @Override
    public void remove(Glyph glyph) {
        // Nothing to do
    }

    @Override
    public Glyph child(int position) {
        return null;
    }

    @Override
    public GlyphIterator createIterator() {
        return null;
    }

    @Override
    public Glyph parent() {
        return null;
    }

    @Override
    public void visit(Visitor checker) {
        // Nothing to do
    }

    @Override
    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public void insert(Glyph glyph, int position) {
        children.add(position, glyph);
    }

}
