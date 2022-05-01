package net.eugenpaul.jlexi.component.label;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextRepresentationToColumnCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.component.text.format.structure.TextParagraph;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2PixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableV2SketchImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.visitor.Visitor;

public class Label extends GuiGlyph {

    @Getter
    private TextFormat textFormat;
    private ResourceManager storage;

    @Getter
    private String text;
    private TextParagraph textElement;

    private TextCompositor<TextRepresentation> compositor;

    public Label(Glyph parent, String text, TextFormat textFormat, ResourceManager storage) {
        super(parent);
        this.textFormat = textFormat;
        this.storage = storage;
        this.text = text;
        this.textElement = new TextParagraph(null, textFormat, storage);
        compositor = new TextRepresentationToColumnCompositor();
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
        cachedDrawableV2 = null;
        getDrawable();
        setSize(cachedDrawableV2.getSize());
    }

    public void setTextFormat(TextFormat format) {
        this.textFormat = storage.getFormats().add(format);
        setText(text);
    }

    @Override
    public DrawableV2 getDrawable() {
        if (cachedDrawableV2 != null) {
            return cachedDrawableV2.draw();
        }

        List<TextRepresentation> rows = textElement.getRows(getSize());

        List<TextRepresentation> column = compositor.compose(rows.iterator(), getSize());

        if (column.isEmpty()) {
            return DrawableV2PixelsImpl.EMPTY;
        }

        cachedDrawableV2 = new DrawableV2SketchImpl(textFormat.getBackgroundColor());
        var drawable = column.get(0).getDrawable();
        cachedDrawableV2.addDrawable(drawable, 0, 0);

        return cachedDrawableV2.draw();
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

}
