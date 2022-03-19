package net.eugenpaul.jlexi.component.text;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.component.interfaces.TextUpdateable;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextStructureFormToColumnCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureFormOfStructures;
import net.eugenpaul.jlexi.component.text.format.structure.TextPaneDocument;
import net.eugenpaul.jlexi.component.text.keyhandler.AbstractKeyHandler;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorMove;
import net.eugenpaul.jlexi.component.text.keyhandler.KeyHandlerable;
import net.eugenpaul.jlexi.component.text.keyhandler.TextPaneExtendedKeyHandler;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.effect.EffectController;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;
import net.eugenpaul.jlexi.visitor.Visitor;

/**
 * Display Rows.
 */
public class TextPane extends TextStructureFormOfStructures implements GuiComponent, KeyHandlerable, TextUpdateable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPane.class);

    private TextPaneDocument document;

    private TextCompositor<TextStructureForm> compositor;

    private TreeMap<Integer, TextStructureForm> yPositionToSite;

    private AbstractKeyHandler keyHandler;

    @Getter
    private Cursor mouseCursor;

    @Getter
    private ResourceManager storage;

    private boolean editMode = false;

    private TextFormat defaulTextFormat;

    public TextPane(Glyph parent, ResourceManager storage, EffectController effectWorker) {
        super(parent);
        this.storage = storage;
        this.compositor = new TextStructureFormToColumnCompositor();

        storage.getFonts();
        this.defaulTextFormat = TextFormat.builder()//
                .fontName(FontStorage.DEFAULT_FONT_NAME)//
                .fontsize(FontStorage.DEFAULT_FONT_SIZE)//
                .bold(false)//
                .italic(false)//
                .build();

        this.document = new TextPaneDocument(defaulTextFormat, storage, "Hello, World!", this);

        this.yPositionToSite = new TreeMap<>();

        this.mouseCursor = new Cursor(null, null, effectWorker);

        this.keyHandler = new TextPaneExtendedKeyHandler(this, storage);

        resizeTo(Size.ZERO_SIZE);
    }

    @Override
    public Drawable getPixels() {
        var sites = compositor.compose(document.getRows(getSize()).iterator(), getSize());

        children.clear();

        for (var el : sites) {
            children.add(el);
        }

        // we must always draw the full area to override removed objects
        Size pixelSize = new Size(size.getWidth(), size.getHeight());
        int[] pixels = new int[size.getWidth() * size.getHeight()];
        Arrays.fill(pixels, 0);

        cachedDrawable = new DrawableImpl(pixels, pixelSize);

        yPositionToSite.clear();

        Vector2d position = new Vector2d(0, 0);
        for (var el : sites) {
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
    public Iterator<Glyph> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public void visit(Visitor checker) {
        // Nothing to do
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on TextPane. Position ({},{}).", mouseX, mouseY);

        var row = yPositionToSite.floorEntry(mouseY);
        if (null == row) {
            return;
        }

        TextElement clickedElement = row.getValue().getCorsorElementAt(//
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
    public void notifyRedraw(Drawable drawData, Vector2d position, Size size) {
        super.notifyRedraw(drawData, position, size);
        LOGGER.trace("Recive Redraw from schild");
    }

    @Override
    public void setText(String text) {
        LOGGER.trace("Set Document.text to \"{}\".", text);
        document = new TextPaneDocument(defaulTextFormat, storage, text, this);
        getPixels();
    }

    @Override
    public void setText(List<TextElement> text) {
        LOGGER.trace("Set Document.text from List<TextElement>");
        document = new TextPaneDocument(defaulTextFormat, storage, text, this);
        getPixels();
    }

    public void notifyChange() {
        cachedDrawable = null;
        if (!editMode) {
            parent.notifyRedraw(getPixels(), relativPosition, size);
        }
    }

    @Override
    public void resizeTo(Size size) {
        setSize(size);
        cachedDrawable = null;
        document.resetStructure();
    }

    @Override
    public void onKeyTyped(Character key) {
        LOGGER.trace("Key typed: {}", key);
        editMode = true;
        try {
            keyHandler.onKeyTyped(key);
        } finally {
            editMode = false;
        }
        parent.notifyRedraw(getPixels(), relativPosition, size);
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
    public Glyph getThis() {
        return this;
    }

    @Override
    public void doCursorMove(CursorMove cursorMove) {
        LOGGER.trace("do cursor move: {}", cursorMove);
    }

    @Override
    public void keyUpdate() {
        parent.notifyRedraw(getPixels(), relativPosition, size);
    }

    @Override
    public EffectController getEffectHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getCorsorElementAt(Vector2d pos) {
        var row = yPositionToSite.floorEntry(pos.getY());
        if (null == row) {
            return null;
        }

        TextElement clickedElement = row.getValue().getCorsorElementAt(//
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
    public TextStructureForm getTextStructureForm() {
        return this;
    }

}
