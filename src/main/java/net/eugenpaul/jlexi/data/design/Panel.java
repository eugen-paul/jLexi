package net.eugenpaul.jlexi.data.design;

import java.util.Arrays;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Point;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.framing.Resizeable;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.iterator.NullIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;

public class Panel implements Glyph, Resizeable {

    private static final int COLOR_BACKGROUND = 0xFFFF0000;
    private static final int[][] EMPTY_PANEL = new int[0][0];
    private static final Drawable EMPTY_DRAWABLE = () -> EMPTY_PANEL;

    private Size size;

    private int[][] pixels;

    public Panel() {
        this.size = Size.ZERO_SIZE;
        this.pixels = EMPTY_PANEL;
    }

    @Override
    public Drawable getPixels() {
        if (size.isZero()) {
            return EMPTY_DRAWABLE;
        }

        pixels = new int[size.getHight()][size.getWidth()];
        for (int[] line : pixels) {
            Arrays.fill(line, COLOR_BACKGROUND);
        }
        return () -> pixels;
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
    public void insert(Glyph glyph, int position) {
        // Nothig to do
    }

    @Override
    public void remove(Glyph glyph) {
        // Nothig to do
    }

    @Override
    public Glyph child(int position) {
        return null;
    }

    @Override
    public GlyphIterator createIterator() {
        return NullIterator.getNullIterator();
    }

    @Override
    public Glyph parent() {
        return null;
    }

    @Override
    public void visit(Visitor checker) {
        // Nothig to do
    }

    @Override
    public void setSize(Size size) {
        if (size.isZero()) {
            this.pixels = EMPTY_PANEL;
            this.size = Size.ZERO_SIZE;
        } else {
            this.size = size;
        }
    }

}
