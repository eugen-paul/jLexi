package net.eugenpaul.jlexi.data.design;

import java.util.Arrays;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Point;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.iterator.NullIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;

public class EmptyPanel implements Glyph {

    /** width of the window */
    private int width;
    /** hight of the window */
    private int hight;

    private static final int COLOR_BACKGROUND = 0xFFFF0000;

    private int[][] pixels;

    public EmptyPanel(int width, int hight) {
        this.width = width;
        this.hight = hight;

        pixels = new int[hight][width];
        for (int[] line : pixels) {
            Arrays.fill(line, COLOR_BACKGROUND);
        }
    }

    @Override
    public Drawable getPixels() {
        return () -> pixels;
    }

    @Override
    public Size getSize() {
        return new Size(width, hight);
    }

    public void updateSize(int width, int hight) {
        this.width = width;
        this.hight = hight;

        pixels = new int[hight][width];
        for (int[] line : pixels) {
            Arrays.fill(line, COLOR_BACKGROUND);
        }
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

}
