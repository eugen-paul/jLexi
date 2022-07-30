package net.eugenpaul.jlexi.component.button;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.border.Border;
import net.eugenpaul.jlexi.component.border.Border.BorderBuilderComponent;
import net.eugenpaul.jlexi.component.formatting.CentralGlypthCompositor;
import net.eugenpaul.jlexi.component.label.Label;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public abstract class TextButton extends Button {

    private Label label;
    private Border border;

    protected TextButton(Glyph parent, BorderBuilderComponent borderBuilderComponent, String text, TextFormat format,
            ResourceManager storage) {
        super(parent);
        this.label = new Label(this, text, format, storage);

        this.border = borderBuilderComponent//
                .parent(this)//
                .component(this.label)//
                .build();
        this.border.setBackgroundColor(getBgColorNormal());

        var textCompositor = new CentralGlypthCompositor<>(getBgColorNormal());
        setCompositor(textCompositor);
        setElement(this.border);
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
            getCompositor().setBackgroundColor(getBgColorNormal());
            label.setTextFormat(label.getTextFormat().withBackgroundColor(getBgColorNormal()));
            border.setBackgroundColor(getBgColorNormal());
        } else {
            getCompositor().setBackgroundColor(getBgColorPush());
            label.setTextFormat(label.getTextFormat().withBackgroundColor(getBgColorPush()));
            border.setBackgroundColor(getBgColorPush());
        }
    }

    public void setLabel(String text) {
        this.label.setText(text);
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        border.resizeTo(size);
    }

    public String getLabel() {
        return label.getText();
    }

    public void setTextFormat(TextFormat format) {
        this.label.setTextFormat(format);
    }

    public TextFormat getTextFormat() {
        return label.getTextFormat();
    }

}
