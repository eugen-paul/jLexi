package net.eugenpaul.jlexi.component.text;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.formatting.Composition;
import net.eugenpaul.jlexi.component.formatting.text.RowCompositor;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.component.text.keyhandler.KeyHandlerable;
import net.eugenpaul.jlexi.component.text.keyhandler.TestPaneKeyHandler;
import net.eugenpaul.jlexi.component.text.structure.CharGlyph;
import net.eugenpaul.jlexi.component.text.structure.TextPlaceHolder;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.effect.CursorEffect;
import net.eugenpaul.jlexi.effect.EffectHandler;
import net.eugenpaul.jlexi.effect.TextPaneEffect;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Verctor2d;
import net.eugenpaul.jlexi.utils.container.NodeList;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.CollisionHelper;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;
import net.eugenpaul.jlexi.visitor.Visitor;

/**
 * Display Rows.
 */
public class TextPane extends Composition<TextPaneElement> implements GuiComponent, KeyHandlerable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPane.class);

    private NodeList<TextPaneElement> nodeList;

    private List<TextPaneElement> formattedChildren;

    @Getter
    private FontStorage fontStorage;

    @Getter
    private EffectHandler effectHandler;

    @Getter
    @Setter
    private TextPaneEffect cursor;

    private TestPaneKeyHandler keyHanlder;

    public TextPane(Glyph parent, FontStorage fontStorage, EffectHandler effectWorker) {
        super(parent);
        this.effectHandler = effectWorker;
        this.fontStorage = fontStorage;
        this.cursor = null;
        setCompositor(new RowCompositor(this));
        resizeTo(Size.ZERO_SIZE);
        nodeList = new NodeList<>();
        formattedChildren = Collections.emptyList();

        addPlaceHolder();

        keyHanlder = new TestPaneKeyHandler(this);
    }

    @Override
    public Drawable getPixels() {
        formattedChildren = compositor.compose(nodeList.iterator(), getSize().getWidth());

        List<Drawable> childDrawable = formattedChildren.stream()//
                .map(Glyph::getPixels)//
                .collect(Collectors.toList());

        int width = 0;
        int hight = 0;

        for (Drawable drawable : childDrawable) {
            width = Math.max(width, drawable.getPixelSize().getWidth());
            hight += drawable.getPixelSize().getHight();
        }

        int[] pixels = new int[width * hight];
        Size pixelsSize = new Size(width, hight);

        int positionX = 0;
        int positionY = 0;
        Iterator<TextPaneElement> textFieldIterator = formattedChildren.iterator();
        for (Drawable drawable : childDrawable) {
            if (textFieldIterator.hasNext()) {
                Glyph currentRow = textFieldIterator.next();
                currentRow.getRelativPosition().setX(positionX);
                currentRow.getRelativPosition().setY(positionY);
            }
            ImageArrayHelper.copyRectangle(//
                    drawable.getPixels(), //
                    drawable.getPixelSize(), //
                    new Verctor2d(0, 0), //
                    drawable.getPixelSize(), //
                    pixels, //
                    pixelsSize, //
                    new Verctor2d(positionX, positionY)//
            );
            positionY += drawable.getPixelSize().getHight();
        }

        return new DrawableImpl(pixels, pixelsSize);
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

        int i = 0;
        for (TextPaneElement textElement : formattedChildren) {
            if (CollisionHelper.isPointOnArea(//
                    new Verctor2d(mouseX, mouseY), textElement.getRelativPosition(), //
                    textElement.getSize())//
            ) {
                LOGGER.trace("Click on row {}", i);
                var nodeElement = textElement.getTextPaneListElement();

                if (textElement.isCursorHoldable()) {
                    var clickNodeElem = textElement.onMouseClickTE(//
                            mouseX - textElement.getRelativPosition().getX(), //
                            mouseY - textElement.getRelativPosition().getY(), //
                            button //
                    );
                    if (clickNodeElem != null) {
                        nodeElement = clickNodeElem;
                    }
                }

                if (cursor != null) {
                    effectHandler.removeEffect(cursor);
                }
                cursor = new CursorEffect(nodeElement.getData());
                nodeElement.getData().addEffect(cursor);
                effectHandler.addEffect(cursor);
                break;
            }
            i++;
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
    }

    private void addPlaceHolder() {
        // Last charakter is always a Place-Holder
        TextPlaceHolder placeHolder = new TextPlaceHolder(this, fontStorage, null);
        NodeListElement<TextPaneElement> textPaneListElement = nodeList.addLast(placeHolder);
        placeHolder.setTextPaneListElement(textPaneListElement);
    }

    @Override
    public void resizeTo(Size size) {
        setSize(size);
    }

    @Override
    public void notifyUpdate(Glyph child) {
        LOGGER.trace("textPane notifyUpdate to parent");
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
}
