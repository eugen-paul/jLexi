package net.eugenpaul.jlexi.component.button;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.formatting.CentralGlypthCompositor;
import net.eugenpaul.jlexi.component.label.Label;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public abstract class TextButton extends Button {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(TextButton.class);

    private Label text;
    protected CentralGlypthCompositor<Glyph> textCompositor;

    protected TextButton(Glyph parent, String text, TextFormat format, ResourceManager storage) {
        super(parent);
        this.text = new Label(this, text, format, storage);
        this.textCompositor = new CentralGlypthCompositor<>(getBgColorNormal());

        setCompositor(this.textCompositor);
        getElements().add(this.text);
    }

    protected abstract Color getBgColorNormal();

    protected abstract Color getBgColorFocus();

    protected abstract Color getBgColorPush();

    protected abstract Color getBgColorCheck();

    @Override
    public void setState(ButtonState state) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        if (state == ButtonState.NORMAL) {
            textCompositor.setBackgroundColor(getBgColorNormal());
            text.setTextFormat(text.getTextFormat().withBackgroundColor(getBgColorNormal()));
        } else {
            textCompositor.setBackgroundColor(getBgColorPush());
            text.setTextFormat(text.getTextFormat().withBackgroundColor(getBgColorPush()));
        }
    }

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

    public TextFormat getTextFormat() {
        return text.getTextFormat();
    }

}
