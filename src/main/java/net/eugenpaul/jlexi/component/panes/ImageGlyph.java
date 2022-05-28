package net.eugenpaul.jlexi.component.panes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Builder;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.draw.DrawableImageImpl.DrawableImageImplBuilder;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.visitor.Visitor;

public class ImageGlyph extends GuiGlyph {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageGlyph.class);

    protected DrawableImageImplBuilder imageBuilder;
    protected Path imagePath;
    protected Drawable imageDrawable;

    @Builder
    protected ImageGlyph(Glyph parent, DrawableImageImplBuilder imageBuilder, Path imagePath) {
        super(parent);
        this.imageBuilder = imageBuilder;
        this.imagePath = imagePath;
        try {
            this.imageDrawable = imageBuilder.fromPath(imagePath).build();
            setSize(imageDrawable.getSize());
        } catch (IOException e) {
            this.imageDrawable = null;
            setSize(Size.ZERO_SIZE);
            LOGGER.error("Cann't load image.", e);
        }
    }

    @Override
    public boolean isResizeble() {
        return true;
    }

    public void setImagePath(Path imagePath) {
        this.imagePath = imagePath;
        initImage(imagePath);
    }

    private void initImage(Path imagePath) {
        try {
            this.imageDrawable = imageBuilder.fromPath(imagePath).size(getSize()).build();
        } catch (IOException e) {
            this.imageDrawable = null;
            LOGGER.error("Cann't load image.", e);
        }
    }

    @Override
    public void resizeTo(Size size) {
        cachedDrawable = null;
        setSize(size);
        initImage(imagePath);
    }

    @Override
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        this.cachedDrawable = new DrawableSketchImpl(Color.INVISIBLE);

        if (imageDrawable != null) {
            this.cachedDrawable.addDrawable(imageDrawable, 0, 0, 0);
        }

        return this.cachedDrawable.draw();
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
