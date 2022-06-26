package net.eugenpaul.jlexi.component.scrollpane;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.button.Button;
import net.eugenpaul.jlexi.component.formatting.ToSingleGlyphCompositor;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.component.panes.ImageGlyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;
import net.eugenpaul.jlexi.utils.helper.CollisionHelper;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class Scrollbar extends GuiGlyph implements MouseDraggable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scrollbar.class);

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
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        if (scrollCallback == null) {
            return;
        }

        if (CollisionHelper.isPointOnArea(mouseX, mouseY, buttonFirst.getRelativPosition(), buttonFirst.getSize())) {
            scrollCallback.scrolledTo((int) (intervalOffset - buttonStep));
        } else if (CollisionHelper.isPointOnArea(mouseX, mouseY, buttonLast.getRelativPosition(),
                buttonLast.getSize())) {
            scrollCallback.scrolledTo((int) (intervalOffset + buttonStep));
        } else if (CollisionHelper.isPointOnArea(mouseX, mouseY, runnerGlyph.getRelativPosition(),
                runnerGlyph.getSize())) {
            // TODO
        } else if (CollisionHelper.isPointOnArea(mouseX, mouseY, backgroundGlyph.getRelativPosition(),
                backgroundGlyph.getSize())) {
            boolean isClickFirst;
            if (type == ScrollbarType.VERTICAL) {
                int barMiddle;
                barMiddle = backgroundGlyph.getSize().getHeight() / 2 + backgroundGlyph.getRelativPosition().getY();
                isClickFirst = mouseY < barMiddle;
            } else {
                int barMiddle;
                barMiddle = backgroundGlyph.getSize().getWidth() / 2 + backgroundGlyph.getRelativPosition().getX();
                isClickFirst = mouseX < barMiddle;
            }

            if (isClickFirst) {
                scrollCallback.scrolledTo((int) (intervalOffset - barStep));
            } else {
                scrollCallback.scrolledTo((int) (intervalOffset + barStep));
            }
        }
    }

    @Override
    public void onMouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
        Vector2d relPosToMain = getRelativPositionToMainParent();

        int mouseToElement;
        int mouseMin;
        int mouseMax;
        if (type == ScrollbarType.VERTICAL) {
            mouseToElement = mouseY - relPosToMain.getY();

            mouseMin = this.buttonFirst.getSize().getHeight() //
                    + this.runnerGlyph.getSize().getHeight() / 2 //
            ;
            mouseMax = this.buttonFirst.getSize().getHeight() //
                    + this.backgroundGlyph.getSize().getHeight() //
                    - this.runnerGlyph.getSize().getHeight() / 2 //
            ;
        } else {
            mouseToElement = mouseX - relPosToMain.getX();

            mouseMin = this.buttonFirst.getSize().getWidth() //
                    + this.runnerGlyph.getSize().getWidth() / 2 //
            ;
            mouseMax = this.buttonFirst.getSize().getWidth() //
                    + this.backgroundGlyph.getSize().getWidth() //
                    - this.runnerGlyph.getSize().getWidth() / 2 //
            ;
        }
        int mouseArea = mouseMax - mouseMin;

        int mouseToArea = Math.max(0, mouseToElement - mouseMin);
        mouseToArea = Math.min(mouseArea, mouseToArea);

        int offetPercent = mouseToArea * 100 / mouseArea;
        long realOffet = offetPercent * (intervalTotal - intervalDisplayed) / 100L;

        this.scrollCallback.scrolledTo((int) realOffet);
    }

    @Override
    public MouseDraggable onMousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Scrollbar onMousePressed. Position ({},{}).", mouseX, mouseY);
        return this;
    }

    @Override
    public MouseDraggable onMouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Scrollbar onMouseReleased. Position ({},{}).", mouseX, mouseY);
        return this;
    }

    @Override
    public void onMouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
        if (direction == MouseWheelDirection.UP) {
            scrollCallback.scrolledTo((int) (intervalOffset - barStep));
        } else {
            scrollCallback.scrolledTo((int) (intervalOffset + barStep));
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
