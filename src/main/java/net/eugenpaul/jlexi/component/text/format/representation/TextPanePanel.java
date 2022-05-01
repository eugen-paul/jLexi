package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.ChangeListener;
import net.eugenpaul.jlexi.component.interfaces.GuiEvents;
import net.eugenpaul.jlexi.component.interfaces.TextUpdateable;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextRepresentationToColumnCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.structure.TextPaneDocument;
import net.eugenpaul.jlexi.component.text.keyhandler.AbstractKeyHandler;
import net.eugenpaul.jlexi.component.text.keyhandler.KeyHandlerable;
import net.eugenpaul.jlexi.component.text.keyhandler.TextPaneExtendedKeyHandler;
import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2SketchImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;
import net.eugenpaul.jlexi.visitor.Visitor;

public class TextPanePanel extends TextRepresentationOfRepresentation
        implements ChangeListener, GuiEvents, TextUpdateable, KeyHandlerable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPanePanel.class);

    private TextPaneDocument document;

    private TextCompositor<TextRepresentation> compositor;

    private TreeMap<Integer, TextRepresentation> yPositionToSite;

    private AbstractKeyHandler keyHandler;

    @Getter
    private Cursor mouseCursor;

    private ResourceManager storage;

    @Getter
    private final String cursorName;

    public TextPanePanel(String cursorPrefix, Glyph parent, ResourceManager storage, AbstractController controller) {
        super(parent);
        this.storage = storage;
        this.compositor = new TextRepresentationToColumnCompositor();
        this.cursorName = cursorPrefix + "textPaneCursor";

        this.document = new TextPaneDocument(//
                TextFormat.DEFAULT, //
                storage, //
                List.of(TextElementFactory.genNewLineChar(//
                        storage, //
                        TextFormat.DEFAULT, //
                        TextFormatEffect.DEFAULT_FORMAT_EFFECT//
                )), //
                this//
        );

        this.mouseCursor = new Cursor(null, null, controller, this.cursorName);

        this.yPositionToSite = new TreeMap<>();

        this.keyHandler = new TextPaneExtendedKeyHandler(this, storage);
    }

    @Override
    public Drawable getPixels() {
        if (cachedDrawable != null) {
            return cachedDrawable;
        }
        children.clear();

        children.addAll(compositor.compose(document.getRows(getSize()).iterator(), getSize()));

        // we must always draw the full area to override removed objects
        Size pixelSize = getSize();
        int[] pixels = new int[(int) pixelSize.compArea()];
        Arrays.fill(pixels, 0);

        cachedDrawable = new DrawableImpl(pixels, pixelSize);

        yPositionToSite.clear();

        Vector2d position = Vector2d.zero();
        for (var el : children) {
            ImageArrayHelper.copyRectangle(el.getPixels(), cachedDrawable, position);
            var yPosition = position.getY();

            yPositionToSite.put(yPosition, el);

            el.setRelativPosition(new Vector2d(0, yPosition));
            el.setParent(this);

            position.setY(yPosition + el.getSize().getHeight());
        }

        return cachedDrawable;
    }

    @Override
    public DrawableV2 getDrawable() {
        if (cachedDrawableV2 != null) {
            return cachedDrawableV2.draw();
        }

        children.clear();

        children.addAll(compositor.compose(document.getRows(getSize()).iterator(), getSize()));

        cachedDrawableV2 = new DrawableV2SketchImpl(Color.WHITE, getSize());

        yPositionToSite.clear();

        int currentY = 0;

        for (var el : children) {
            cachedDrawableV2.addDrawable(el.getDrawable(), 0, currentY);

            yPositionToSite.put(currentY, el);

            el.setRelativPosition(new Vector2d(0, currentY));
            el.setParent(this);

            currentY += el.getSize().getHeight();
        }

        return cachedDrawableV2.draw();
    }

    @Override
    public Iterator<Glyph> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public void visit(Visitor checker) {
        // Nothing to do
    }

    @Override
    public TextPosition getCorsorElementAt(Vector2d pos) {
        var row = yPositionToSite.floorEntry(pos.getY());
        if (null == row) {
            return null;
        }

        TextPosition clickedElement = row.getValue().getCorsorElementAt(//
                new Vector2d(//
                        pos.sub(row.getValue().getRelativPosition())//
                )//
        );
        if (clickedElement != null) {
            LOGGER.trace("Document Click on Element: {}.", clickedElement);
        } else {
            LOGGER.trace("Document Click on Element: NONE.");
        }
        return clickedElement;
    }

    @Override
    public void setText(List<TextElement> text) {
        LOGGER.trace("Set Document.text from List<TextElement>");
        document = new TextPaneDocument(TextFormat.DEFAULT, storage, text, this);
        notifyChange();
    }

    @Override
    public void resizeTo(Size size) {
        notifyChange();
        setSize(size);
        document.resetStructure();
    }

    @Override
    public boolean isResizeble() {
        return true;
    }

    @Override
    public void onMousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO
    }

    @Override
    public void onMouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {

        LOGGER.trace("Click on TextPane. Position ({},{}).", mouseX, mouseY);

        var row = yPositionToSite.floorEntry(mouseY);
        if (null == row) {
            return;
        }

        TextPosition clickedElement = row.getValue().getCorsorElementAt(//
                new Vector2d(//
                        mouseX - row.getValue().getRelativPosition().getX(), //
                        mouseY - row.getValue().getRelativPosition().getY() //
                )//
        );
        if (clickedElement != null) {
            LOGGER.trace("Document Click on Element: {}.", clickedElement);
            mouseCursor.moveCursorTo(clickedElement);
        } else {
            LOGGER.trace("Document Click on Element: NONE.");
        }
    }

    @Override
    public void onKeyTyped(Character key) {
        LOGGER.trace("Key typed: {}", key);
        keyHandler.onKeyTyped(key);
    }

    @Override
    public void onKeyPressed(KeyCode keyCode) {
        LOGGER.trace("Key pressed: {}", keyCode);
        keyHandler.onKeyPressed(keyCode);
    }

    @Override
    public void onKeyReleased(KeyCode keyCode) {
        LOGGER.trace("Key released: {}", keyCode);
        keyHandler.onKeyReleased(keyCode);
    }

    @Override
    public TextRepresentation getTextRepresentation() {
        return this;
    }
}
