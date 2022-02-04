package net.eugenpaul.jlexi.component.text;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.component.interfaces.TextUpdateable;
import net.eugenpaul.jlexi.component.text.formatting.RowCompositor;
import net.eugenpaul.jlexi.component.text.formatting.TextCompositor;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorMove;
import net.eugenpaul.jlexi.component.text.keyhandler.KeyHandlerable;
import net.eugenpaul.jlexi.component.text.keyhandler.TextPaneKeyHandler;
import net.eugenpaul.jlexi.component.text.structure.CharGlyph;
import net.eugenpaul.jlexi.component.text.structure.TextPlaceHolder;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.effect.EffectHandler;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.container.NodeList;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.visitor.Visitor;

/**
 * Display Rows.
 */
public class TextPane extends Glyph implements GuiComponent, KeyHandlerable, TextUpdateable {

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

    private TextPaneKeyHandler keyHanlder;

    public TextPane(Glyph parent, FontStorage fontStorage, EffectHandler effectWorker) {
        super(parent);
        this.effectHandler = effectWorker;
        this.fontStorage = fontStorage;
        compositor = new RowCompositor<>(this, new Size(getSize().getWidth(), Integer.MAX_VALUE));
        nodeList = new NodeList<>();

        mouseCursor = new Cursor(null, null, effectHandler);

        resizeTo(Size.ZERO_SIZE);
        addPlaceHolder();

        keyHanlder = new TextPaneKeyHandler(this);
    }

    @Override
    public Drawable getPixels() {
        compositor.compose(nodeList.iterator());

        cachedDrawable = compositor.getPixels();

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

    @Override
    public void setText(String text) {

        nodeList.clear();

        for (int i = 0; i < text.length(); i++) {
            CharGlyph glyph = new CharGlyph(this, text.charAt(i), fontStorage, null);
            NodeListElement<TextPaneElement> textPaneListElement = nodeList.addLast(glyph);
            glyph.setTextPaneListElement(textPaneListElement);
        }

        addPlaceHolder();

        getPixels();

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
    public void keyUpdate() {
        parent.notifyRedraw(getPixels(), relativPosition, size);
    }

}
