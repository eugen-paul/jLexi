package net.eugenpaul.jlexi.component.scrollpane;

import java.nio.file.Paths;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.formatting.HorizontalAlignmentGlypthCompositor;
import net.eugenpaul.jlexi.component.formatting.VerticalAlignmentGlypthCompositor;
import net.eugenpaul.jlexi.component.panes.ImageGlyph;
import net.eugenpaul.jlexi.design.dark.DarkImageButton;
import net.eugenpaul.jlexi.design.dark.DarkImageButton.DarkImageButtonBuilder;
import net.eugenpaul.jlexi.draw.DrawableImageImpl;
import net.eugenpaul.jlexi.utils.AligmentH;
import net.eugenpaul.jlexi.utils.AligmentV;

public abstract class ScrollbarImage extends Scrollbar {

    protected ScrollbarImage(Glyph parent, ScrollbarType type, String arrowFirstPath, String arrowLastPath,
            String backgroundPath, String runnerPath) {
        super(parent);
        this.type = type;

        if (this.type == ScrollbarType.VERTICAL) {
            this.compositor = new HorizontalAlignmentGlypthCompositor<>(//
                    getScrollbarColor(), //
                    AligmentH.LEFT //
            );
        } else {
            this.compositor = new VerticalAlignmentGlypthCompositor<>(//
                    getScrollbarColor(), //
                    AligmentV.TOP //
            );
        }

        DarkImageButtonBuilder buttonBuilder = DarkImageButton.builder()//
                .parent(this.parent)//
                .borderSize(0) //
        ;

        this.buttonFirst = buttonBuilder //
                .image(new ImageGlyph(null, Paths.get(arrowFirstPath))) //
                .build();
        this.buttonLast = buttonBuilder //
                .image(new ImageGlyph(null, Paths.get(arrowLastPath))) //
                .build();

        this.buttonFirst.resizeTo(getBarWidth(), getBarWidth());
        this.buttonLast.resizeTo(getBarWidth(), getBarWidth());

        this.backgroundGlyph = ImageGlyph.builder()//
                .parent(null)//
                .imagePath(Paths.get(backgroundPath))//
                .imageBuilder(DrawableImageImpl.builder())//
                .build()//
        ;

        this.runnerGlyph = ImageGlyph.builder()//
                .parent(null)//
                .imagePath(Paths.get(runnerPath))//
                .imageBuilder(DrawableImageImpl.builder())//
                .build()//
        ;
    }

}
