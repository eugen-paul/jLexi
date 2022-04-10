package net.eugenpaul.jlexi.component.menubar;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiCompenentMonoGlyph;
import net.eugenpaul.jlexi.component.button.Button;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.CollisionHelper;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

/**
 * Add Menubar to Glyph
 */
public abstract class MenuBar extends GuiCompenentMonoGlyph {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuBar.class);

    private static final Drawable EMPTY_DRAWABLE = DrawableImpl.EMPTY_DRAWABLE;

    protected Drawable menuBackground;

    protected int menubarHeight = 0;
    private int menubarPadding = 2;

    private List<Button> menuButtons;

    /**
     * C'tor
     * 
     * @param component
     */
    protected MenuBar(Glyph parent, GuiComponent component, Size size) {
        super(parent, component);
        this.component.getGlyph().setParent(this);
        this.component.getGlyph().setRelativPosition(new Vector2d(0, menubarHeight));
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

        this.component.getGlyph().setRelativPosition(new Vector2d(0, this.menubarHeight));

        computeBackground();
        return true;
    }

    protected abstract void computeBackground();

    @Override
    public Drawable getPixels() {
        if (getSize().isZero()) {
            return EMPTY_DRAWABLE;
        }

        Drawable componentPixels = super.getPixels();

        int[] responsePixels = new int[getSize().getHeight() * getSize().getWidth()];

        System.arraycopy(menuBackground.getPixels(), 0, responsePixels, 0, menuBackground.getPixels().length);

        cachedDrawable = new DrawableImpl(responsePixels, getSize());

        Vector2d pos = new Vector2d(menubarPadding, menubarPadding);
        for (Button button : menuButtons) {
            ImageArrayHelper.copyRectangle(//
                    button.getPixels(), //
                    cachedDrawable, //
                    pos//
            );
            button.setRelativPosition(new Vector2d(pos));
            pos.setX(pos.getX() + button.getSize().getWidth() + menubarPadding);
        }

        System.arraycopy(//
                componentPixels.getPixels(), //
                0, //
                responsePixels, //
                menuBackground.getPixels().length, //
                componentPixels.getPixels().length//
        );

        return cachedDrawable;
    }

    @Override
    public boolean isResizeble() {
        return true;
    }

    @Override
    public void resizeTo(Size newSize) {
        setSize(newSize);

        computeBackground();

        getPixels();

        notifyRedraw(cachedDrawable, relativPosition, newSize);
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseY <= menubarHeight) {
            LOGGER.trace("Click on Menu. Position ({},{}).", mouseX, mouseY);
            for (Button menuButton : menuButtons) {
                if (CollisionHelper.isPointOnArea(mouseX, mouseY, menuButton.getRelativPosition(),
                        menuButton.getSize())) {
                    menuButton.onMouseClick(//
                            mouseX - menuButton.getRelativPosition().getX(), //
                            mouseY - menuButton.getRelativPosition().getY(), //
                            button //
                    );
                    return;
                }
            }
        } else {
            LOGGER.trace("Click on inner component. Position ({},{}). Item Position ({},{}).", mouseX, mouseY, mouseX,
                    mouseY - menubarHeight);
            component.onMouseClick(mouseX, mouseY - menubarHeight, button);
        }
    }

    @Override
    public void onMousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onKeyTyped(Character key) {
        component.onKeyTyped(key);
    }

    @Override
    public void onKeyPressed(KeyCode keyCode) {
        component.onKeyPressed(keyCode);
    }

    @Override
    public void onKeyReleased(KeyCode keyCode) {
        component.onKeyReleased(keyCode);
    }

    @Override
    public Drawable getPixels(Vector2d position, Size size) {
        // TODO get Pixels from cachedDrawable
        return component.getGlyph().getPixels(new Vector2d(position.getX(), position.getY() - menubarHeight), size);
    }

    @Override
    public void notifyRedraw(Drawable drawData, Vector2d relativPosition, Size size) {
        // TODO add Pixels to cachedDrawable
        cachedDrawable = null;
        if (parent != null) {
            parent.notifyRedraw(drawData, relativPosition.addNew(this.relativPosition), size);
        }
    }
}
