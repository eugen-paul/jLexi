package net.eugenpaul.jlexi.draw;

import lombok.Getter;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.CollisionHelper;
import net.eugenpaul.jlexi.utils.helper.ImageArrayConverter;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class DrawablePixelsImpl implements Drawable {

    public static final Drawable EMPTY = new DrawablePixelsImpl(Size.ZERO_SIZE);

    private int[] argbPixels;
    private int[] rgbaPixels;
    private Color[] colorPixels;
    @Getter
    private Size size;

    private DrawablePixelsImpl() {

    }

    private DrawablePixelsImpl(Size size) {
        this.argbPixels = new int[0];
        this.rgbaPixels = new int[0];
        this.colorPixels = new Color[0];
        this.size = size;
    }

    @Override
    public int[] asArgbPixels() {
        if (this.argbPixels == null) {
            if (this.rgbaPixels != null) {
                this.argbPixels = ImageArrayConverter.rgbaToArgb(this.rgbaPixels);
            } else if (this.colorPixels != null) {
                // TODO
                throw new NotYetImplementedException();
            }
        }
        return this.argbPixels;
    }

    @Override
    public void toArgbPixels(int[] dest, Size destSize, Area drawArea, Vector2d relativePos) {
        int[] pixels = asArgbPixels();

        Vector2d absolutDrawPosition = drawArea.getPosition().addNew(relativePos);

        Area finalDrawArea = CollisionHelper.getOverlapping(//
                drawArea.getPosition(), //
                drawArea.getSize(), //
                absolutDrawPosition, //
                size //
        );

        int xPos = Math.max(0, drawArea.getPosition().getX() - finalDrawArea.getPosition().getX());
        int yPos = Math.max(0, drawArea.getPosition().getY() - finalDrawArea.getPosition().getY());

        ImageArrayHelper.copyRectangle(//
                pixels, //
                size, //
                new Vector2d(xPos, yPos), //
                finalDrawArea.getSize(), //
                dest, //
                destSize, //
                finalDrawArea.getPosition() //
        );
    }

    @Override
    public int[] asRgbaPixels() {
        if (this.rgbaPixels == null) {
            if (this.argbPixels != null) {
                this.rgbaPixels = ImageArrayConverter.argbToRgba(this.argbPixels);
            } else if (this.colorPixels != null) {
                // TODO
                throw new NotYetImplementedException();
            }
        }
        return this.rgbaPixels;
    }

    @Override
    public Color[] asColorPixels() {
        // TODO
        throw new NotYetImplementedException();
    }

    public static DrawableImplArgbBuilder builderArgb() {
        return new DrawableImplArgbBuilder();
    }

    public static DrawableImplRgbaBuilder buildeRgba() {
        return new DrawableImplRgbaBuilder();
    }

    public static DrawableImplColorBuilder builderColor() {
        return new DrawableImplColorBuilder();
    }

    public static class DrawableImplArgbBuilder {
        private DrawableImplArgbBuilder() {
        }

        public DrawableImplSizeBuilder argbPixels(int[] argbPixels) {
            DrawableImplSizeBuilder response = new DrawableImplSizeBuilder();
            response.argbPixels = argbPixels.clone();
            return response;
        }
    }

    public static class DrawableImplRgbaBuilder {
        private DrawableImplRgbaBuilder() {
        }

        public DrawableImplSizeBuilder rgbAPixels(int[] rgbaPixels) {
            DrawableImplSizeBuilder response = new DrawableImplSizeBuilder();
            response.rgbaPixels = rgbaPixels.clone();
            return response;
        }
    }

    public static class DrawableImplColorBuilder {
        private DrawableImplColorBuilder() {
        }

        public DrawableImplSizeBuilder rgbAPixels(Color[] colorPixels) {
            DrawableImplSizeBuilder response = new DrawableImplSizeBuilder();
            response.colorPixels = colorPixels.clone();
            return response;
        }
    }

    public static class DrawableImplSizeBuilder {
        private int[] argbPixels;
        private int[] rgbaPixels;
        private Color[] colorPixels;

        private DrawableImplSizeBuilder() {
            argbPixels = null;
            rgbaPixels = null;
            colorPixels = null;
        }

        public DrawableImplBuilderEnd size(Size size) {
            DrawableImplBuilderEnd response = new DrawableImplBuilderEnd();
            response.argbPixels = this.argbPixels;
            response.rgbaPixels = this.rgbaPixels;
            response.colorPixels = this.colorPixels;
            response.size = size;
            return response;
        }

    }

    public static class DrawableImplBuilderEnd {
        private int[] argbPixels;
        private int[] rgbaPixels;
        private Color[] colorPixels;
        private Size size;

        private DrawableImplBuilderEnd() {
        }

        public DrawablePixelsImpl build() {
            DrawablePixelsImpl response = new DrawablePixelsImpl();
            response.argbPixels = this.argbPixels;
            response.rgbaPixels = this.rgbaPixels;
            response.colorPixels = this.colorPixels;
            response.size = this.size;
            return response;
        }
    }
}
