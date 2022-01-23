package net.eugenpaul.jlexi.data.framing;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.DrawableImpl;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Position;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.design.GuiComponent;
import net.eugenpaul.jlexi.data.effect.CursorEffect;
import net.eugenpaul.jlexi.data.effect.EffectHandler;
import net.eugenpaul.jlexi.data.effect.TextPaneEffect;
import net.eugenpaul.jlexi.data.formatting.Composition;
import net.eugenpaul.jlexi.data.formatting.text.RowCompositor;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.stucture.CharGlyph;
import net.eugenpaul.jlexi.data.stucture.EndOfLine;
import net.eugenpaul.jlexi.data.stucture.TextPaneElement;
import net.eugenpaul.jlexi.data.stucture.TextPlaceHolder;
import net.eugenpaul.jlexi.data.visitor.Visitor;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.CharacterHelper;
import net.eugenpaul.jlexi.utils.Collisions;
import net.eugenpaul.jlexi.utils.GlyphNodeList;
import net.eugenpaul.jlexi.utils.ImageArrays;
import net.eugenpaul.jlexi.utils.NodeList.NodeListElement;

/**
 * Display Rows.
 */
public class TextPane extends Composition<Glyph> implements GuiComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPane.class);
    private GlyphNodeList nodeList;

    private List<Drawable> childDrawable;
    private List<Glyph> textField;

    private FontStorage fontStorage;
    private EffectHandler effectHandler;

    private TextPaneEffect currectCursorEffect;

    public TextPane(Glyph parent, FontStorage fontStorage, EffectHandler effectWorker) {
        super(parent);
        this.effectHandler = effectWorker;
        this.fontStorage = fontStorage;
        this.currectCursorEffect = null;
        setCompositor(new RowCompositor(this));
        resizeTo(Size.ZERO_SIZE);
        nodeList = new GlyphNodeList();
        childDrawable = Collections.emptyList();
        textField = Collections.emptyList();

        addPlaceHolder();
    }

    @Override
    public Drawable getPixels() {
        textField = compositor.compose(nodeList.iterator(), getSize().getWidth());

        childDrawable = textField.stream()//
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
        Iterator<Glyph> textFieldIterator = textField.iterator();
        for (Drawable drawable : childDrawable) {
            if (textFieldIterator.hasNext()) {
                Glyph currentRow = textFieldIterator.next();
                currentRow.getRelativPosition().setPosW(positionX);
                currentRow.getRelativPosition().setPosH(positionY);
            }
            ImageArrays.copyRectangle(//
                    drawable.getPixels(), //
                    drawable.getPixelSize(), //
                    0, //
                    0, //
                    drawable.getPixelSize(), //
                    pixels, //
                    pixelsSize, //
                    positionX, //
                    positionY//
            );
            positionY += drawable.getPixelSize().getHight();
        }

        return new DrawableImpl(pixels, pixelsSize);
    }

    @Override
    public GlyphIterator createIterator() {
        return null;
    }

    @Override
    public void visit(Visitor checker) {
        // Nothing to do
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on TextPane. Position ({},{}).", mouseX, mouseY);

        int i = 0;
        for (Glyph row : textField) {
            if (Collisions.isPointOnArea(new Position(mouseX, mouseY), row.getRelativPosition(), row.getSize())) {
                LOGGER.trace("Click on row {}", i);
                if (row instanceof TextElementClickable) {
                    TextElementClickable g = (TextElementClickable) row;
                    NodeListElement<TextPaneElement> elem = g.onMouseClickTE(//
                            mouseX - row.getRelativPosition().getPosW(), //
                            mouseY - row.getRelativPosition().getPosH(), //
                            button //
                    );

                    if (currectCursorEffect != null) {
                        effectHandler.removeEffect(currectCursorEffect);
                    }
                    currectCursorEffect = new CursorEffect(elem.getData());
                    elem.getData().addEffect(currectCursorEffect);
                    effectHandler.addEffect(currectCursorEffect);
                }
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
        if (!CharacterHelper.isPrintable(key)) {
            return;
        }
        if (currectCursorEffect != null) {
            CharGlyph glyph = new CharGlyph(this, key.charValue(), fontStorage, null);
            NodeListElement<TextPaneElement> node = currectCursorEffect.getGlyph().getTextPaneListElement()
                    .insertBefore(glyph);
            glyph.setTextPaneListElement(node);
        }
        getParent().notifyUpdate(this);
    }

    @Override
    public void onKeyPressed(KeyCode keyCode) {

        if (KeyCode.ENTER == keyCode) {
            if (currectCursorEffect != null) {
                EndOfLine glyph = new EndOfLine(this, fontStorage, null);
                NodeListElement<TextPaneElement> node = currectCursorEffect.getGlyph().getTextPaneListElement()
                        .insertBefore(glyph);
                glyph.setTextPaneListElement(node);
                getParent().notifyUpdate(this);
            }
        }

        if (KeyCode.RIGHT == keyCode) {
            if (currectCursorEffect != null) {
                NodeListElement<TextPaneElement> elem = currectCursorEffect.getGlyph().getTextPaneListElement()
                        .getNext();
                if (elem != null) {
                    effectHandler.removeEffect(currectCursorEffect);
                    currectCursorEffect = new CursorEffect(elem.getData());
                    elem.getData().addEffect(currectCursorEffect);
                    effectHandler.addEffect(currectCursorEffect);
                }
            }
        }

        if (KeyCode.LEFT == keyCode) {
            if (currectCursorEffect != null) {
                NodeListElement<TextPaneElement> elem = currectCursorEffect.getGlyph().getTextPaneListElement()
                        .getPrev();
                if (elem != null) {
                    effectHandler.removeEffect(currectCursorEffect);
                    currectCursorEffect = new CursorEffect(elem.getData());
                    elem.getData().addEffect(currectCursorEffect);
                    effectHandler.addEffect(currectCursorEffect);
                }
            }
        }

        if (KeyCode.DELETE == keyCode) {
            if (currectCursorEffect != null) {
                NodeListElement<TextPaneElement> elem = currectCursorEffect.getGlyph().getTextPaneListElement();
                if (elem != null && !(elem.getData() instanceof EndOfLine)) {
                    effectHandler.removeEffect(currectCursorEffect);
                    if (elem.getNext() != null) {
                        currectCursorEffect = new CursorEffect(elem.getNext().getData());
                        elem.getNext().getData().addEffect(currectCursorEffect);
                    } else if (elem.getPrev() != null) {
                        currectCursorEffect = new CursorEffect(elem.getPrev().getData());
                        elem.getPrev().getData().addEffect(currectCursorEffect);
                    }
                    elem.remove();
                    effectHandler.addEffect(currectCursorEffect);
                    getParent().notifyUpdate(this);
                }
            }
        }

        if (KeyCode.BACK_SPACE == keyCode) {
            if (currectCursorEffect != null) {
                NodeListElement<TextPaneElement> elem = currectCursorEffect.getGlyph().getTextPaneListElement()
                        .getPrev();
                if (elem != null) {
                    elem.remove();
                    getParent().notifyUpdate(this);
                }
            }
        }
    }

    @Override
    public void onKeyReleased(KeyCode keyCode) {
    }
}
