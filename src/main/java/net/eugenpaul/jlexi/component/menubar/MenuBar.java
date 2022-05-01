package net.eugenpaul.jlexi.component.menubar;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiCompenentMonoGlyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.button.Button;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2PixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableV2SketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.CollisionHelper;

/**
 * Add Menubar to Glyph
 */
public abstract class MenuBar extends GuiCompenentMonoGlyph {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuBar.class);

    private static final DrawableV2 EMPTY_DRAWABLE_V2 = DrawableV2PixelsImpl.EMPTY;

    protected DrawableV2 menuBackground2;

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
    public DrawableV2 getDrawable() {
        if (cachedDrawableV2 != null) {
            return cachedDrawableV2.draw();
        }

        if (getSize().isZero()) {
            return EMPTY_DRAWABLE_V2;
        }

        DrawableV2 componentDrawable = super.getDrawable();

        cachedDrawableV2 = new DrawableV2SketchImpl(Color.WHITE, getSize());

        cachedDrawableV2.addDrawable(menuBackground2, 0, 0);

        Vector2d pos = new Vector2d(menubarPadding, menubarPadding);
        for (Button button : menuButtons) {
            cachedDrawableV2.addDrawable(button.getDrawable(), pos.getX(), pos.getY(), 1);
            button.setRelativPosition(new Vector2d(pos));
            pos.setX(pos.getX() + button.getSize().getWidth() + menubarPadding);
        }

        cachedDrawableV2.addDrawable(componentDrawable, 0, menuBackground2.getSize().getHeight(), 1);

        return cachedDrawableV2.draw();
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

    @Override
    protected void onMouseClickOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on Menu. Position ({},{}).", mouseX, mouseY);
        for (Button menuButton : menuButtons) {
            if (CollisionHelper.isPointOnArea(mouseX, mouseY, menuButton.getRelativPosition(), menuButton.getSize())) {
                menuButton.onMouseClick(//
                        mouseX - menuButton.getRelativPosition().getX(), //
                        mouseY - menuButton.getRelativPosition().getY(), //
                        button //
                );
                return;
            }
        }
    }

    @Override
    protected void onMousePressedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onMouseReleasedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO Auto-generated method stub

    }

}
