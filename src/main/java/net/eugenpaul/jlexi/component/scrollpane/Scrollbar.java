package net.eugenpaul.jlexi.component.scrollpane;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.button.Button;
import net.eugenpaul.jlexi.component.formatting.ToSingleGlyphCompositor;
import net.eugenpaul.jlexi.component.panes.ImageGlyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class Scrollbar extends GuiGlyph {

    public enum ScrollbarType {
        VERTICAL, HORIZONTAL
    }

    protected static final int DEFAULT_WIDTH = 15;

    protected Color scrollbarColor;

    protected int width = DEFAULT_WIDTH;

    protected ToSingleGlyphCompositor<GuiGlyph> compositor;

    protected Button buttonFirst;
    protected Button buttonLast;
    protected ImageGlyph backgroundGlyph;
    protected ImageGlyph runnerGlyph;

    protected ScrollbarType type;

    protected long intervalTotal = 0;
    protected long intervalDisplayed = 0;
    protected long intervalOffset = 0;

    protected Scrollbar(Glyph parent) {
        super(parent);
    }

    @Override
    public boolean isResizeble() {
        return true;
    }

    @Override
    public void resizeTo(Size size) {
        this.cachedDrawable = null;
        setSize(size);
    }

    @Override
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        if (type == ScrollbarType.VERTICAL) {
            backgroundGlyph.resizeTo(width, getSize().getHeight() - 2 * width);
        } else {
            backgroundGlyph.resizeTo(getSize().getWidth() - 2 * width, width);
        }

        List<GuiGlyph> elements = new LinkedList<>();
        elements.add(buttonFirst);
        elements.add(backgroundGlyph);
        elements.add(buttonLast);
        elements = elements.stream().filter(Objects::nonNull).collect(Collectors.toList());

        var bar = compositor.composeToSingle(elements.iterator(), getSize());

        this.cachedDrawable = new DrawableSketchImpl(scrollbarColor, getSize());
        this.cachedDrawable.addDrawable(bar.getDrawable(), 0, 0);

        addRunnerV();
        addRunnerH();

        return this.cachedDrawable.draw();
    }

    private void addRunnerV() {
        if (type == ScrollbarType.VERTICAL //
                && runnerGlyph != null //
                && intervalTotal != 0 //
        ) {
            Size runnerSize;

            int visiblePercent = (int) (intervalDisplayed * 100 / intervalTotal);
            int runnerHeight = (getSize().getHeight() - 2 * width) * visiblePercent / 100;

            runnerSize = new Size(width, runnerHeight);

            runnerGlyph.resizeTo(runnerSize);

            int offsetPercent = (int) (intervalOffset * 100 / intervalTotal);

            int offset = Math.min(//
                    this.width + (getSize().getHeight() - 2 * width) * offsetPercent / 100, //
                    getSize().getHeight() - this.width - runnerHeight //
            );

            this.cachedDrawable.addDrawable(//
                    runnerGlyph.getDrawable(), //
                    0, //
                    offset, //
                    0 //
            );
        }
    }

    private void addRunnerH() {
        if (type == ScrollbarType.HORIZONTAL //
                && runnerGlyph != null //
                && intervalTotal != 0 //
        ) {
            Size runnerSize;

            int visiblePercent = (int) (intervalDisplayed * 100 / intervalTotal);
            int runnerWidth = (getSize().getWidth() - 2 * width) * visiblePercent / 100;

            runnerSize = new Size(runnerWidth, width);

            runnerGlyph.resizeTo(runnerSize);

            int offsetPercent = (int) (intervalOffset * 100 / intervalTotal);

            int offset = Math.min(//
                    this.width + (getSize().getWidth() - 2 * width) * offsetPercent / 100, //
                    getSize().getWidth() - this.width - runnerWidth //
            );

            this.cachedDrawable.addDrawable(//
                    runnerGlyph.getDrawable(), //
                    offset, //
                    0, //
                    0 //
            );
        }
    }

    public void setIntervalTotal(long intervalTotal) {
        this.intervalTotal = intervalTotal;
        this.cachedDrawable = null;
    }

    public void setIntervalDisplayed(long intervalDisplayed) {
        this.intervalDisplayed = intervalDisplayed;
        this.cachedDrawable = null;
    }

    public void setIntervalOffset(long intervalOffset) {
        this.intervalOffset = intervalOffset;
        this.cachedDrawable = null;
    }

    @Override
    public Iterator<Glyph> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub
    }

}
