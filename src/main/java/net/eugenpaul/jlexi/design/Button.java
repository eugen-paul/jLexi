package net.eugenpaul.jlexi.design;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.formatting.CentralGlypthCompositor;
import net.eugenpaul.jlexi.component.formatting.GlyphCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class Button extends Glyph {

    private Label text;

    private Color backgroundColor;
    private GlyphCompositor<Label> compositor;

    protected Button(Glyph parent, String text, TextFormat format, ResourceManager storage) {
        super(parent);
        this.backgroundColor = format.getBackgroundColor();
        this.text = new Label(this, text, format, storage);
        this.compositor = new CentralGlypthCompositor<>(this.backgroundColor);
    }

    /**
     * Will be call by pressing the Button
     */
    public abstract void press();

    public void setLabel(String text) {
        this.text.setText(text);
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        text.setSize(size);
    }

    public String getLabel() {
        return text.getText();
    }

    public void setTextFormat(TextFormat format) {
        this.text.setTextFormat(format);
    }

    public TextFormat getFormat() {
        return text.getFormat();
    }

    @Override
    public Drawable getPixels() {
        List<Glyph> glyph = compositor.compose(List.of(text).iterator(), size);

        if (glyph.isEmpty()) {
            return DrawableImpl.EMPTY_DRAWABLE;
        }

        return glyph.get(0).getPixels();
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
