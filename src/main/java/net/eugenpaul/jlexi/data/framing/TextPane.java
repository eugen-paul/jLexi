package net.eugenpaul.jlexi.data.framing;

import java.util.List;
import java.util.stream.Collectors;

import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.DrawableImpl;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.design.GuiComponent;
import net.eugenpaul.jlexi.data.formatting.Composition;
import net.eugenpaul.jlexi.data.formatting.text.RowCompositor;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.stucture.CharGlyph;
import net.eugenpaul.jlexi.data.visitor.Visitor;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.GlyphNodeList;
import net.eugenpaul.jlexi.utils.ImageArrays;

/**
 * Display Rows.
 */
public class TextPane extends Composition<Glyph> implements GuiComponent {

    private GlyphNodeList nodeList;

    private FontStorage fontStorage;

    public TextPane(Glyph parent, FontStorage fontStorage) {
        super(parent);
        setCompositor(new RowCompositor(this));
        this.fontStorage = fontStorage;
        setPreferredSize(Size.ZERO_SIZE);
        nodeList = new GlyphNodeList();
    }

    @Override
    public Drawable getPixels() {
        List<Glyph> textField = compositor.compose(nodeList.iterator(), getPreferredSize().getWidth());

        List<Drawable> childDrawable = textField.stream()//
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
    public void setSize(Size size) {
        setPreferredSize(size);
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO Auto-generated method stub
    }

    public void setText(String text) {
        for (int i = 0; i < text.length(); i++) {
            CharGlyph glyph = new CharGlyph(this, text.charAt(i), fontStorage);
            nodeList.addLast(glyph);
        }
    }
}
