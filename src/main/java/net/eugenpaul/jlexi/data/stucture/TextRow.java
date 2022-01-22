package net.eugenpaul.jlexi.data.stucture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.framing.MouseButton;
import net.eugenpaul.jlexi.data.framing.TextElementClickable;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.DrawableImpl;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.iterator.ListIterator;
import net.eugenpaul.jlexi.data.visitor.Visitor;
import net.eugenpaul.jlexi.utils.ImageArrays;
import net.eugenpaul.jlexi.utils.NodeList.NodeListElement;

public class TextRow extends TextPaneElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextRow.class);

    private List<Glyph> children;

    public TextRow(Glyph parent) {
        super(parent, null);
        children = new ArrayList<>();
    }

    public TextRow(Glyph parent, List<Glyph> children) {
        super(parent, null);
        this.children = children;
        this.children.forEach(v -> v.setParent(this));

        AtomicInteger sizeX = new AtomicInteger();
        AtomicInteger sizeY = new AtomicInteger();

        children.stream().forEach(v -> {
            sizeX.addAndGet(v.getSize().getWidth());
            if (sizeY.get() < v.getSize().getHight()) {
                sizeY.set(v.getSize().getHight());
            }
        });

        setSize(new Size(sizeX.get(), sizeY.get()));
    }

    @Override
    public Drawable getPixels() {
        List<Drawable> childDrawable = children.stream()//
                .map(Glyph::getPixels)//
                .collect(Collectors.toList());

        int width = 0;
        int hight = 0;

        for (Drawable drawable : childDrawable) {
            width += drawable.getPixelSize().getWidth();
            hight = Math.max(hight, drawable.getPixelSize().getHight());
        }

        int[] pixels = new int[width * hight];
        Size pixelsSize = new Size(width, hight);

        int positionX = 0;
        for (Drawable drawable : childDrawable) {
            ImageArrays.copyRectangle(//
                    drawable.getPixels(), //
                    drawable.getPixelSize(), //
                    0, //
                    0, //
                    drawable.getPixelSize(), //
                    pixels, //
                    pixelsSize, //
                    positionX, //
                    hight - drawable.getPixelSize().getHight()//
            );
            positionX += drawable.getPixelSize().getWidth();
        }

        return new DrawableImpl(pixels, pixelsSize);
    }

    @Override
    public GlyphIterator createIterator() {
        return new ListIterator(children);
    }

    @Override
    public void visit(Visitor checker) {
        checker.visit(this);
    }

    @Override
    public NodeListElement<TextPaneElement> onMouseClickTE(Integer mouseX, Integer mouseY, MouseButton button) {
        int x = 0;
        for (Glyph glyph : children) {
            if (x + glyph.getSize().getWidth() > mouseX) {
                if (glyph instanceof TextElementClickable) {
                    TextElementClickable g = (TextElementClickable) glyph;
                    return g.onMouseClickTE(mouseX - x, mouseY, button);
                }
                break;
            }

            x += glyph.getSize().getWidth();
        }
        return null;
    }

    @Override
    public boolean isCursorHoldable() {
        return true;
    }

    @Override
    public void notifyUpdate(Glyph child) {
        LOGGER.trace("row notifyUpdate to parent");
        getParent().notifyUpdate(this);
    }

}
