package net.eugenpaul.jlexi.design;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.component.text.format.structure.TextParagraph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class Button extends Glyph {

    private static final String EMPTY = "";

    @Getter
    private TextFormat format;
    private ResourceManager storage;

    @Getter
    private String label;
    private TextParagraph textElement;
    protected Color backgroundColor;

    protected Button(Glyph parent, Color backgroundColor, TextFormat format, ResourceManager storage) {
        super(parent);
        this.backgroundColor = backgroundColor;
        this.label = EMPTY;
        this.format = format;
        this.storage = storage;
        this.textElement = new TextParagraph(null, format, storage);
    }

    /**
     * Will be call by pressing the Button
     */
    public abstract void press();

    public void setLabel(String label) {
        this.label = label;

        textElement.clear();
        for (char c : label.toCharArray()) {
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
    }

    public void setTextFormat(TextFormat format) {
        storage.getFormats().add(format);
        this.format = format;
        setLabel(label);
    }

    @Override
    public Drawable getPixels() {
        List<TextStructureForm> rows = textElement.getRows(size);

        Size pixelSize = new Size(size.getWidth(), size.getHeight());
        int[] pixels = new int[size.getWidth() * size.getHeight()];
        Arrays.fill(pixels, backgroundColor.getARGB());

        cachedDrawable = new DrawableImpl(pixels, pixelSize);

        Vector2d position = new Vector2d(0, 0);
        for (var el : rows) {
            ImageArrayHelper.copyRectangle(el.getPixels(), cachedDrawable, position);
            var yPosition = position.getY();

            el.setRelativPosition(new Vector2d(0, yPosition));
            el.setParent(this);

            position.setY(yPosition + el.getSize().getHeight());
        }

        return cachedDrawable;
    }

    @Override
    public Iterator<Glyph> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub

    }
}
