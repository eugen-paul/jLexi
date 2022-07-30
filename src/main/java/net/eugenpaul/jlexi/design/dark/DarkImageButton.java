package net.eugenpaul.jlexi.design.dark;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.border.Border;
import net.eugenpaul.jlexi.component.border.Border.BorderBuilderComponent;
import net.eugenpaul.jlexi.component.button.ImageButton;
import net.eugenpaul.jlexi.component.panes.ImageGlyph;
import net.eugenpaul.jlexi.utils.Color;

public class DarkImageButton extends ImageButton {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DarkImageButtonBuilder {
        private Glyph parent;
        private ImageGlyph image;
        private BorderBuilderComponent borderBuilderComponent;
        private int borderSize = DarkFactory.BORDER_SIZE;

        public DarkImageButtonBuilder parent(Glyph parent) {
            this.parent = parent;
            return this;
        }

        public DarkImageButtonBuilder image(ImageGlyph image) {
            this.image = image;
            return this;
        }

        public DarkImageButtonBuilder borderSize(int borderSize) {
            this.borderSize = borderSize;
            return this;
        }

        public DarkImageButtonBuilder borderBuilderComponent(BorderBuilderComponent borderBuilderComponent) {
            this.borderBuilderComponent = borderBuilderComponent;
            return this;
        }

        public DarkImageButton build() {
            if (borderBuilderComponent == null) {
                borderBuilderComponent = Border.builder()//
                        .borderColor(DarkFactory.BORDER_COLOR)//
                        .backgroundColor(DarkFactory.BORDER_BACKGROUND_COLOR)//
                        .borderSize(borderSize)//
                        .getBuilderComponent() //
                ;
            }
            return new DarkImageButton(parent, image, borderBuilderComponent);
        }
    }

    private DarkImageButton(Glyph parent, ImageGlyph image, BorderBuilderComponent borderBuilderComponent) {
        super(//
                parent, //
                image, //
                borderBuilderComponent //
        );
    }

    public static DarkImageButtonBuilder builder() {
        return new DarkImageButtonBuilder();
    }

    @Override
    protected Color getBgColorNormal() {
        return DarkFactory.BUTTON_BACKGROUND_COLOR;
    }

    @Override
    protected Color getBgColorFocus() {
        return DarkFactory.BUTTON_BACKGROUND_FOCUS_COLOR;
    }

    @Override
    protected Color getBgColorPush() {
        return DarkFactory.BUTTON_BACKGROUND_PUSH_COLOR;
    }

    @Override
    protected Color getBgColorCheck() {
        return DarkFactory.BUTTON_BACKGROUND_CHECK_COLOR;
    }

}
