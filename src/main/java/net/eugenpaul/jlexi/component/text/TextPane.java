package net.eugenpaul.jlexi.component.text;

import java.util.Iterator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.component.text.formatting.RowCompositor;
import net.eugenpaul.jlexi.component.text.formatting.TextCompositor;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorMove;
import net.eugenpaul.jlexi.component.text.keyhandler.KeyHandlerable;
import net.eugenpaul.jlexi.component.text.keyhandler.TextPaneKeyHandler;
import net.eugenpaul.jlexi.component.text.structure.CharGlyph;
import net.eugenpaul.jlexi.component.text.structure.TextPlaceHolder;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.effect.EffectHandler;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.container.NodeList;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;
import net.eugenpaul.jlexi.visitor.Visitor;

/**
 * Display Rows.
 */
public class TextPane extends Glyph implements GuiComponent, KeyHandlerable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPane.class);

    private NodeList<TextPaneElement> nodeList;

    private TextCompositor<TextPaneElement> compositor;

    @Getter
    private FontStorage fontStorage;

    @Getter
    private EffectHandler effectHandler;

    @Getter
    @Setter
    private Cursor mouseCursor;

    private boolean recompose;

    private boolean cached;
    private Drawable cachedDrawable;
    private LinkedList<Glyph> updatedChildren;
    private LinkedList<Area> updatedAreas;

    private TextPaneKeyHandler keyHanlder;

    public TextPane(Glyph parent, FontStorage fontStorage, EffectHandler effectWorker) {
        super(parent);
        this.effectHandler = effectWorker;
        this.fontStorage = fontStorage;
        compositor = new RowCompositor<>(this, new Size(getSize().getWidth(), Integer.MAX_VALUE));
        nodeList = new NodeList<>();

        mouseCursor = new Cursor(null, null, effectHandler);
        recompose = true;
        cached = false;

        updatedChildren = new LinkedList<>();
        updatedAreas = new LinkedList<>();

        resizeTo(Size.ZERO_SIZE);
        addPlaceHolder();

        keyHanlder = new TextPaneKeyHandler(this);
    }

    @Override
    public Drawable getPixels() {
        if (cached && !recompose) {
            return cachedDrawable;
        }

        compositor.compose(nodeList.iterator());

        cachedDrawable = compositor.getPixels();
        cached = true;
        recompose = false;
        updatedChildren.clear();
        updatedAreas.clear();

        return cachedDrawable;
    }

    @Override
    public Iterator<Glyph> iterator() {
        return NULLITERATOR;
    }

    @Override
    public void visit(Visitor checker) {
        // Nothing to do
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on TextPane. Position ({},{}).", mouseX, mouseY);

        TextPaneElement element = compositor.getElementOnPosition(new Vector2d(mouseX, mouseY));
        if (element != null) {
            mouseCursor.moveCursorTo(element.textPaneListElement);
        }
    }

    public void setText(String text) {

        nodeList.clear();

        for (int i = 0; i < text.length(); i++) {
            CharGlyph glyph = new CharGlyph(this, text.charAt(i), fontStorage, null);
            NodeListElement<TextPaneElement> textPaneListElement = nodeList.addLast(glyph);
            glyph.setTextPaneListElement(textPaneListElement);
        }

        addPlaceHolder();

        mouseCursor.moveCursorTo(nodeList.getFirstNode());
    }

    private void addPlaceHolder() {
        // Last charakter is always a Place-Holder
        TextPlaceHolder placeHolder = new TextPlaceHolder(this, fontStorage, null);
        NodeListElement<TextPaneElement> textPaneListElement = nodeList.addLast(placeHolder);
        placeHolder.setTextPaneListElement(textPaneListElement);
    }

    @Override
    public void resizeTo(Size size) {
        compositor.updateSize(new Size(size.getWidth(), Integer.MAX_VALUE));
        setSize(size);

        cached = false;
        recompose = true;
        updatedChildren.clear();
        updatedAreas.clear();

    }

    @Override
    public void notifyUpdate(Glyph child) {
        LOGGER.trace("textPane notifyUpdate to parent");
        cached = false;
        recompose = true;
        updatedChildren.clear();
        updatedAreas.clear();
        getParent().notifyUpdate(this);
    }

    @Override
    public void onKeyTyped(Character key) {
        keyHanlder.onKeyTyped(key);
    }

    @Override
    public void onKeyPressed(KeyCode keyCode) {
        keyHanlder.onKeyPressed(keyCode);
    }

    @Override
    public void onKeyReleased(KeyCode keyCode) {
        keyHanlder.onKeyReleased(keyCode);
    }

    @Override
    public Glyph getThis() {
        return this;
    }

    @Override
    public void doCursorMove(CursorMove cursorMove) {
        compositor.moveCursor(cursorMove, mouseCursor);
    }

    @Override
    public void notifyRedraw(Glyph child, Vector2d position, Size size) {
        cached = false;
        recompose = false;
        updatedChildren.add(child);
        updatedAreas.add(new Area(position, size));

        parent.notifyRedraw(this, child.getRelativPosition().addNew(position), size);
    }

    @Override
    public Drawable getPixels(Vector2d position, Size size) {
        if (recompose) {
            getPixels();
        }

        if (!cached) {
            return updateAndGetPixels(position, size);
        }

        if (position.getX() == 0 && position.getY() == 0 //
                && this.size.equals(size)) {
            return getPixels();
        }

        int[] pixels = new int[size.getWidth() * size.getHight()];

        ImageArrayHelper.copyRectangle(//
                cachedDrawable.getPixels(), //
                cachedDrawable.getPixelSize(), //
                position, //
                size, //
                pixels, //
                size, //
                Vector2d.zero() //
        );

        return new DrawableImpl(pixels, size);
    }

    private Drawable updateAndGetPixels(Vector2d position, Size size) {
        Iterator<Glyph> updatedChildrenIterator = updatedChildren.iterator();
        Iterator<Area> updatedAreaIterator = updatedAreas.iterator();

        while (updatedChildrenIterator.hasNext() && updatedAreaIterator.hasNext()) {
            var child = updatedChildrenIterator.next();
            var area = updatedAreaIterator.next();

            var childDrawable = child.getPixels(area.getPosition(), area.getSize());

            ImageArrayHelper.copyRectangle(//
                    childDrawable.getPixels(), //
                    childDrawable.getPixelSize(), //
                    Vector2d.zero(), //
                    childDrawable.getPixelSize(), //
                    cachedDrawable.getPixels(), //
                    cachedDrawable.getPixelSize(), //
                    child.getRelativPosition() //
            );
        }

        updatedChildren.clear();
        updatedAreas.clear();
        cached = true;

        int[] t = new int[size.getWidth() * size.getHight()];
        ImageArrayHelper.copyRectangle(//
                cachedDrawable.getPixels(), //
                cachedDrawable.getPixelSize(), //
                position, //
                size, //
                t, //
                size, //
                Vector2d.zero() //
        );

        return new DrawableImpl(t, size);
    }

    @Override
    public void keyUpdate() {
        cached = false;
        recompose = true;
        updatedChildren.clear();
        updatedAreas.clear();
        parent.notifyUpdate(this);
    }

}
