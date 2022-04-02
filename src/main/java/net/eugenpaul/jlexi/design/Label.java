package net.eugenpaul.jlexi.design;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.Glyph;
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

public class Label extends Glyph {

    @Getter
    private TextFormat format;
    private ResourceManager storage;

    @Getter
    private String text;
    private TextParagraph textElement;

    private TextCompositor<TextStructureForm> compositor;

    public Label(Glyph parent, String text, TextFormat format, ResourceManager storage) {
        super(parent);
        this.format = format;
        this.storage = storage;
        this.text = text;
        this.textElement = new TextParagraph(null, format, storage);
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
                            format, //
                            TextFormatEffect.DEFAULT_FORMAT_EFFECT//
                    )//
            );
        }
        cachedDrawable = null;
    }

    public void setTextFormat(TextFormat format) {
        this.format = storage.getFormats().add(format);
        setText(text);
    }

    @Override
    public Drawable getPixels() {
        List<TextStructureForm> rows = textElement.getRows(size);

        List<TextStructureForm> column = compositor.compose(rows.iterator(), size);

        if (column.isEmpty()) {
            return DrawableImpl.EMPTY_DRAWABLE;
        }

        Size pixelSize = new Size(column.get(0).getSize().getWidth(), column.get(0).getSize().getHeight());
        int[] pixels = new int[column.get(0).getSize().getWidth() * column.get(0).getSize().getHeight()];

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

}
