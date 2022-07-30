package net.eugenpaul.jlexi.component.menubar;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiCompenentMonoGlyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.button.Button;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.CollisionHelper;

/**
 * Add Menubar to Glyph
 */
@Slf4j
public abstract class MenuBar extends GuiCompenentMonoGlyph {

    private static final Drawable EMPTY_DRAWABLE = DrawablePixelsImpl.EMPTY;

    protected Drawable menuBackground;

    protected int menubarHeight = 0;
    private int menubarPadding = 2;

    private List<Button> menuButtons;

    /**
     * C'tor
     * 
     * @param component
     */
    protected MenuBar(Glyph parent, GuiGlyph component, Size size) {
        super(parent, component);
        this.component.setRelativPosition(new Vector2d(0, menubarHeight));
        setSize(size);
        this.menuButtons = new LinkedList<>();

        computeBackground();
    }

    public boolean addMenuButton(Button button) {
        this.menuButtons.add(button);

        this.menubarHeight = this.menuButtons.stream().map(v -> v.getSize().getHeight()).reduce(0, Math::max);
        if (this.menubarHeight > 0) {
            this.menubarHeight += this.menubarPadding * 2;
        }

        this.component.setRelativPosition(new Vector2d(0, this.menubarHeight));

        computeBackground();
        return true;
    }

    protected abstract void computeBackground();

    @Override
    public Drawable getDrawable() {
        if (cachedDrawable != null) {
            return cachedDrawable.draw();
        }

        if (getSize().isZero()) {
            return EMPTY_DRAWABLE;
        }

        Drawable componentDrawable = super.getDrawable();

        cachedDrawable = new DrawableSketchImpl(Color.WHITE, getSize());

        cachedDrawable.addDrawable(menuBackground, 0, 0);

        Vector2d pos = new Vector2d(menubarPadding, menubarPadding);
        for (Button button : menuButtons) {
            cachedDrawable.addDrawable(button.getDrawable(), pos.getX(), pos.getY(), 1);
            button.setRelativPosition(new Vector2d(pos));
            pos.setX(pos.getX() + button.getSize().getWidth() + menubarPadding);
        }

        cachedDrawable.addDrawable(componentDrawable, 0, menuBackground.getSize().getHeight(), 1);

        return cachedDrawable.draw();
    }

    @Override
    public boolean isResizeble() {
        return true;
    }

    @Override
    public void resizeTo(Size newSize) {
        setSize(newSize);

        computeBackground();

        getDrawable();

        redraw();
    }

    @Override
    protected boolean isPositionOnComponent(Integer mouseX, Integer mouseY) {
        return mouseY >= menubarHeight;
    }

    private GuiGlyph getMenuElement(Integer mouseX, Integer mouseY) {
        for (Button menuButton : menuButtons) {
            if (CollisionHelper.isPointOnArea(mouseX, mouseY, menuButton.getRelativPosition(), menuButton.getSize())) {
                return menuButton;
            }
        }
        return null;
    }

    @Override
    protected void onMouseClickOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on Menu. Position ({},{}).", mouseX, mouseY);
        var menuElement = getMenuElement(mouseX, mouseY);
        if (menuElement != null) {
            menuElement.onMouseClick(//
                    mouseX - menuElement.getRelativPosition().getX(), //
                    mouseY - menuElement.getRelativPosition().getY(), //
                    button //
            );
        }
    }

    @Override
    protected MouseDraggable onMousePressedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        var menuElement = getMenuElement(mouseX, mouseY);
        if (menuElement != null) {
            return menuElement.onMousePressed(//
                    mouseX - menuElement.getRelativPosition().getX(), //
                    mouseY - menuElement.getRelativPosition().getY(), //
                    button //
            );
        }
        return null;
    }

    @Override
    protected MouseDraggable onMouseReleasedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        var menuElement = getMenuElement(mouseX, mouseY);
        if (menuElement != null) {
            return menuElement.onMouseReleased(//
                    mouseX - menuElement.getRelativPosition().getX(), //
                    mouseY - menuElement.getRelativPosition().getY(), //
                    button //
            );
        }
        return null;
    }

}
