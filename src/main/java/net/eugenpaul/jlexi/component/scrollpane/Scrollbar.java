package net.eugenpaul.jlexi.component.scrollpane;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.button.Button;
import net.eugenpaul.jlexi.component.formatting.ToSingleGlyphCompositor;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.component.panes.ImageGlyph;
import net.eugenpaul.jlexi.design.listener.MouseDragAdapter;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;
import net.eugenpaul.jlexi.utils.helper.CollisionHelper;
import net.eugenpaul.jlexi.visitor.Visitor;

@Slf4j
public abstract class Scrollbar extends GuiGlyph {

    public enum ScrollbarType {
        VERTICAL, HORIZONTAL
    }

    protected static final int DEFAULT_WIDTH = 15;

    @Getter
    private Color scrollbarColor = Color.INVISIBLE;

    @Getter
    private int barWidth = DEFAULT_WIDTH;

    protected ToSingleGlyphCompositor<GuiGlyph> compositor;

    protected Button buttonFirst;
    protected Button buttonLast;
    protected ImageGlyph backgroundGlyph;
    protected ImageGlyph runnerGlyph;

    protected ScrollbarType type;

    private int buttonStep = 10;
    private int barStep = 50;

    @Getter
    @Setter
    private ScrollCallback scrollCallback = null;

    protected long intervalTotal = 0;
    protected long intervalDisplayed = 0;
    protected long intervalOffset = 0;

    protected Scrollbar(Glyph parent) {
        super(parent);
        this.mouseEventAdapter = new MouseEventAdapterIntern(this);
        this.mouseDragAdapter = new MouseDraggedIntern(this);
    }

    @Override
    public boolean isResizeble() {
        return true;
    }

    @Override
    public void resizeTo(Size size) {
        this.cachedDrawable = null;
        if (type == ScrollbarType.VERTICAL) {
            setSize(new Size(barWidth, size.getHeight()));
        } else {
            setSize(new Size(size.getWidth(), barWidth));
        }
    }

    protected void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
        if (buttonFirst != null) {
            buttonFirst.resizeTo(barWidth, barWidth);
        }
        if (buttonLast != null) {
            buttonLast.resizeTo(barWidth, barWidth);
        }
    }

    protected void setScrollbarColor(Color scrollbarColor) {
        this.scrollbarColor = scrollbarColor;
        cachedDrawable = null;
        compositor.setBackgroundColor(scrollbarColor);
    }

    @Override
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        if (type == ScrollbarType.VERTICAL) {
            backgroundGlyph.resizeTo(barWidth, getSize().getHeight() - 2 * barWidth);
        } else {
            backgroundGlyph.resizeTo(getSize().getWidth() - 2 * barWidth, barWidth);
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

            int visiblePercent = Math.min(//
                    100, //
                    (int) (intervalDisplayed * 100 / intervalTotal) //
            );

            int runnerHeight = (getSize().getHeight() - 2 * barWidth) * visiblePercent / 100;

            runnerSize = new Size(barWidth, runnerHeight);

            runnerGlyph.resizeTo(runnerSize);

            int offsetPercent = (int) (intervalOffset * 100 / intervalTotal);

            int offset = Math.min(//
                    this.barWidth + (getSize().getHeight() - 2 * barWidth) * offsetPercent / 100, //
                    getSize().getHeight() - this.barWidth - runnerHeight //
            );

            runnerGlyph.setRelativPosition(new Vector2d(0, offset));

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

            int visiblePercent = Math.min(//
                    100, //
                    (int) (intervalDisplayed * 100 / intervalTotal) //
            );

            int runnerWidth = (getSize().getWidth() - 2 * barWidth) * visiblePercent / 100;

            runnerSize = new Size(runnerWidth, barWidth);

            runnerGlyph.resizeTo(runnerSize);

            int offsetPercent = (int) (intervalOffset * 100 / intervalTotal);

            int offset = Math.min(//
                    this.barWidth + (getSize().getWidth() - 2 * barWidth) * offsetPercent / 100, //
                    getSize().getWidth() - this.barWidth - runnerWidth //
            );

            runnerGlyph.setRelativPosition(new Vector2d(offset, 0));

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

    @AllArgsConstructor
    private class MouseEventAdapterIntern implements MouseEventAdapter {
        private Scrollbar scrollbar;

        @Override
        public MouseDraggable mousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
            LOGGER.trace("Scrollbar onMousePressed. Position ({},{}).", mouseX, mouseY);
            return this.scrollbar;
        }

        @Override
        public MouseDraggable mouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
            LOGGER.trace("Scrollbar onMouseReleased. Position ({},{}).", mouseX, mouseY);
            return this.scrollbar;
        }

        @Override
        public void mouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
            if (direction == MouseWheelDirection.UP) {
                this.scrollbar.scrollCallback
                        .scrolledTo((int) (this.scrollbar.intervalOffset - this.scrollbar.barStep));
            } else {
                this.scrollbar.scrollCallback
                        .scrolledTo((int) (this.scrollbar.intervalOffset + this.scrollbar.barStep));
            }
        }

        private boolean isClickOn(Integer mouseX, Integer mouseY, Glyph glyph) {
            return CollisionHelper.isPointOnArea(mouseX, mouseY, glyph.getRelativPosition(), glyph.getSize());
        }

        @Override
        public void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button) {
            if (this.scrollbar.scrollCallback == null) {
                return;
            }

            if (isClickOn(mouseX, mouseY, this.scrollbar.buttonFirst)) {
                this.scrollbar.scrollCallback
                        .scrolledTo((int) (this.scrollbar.intervalOffset - this.scrollbar.buttonStep));
            } else if (isClickOn(mouseX, mouseY, this.scrollbar.buttonLast)) {
                this.scrollbar.scrollCallback
                        .scrolledTo((int) (this.scrollbar.intervalOffset + this.scrollbar.buttonStep));
            } else if (isClickOn(mouseX, mouseY, this.scrollbar.runnerGlyph)) {
                // TODO
            } else if (isClickOn(mouseX, mouseY, this.scrollbar.backgroundGlyph)) {
                boolean isClickFirst;
                if (type == ScrollbarType.VERTICAL) {
                    int barMiddle;
                    barMiddle = this.scrollbar.backgroundGlyph.getSize().getHeight() / 2
                            + this.scrollbar.backgroundGlyph.getRelativPosition().getY();
                    isClickFirst = mouseY < barMiddle;
                } else {
                    int barMiddle;
                    barMiddle = this.scrollbar.backgroundGlyph.getSize().getWidth() / 2
                            + this.scrollbar.backgroundGlyph.getRelativPosition().getX();
                    isClickFirst = mouseX < barMiddle;
                }

                if (isClickFirst) {
                    this.scrollbar.scrollCallback
                            .scrolledTo((int) (this.scrollbar.intervalOffset - this.scrollbar.barStep));
                } else {
                    this.scrollbar.scrollCallback
                            .scrolledTo((int) (this.scrollbar.intervalOffset + this.scrollbar.barStep));
                }
            }
        }
    }

    @AllArgsConstructor
    private class MouseDraggedIntern implements MouseDragAdapter {
        private Scrollbar scrollbar;

        @Override
        public void mouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
            Vector2d relPosToMain = this.scrollbar.getRelativPositionToMainParent();

            int mouseToElement;
            int mouseMin;
            int mouseMax;
            if (type == ScrollbarType.VERTICAL) {
                mouseToElement = mouseY - relPosToMain.getY();

                mouseMin = this.scrollbar.buttonFirst.getSize().getHeight() //
                        + this.scrollbar.runnerGlyph.getSize().getHeight() / 2 //
                ;
                mouseMax = this.scrollbar.buttonFirst.getSize().getHeight() //
                        + this.scrollbar.backgroundGlyph.getSize().getHeight() //
                        - this.scrollbar.runnerGlyph.getSize().getHeight() / 2 //
                ;
            } else {
                mouseToElement = mouseX - relPosToMain.getX();

                mouseMin = this.scrollbar.buttonFirst.getSize().getWidth() //
                        + this.scrollbar.runnerGlyph.getSize().getWidth() / 2 //
                ;
                mouseMax = this.scrollbar.buttonFirst.getSize().getWidth() //
                        + this.scrollbar.backgroundGlyph.getSize().getWidth() //
                        - this.scrollbar.runnerGlyph.getSize().getWidth() / 2 //
                ;
            }
            int mouseArea = mouseMax - mouseMin;

            int mouseToArea = Math.max(0, mouseToElement - mouseMin);
            mouseToArea = Math.min(mouseArea, mouseToArea);

            int offetPercent = mouseToArea * 100 / mouseArea;
            long realOffet = offetPercent * (intervalTotal - intervalDisplayed) / 100L;

            this.scrollbar.scrollCallback.scrolledTo((int) realOffet);
        }
    }
}
