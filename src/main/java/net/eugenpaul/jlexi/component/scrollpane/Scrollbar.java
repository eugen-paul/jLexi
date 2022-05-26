package net.eugenpaul.jlexi.component.scrollpane;

import java.util.Collections;
import java.util.Iterator;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.draw.DrawableImageImpl.DrawableImageImplBufferBuilder;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class Scrollbar extends GuiGlyph {

    public enum ScrollbarType {
        VERTICAL, HORIZONTAL
    }

    protected static final int DEFAULT_WIDTH = 11;

    protected Color scrollbarColor;

    protected int width = DEFAULT_WIDTH;

    protected DrawableImageImplBufferBuilder arrowFirstBuffer;
    protected DrawableImageImplBufferBuilder arrowLastBuffer;
    protected DrawableImageImplBufferBuilder background;
    protected DrawableImageImplBufferBuilder runner;

    protected ScrollbarType type;

    private long intervalTotal = 0;
    private long intervalDisplayed = 0;
    private long intervalOffset = 0;

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
    public Drawable getDrawable() {
        this.cachedDrawable = new DrawableSketchImpl(scrollbarColor, getSize());

        addBackground();

        addRunner();

        addFirstArrow();

        addLastArrow();

        return this.cachedDrawable.draw();
    }

    private void addLastArrow() {
        if (arrowLastBuffer != null) {
            Drawable arrow = arrowLastBuffer//
                    .size(new Size(this.width, this.width))//
                    .build();

            if (type == ScrollbarType.VERTICAL) {
                this.cachedDrawable.addDrawable(arrow, 0, getSize().getHeight() - this.width, 0);
            } else {
                this.cachedDrawable.addDrawable(arrow, getSize().getWidth() - this.width, 0, 0);
            }
        }
    }

    private void addFirstArrow() {
        if (arrowFirstBuffer != null) {
            Drawable arrow = arrowFirstBuffer//
                    .size(new Size(this.width, this.width))//
                    .build();

            this.cachedDrawable.addDrawable(arrow, 0, 0, 0);
        }
    }

    private void addRunner() {
        if (runner != null && intervalTotal != 0) {
            Size runnerSize;

            int visiblePercent = (int) (intervalDisplayed * 100 / intervalTotal);
            if (type == ScrollbarType.VERTICAL) {
                runnerSize = new Size(width, (getSize().getHeight() - 2 * width) * visiblePercent / 100);
            } else {
                runnerSize = new Size((getSize().getWidth() - 2 * width) * visiblePercent / 100, width);
            }

            Drawable rn = runner//
                    .size(runnerSize)//
                    .build();

            int offsetPercent = (int) (intervalOffset * 100 / intervalTotal);
            if (type == ScrollbarType.VERTICAL) {
                this.cachedDrawable.addDrawable(//
                        rn, //
                        0, //
                        this.width + (getSize().getHeight() - 2 * width) * offsetPercent / 100, //
                        0 //
                );
            } else {
                this.cachedDrawable.addDrawable(//
                        rn, //
                        this.width + (getSize().getWidth() - 2 * width) * offsetPercent / 100, //
                        0, //
                        0 //
                );
            }
        }
    }

    private void addBackground() {
        if (background != null) {
            Size bgSize;
            if (type == ScrollbarType.VERTICAL) {
                bgSize = new Size(width, getSize().getHeight() - 2 * width);
            } else {
                bgSize = new Size(getSize().getWidth() - 2 * width, width);
            }

            Drawable bg = background//
                    .size(bgSize)//
                    .build();

            if (type == ScrollbarType.VERTICAL) {
                this.cachedDrawable.addDrawable(bg, 0, width, 0);
            } else {
                this.cachedDrawable.addDrawable(bg, width, 0, 0);
            }
        }
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
