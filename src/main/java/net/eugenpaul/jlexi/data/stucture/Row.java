package net.eugenpaul.jlexi.data.stucture;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.DrawableImpl;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Point;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.iterator.ListIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;
import net.eugenpaul.jlexi.utils.ImageArrays;

public class Row implements Glyph {

    private List<Glyph> children;

    public Row() {
        children = new ArrayList<>();
    }

    @Override
    public Drawable getPixels() {
        List<Drawable> childDrawable = children.stream()//
                .map(Glyph::getPixels)//
                .collect(Collectors.toList());

        int width = 0;
        int hight = 0;

        for (Drawable drawable : childDrawable) {
            width += drawable.getPixelSize().getWidth();
            hight = Math.max(hight, drawable.getPixelSize().getHight());
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
            positionX += drawable.getPixelSize().getWidth();
        }

        return new DrawableImpl(pixels, pixelsSize);
    }

    @Override
    public Size getSize() {
        return null;
    }

    @Override
    public boolean isIntersects(Point point) {
        return false;
    }

    @Override
    public void insert(Glyph glyph, int position) {
        children.add(position, glyph);
    }

    @Override
    public void remove(Glyph glyph) {

    }

    @Override
    public Glyph child(int position) {
        return null;
    }

    @Override
    public Glyph parent() {
        return null;
    }

    @Override
    public GlyphIterator createIterator() {
        return new ListIterator(children);
    }

    @Override
    public void visit(Visitor checker) {
        checker.visit(this);
    }

}
