package net.eugenpaul.jlexi.design;

import java.util.Arrays;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.Resizeable;
import net.eugenpaul.jlexi.component.iterator.GlyphIterator;
import net.eugenpaul.jlexi.component.iterator.NullIterator;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Verctor2d;

public class Panel {
// public class Panel implements Glyph, Resizeable {
    // private Position position;
    // private static final int COLOR_BACKGROUND = 0xFFFF0000;
    // private static final int[] EMPTY_PANEL = new int[0];
    // private static final Drawable EMPTY_DRAWABLE = DrawableImpl.EMPTY_DRAWABLE;

    // private Size size;

    // private int[] pixels;

    // public Panel() {
    //     this.size = Size.ZERO_SIZE;
    //     this.pixels = EMPTY_PANEL;
    // }

    // @Override
    // public Drawable getPixels() {
    //     if (size.isZero()) {
    //         return EMPTY_DRAWABLE;
    //     }

    //     pixels = new int[size.getHight() * size.getWidth()];
    //     Arrays.fill(pixels, COLOR_BACKGROUND);

    //     return new DrawableImpl(pixels, size);
    // }

    // @Override
    // public Size getSize() {
    //     return size;
    // }

    // // @Override
    // // public boolean isIntersects(Point point) {
    // // return false;
    // // }

    // // @Override
    // // public void insert(Glyph glyph, int position) {
    // // // Nothig to do
    // // }

    // // @Override
    // // public void remove(Glyph glyph) {
    // // // Nothig to do
    // // }

    // // @Override
    // // public Glyph child(int position) {
    // // return null;
    // // }

    // @Override
    // public GlyphIterator createIterator() {
    //     return NullIterator.getNullIterator();
    // }

    // @Override
    // public Glyph getParent() {
    //     return null;
    // }

    // @Override
    // public void visit(Visitor checker) {
    //     // Nothig to do
    // }

    // @Override
    // public void setSize(Size size) {
    //     if (size.isZero()) {
    //         this.pixels = EMPTY_PANEL;
    //         this.size = Size.ZERO_SIZE;
    //     } else {
    //         this.size = size;
    //     }
    // }

    // @Override
    // public void setRelativPosition(Position position) {
    //     this.position = position;
    // }

    // @Override
    // public Position getRelativPosition() {
    //     return position;
    // }
}