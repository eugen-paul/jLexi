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
import net.eugenpaul.jlexi.data.formatting.Composition;
import net.eugenpaul.jlexi.data.formatting.text.RowCompositor;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.stucture.CharGlyph;
import net.eugenpaul.jlexi.data.visitor.Visitor;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Collisions;
import net.eugenpaul.jlexi.utils.GlyphNodeList;
import net.eugenpaul.jlexi.utils.ImageArrays;

/**
 * Display Rows.
 */
public class TextPane extends Composition<Glyph> implements GuiComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPane.class);
    private GlyphNodeList nodeList;

    private List<Drawable> childDrawable;
    private List<Glyph> textField;

    private FontStorage fontStorage;

    public TextPane(Glyph parent, FontStorage fontStorage) {
        super(parent);
        setCompositor(new RowCompositor(this));
        this.fontStorage = fontStorage;
        resizeTo(Size.ZERO_SIZE);
        nodeList = new GlyphNodeList();
        childDrawable = Collections.emptyList();
        textField = Collections.emptyList();
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
                if (row instanceof MouseClickable) {
                    MouseClickable g = (MouseClickable) row;
                    g.onMouseClick(//
                            mouseX - row.getRelativPosition().getPosW(), //
                            mouseY - row.getRelativPosition().getPosH(), //
                            button //
                    );
                }
                break;
            }
            i++;
        }

    }

    public void setText(String text) {
        for (int i = 0; i < text.length(); i++) {
            CharGlyph glyph = new CharGlyph(this, text.charAt(i), fontStorage);
            nodeList.addLast(glyph);
        }
    }

    @Override
    public void resizeTo(Size size) {
        setSize(size);
    }
}
