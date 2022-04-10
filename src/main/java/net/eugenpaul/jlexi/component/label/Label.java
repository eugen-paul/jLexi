package net.eugenpaul.jlexi.component.label;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextStructureFormToColumnCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.component.text.format.structure.TextParagraph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;
import net.eugenpaul.jlexi.visitor.Visitor;

public class Label extends GuiGlyph {

    @Getter
    private TextFormat textFormat;
    private ResourceManager storage;

    @Getter
    private String text;
    private TextParagraph textElement;

    private TextCompositor<TextStructureForm> compositor;

    public Label(Glyph parent, String text, TextFormat textFormat, ResourceManager storage) {
        super(parent);
        this.textFormat = textFormat;
        this.storage = storage;
        this.text = text;
        this.textElement = new TextParagraph(null, textFormat, storage);
        compositor = new TextStructureFormToColumnCompositor();
        setText(text);
    }

    public void setText(String text) {
        this.text = text;

        textElement.clear();
        for (char c : text.toCharArray()) {
            textElement.add(//
                    TextElementFactory.fromChar(//
                            null, //
                            storage, //
                            null, //
                            c, //
                            textFormat, //
                            TextFormatEffect.DEFAULT_FORMAT_EFFECT//
                    )//
            );
        }
        setSize(Size.ZERO_MAX);
        cachedDrawable = null;
        getPixels();
        setSize(cachedDrawable.getPixelSize());
    }

    public void setTextFormat(TextFormat format) {
        this.textFormat = storage.getFormats().add(format);
        setText(text);
    }

    @Override
    public Drawable getPixels() {
        if (cachedDrawable != null) {
            return cachedDrawable;
        }

        List<TextStructureForm> rows = textElement.getRows(size);

        List<TextStructureForm> column = compositor.compose(rows.iterator(), size);

        if (column.isEmpty()) {
            return DrawableImpl.EMPTY_DRAWABLE;
        }

        Size pixelSize = column.get(0).getSize();
        int[] pixels = new int[(int) pixelSize.compArea()];

        cachedDrawable = new DrawableImpl(pixels, pixelSize);

        ImageArrayHelper.copyRectangle(column.get(0).getPixels(), cachedDrawable, Vector2d.zero());

        return cachedDrawable;
    }

    @Override
    public Iterator<Glyph> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isResizeble() {
        return false;
    }

    @Override
    public void resizeTo(Size size) {
        // nothing to do
    }

    @Override
    public Glyph getGlyph() {
        return this;
    }
}
