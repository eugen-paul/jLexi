package net.eugenpaul.jlexi.component.text.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.iterator.GlyphIterator;
import net.eugenpaul.jlexi.component.iterator.ListIteratorGen;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class TextRow<T extends TextPaneElement> extends TextPaneElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextRow.class);

    private List<T> children;

    public TextRow(Glyph parent) {
        super(parent, null);
        children = new ArrayList<>();
    }

    public TextRow(Glyph parent, List<T> children) {
        super(parent, null);
        this.children = children;
        this.children.forEach(v -> v.setParent(this));

        // compute size of the row
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
            ImageArrayHelper.copyRectangle(//
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
        return new ListIteratorGen<>(children);
    }

    @Override
    public void visit(Visitor checker) {
        checker.visit(this);
    }

    @Override
    public NodeListElement<TextPaneElement> onMouseClickTE(Integer mouseX, Integer mouseY, MouseButton button) {
        int x = 0;
        for (TextPaneElement glyph : children) {
            if (x + glyph.getSize().getWidth() > mouseX) {
                if (glyph.isCursorHoldable()) {
                    return glyph.onMouseClickTE(mouseX - x, mouseY, button);
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
