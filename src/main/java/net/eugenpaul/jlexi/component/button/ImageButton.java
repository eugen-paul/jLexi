package net.eugenpaul.jlexi.component.button;

import java.nio.file.Path;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.border.Border;
import net.eugenpaul.jlexi.component.border.Border.BorderBuilderComponent;
import net.eugenpaul.jlexi.component.formatting.CentralGlypthCompositor;
import net.eugenpaul.jlexi.component.panes.ImageGlyph;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public abstract class ImageButton extends Button {

    private ImageGlyph image;
    private Border border;

    protected ImageButton(Glyph parent, ImageGlyph image, BorderBuilderComponent borderBuilder) {
        super(parent);

        this.border = borderBuilder.component(image).build();
        image.setParent(border);
        border.setParent(this);

        this.border.setBackgroundColor(getBgColorNormal());

        var textCompositor = new CentralGlypthCompositor<>(getBgColorNormal());
        setCompositor(textCompositor);
        setElement(this.border);
    }

    protected abstract Color getBgColorNormal();

    protected abstract Color getBgColorFocus();

    protected abstract Color getBgColorPush();

    protected abstract Color getBgColorCheck();

    public void setImage(Path imagePath) {
        image.setImagePath(imagePath);
    }

    public void setBorderSize(int borderSize) {
        border.setBorderSize(borderSize);
    }

    @Override
    public void setState(ButtonState state) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        if (state == ButtonState.NORMAL) {
            getCompositor().setBackgroundColor(getBgColorNormal());
            border.setBackgroundColor(getBgColorNormal());
        } else {
            getCompositor().setBackgroundColor(getBgColorPush());
            border.setBackgroundColor(getBgColorPush());
        }
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        border.resizeTo(size);
    }

}
