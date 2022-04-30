package net.eugenpaul.jlexi.draw;

import lombok.Getter;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayConverter;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class DrawableV2PixelsImpl implements DrawableV2 {

    public static final DrawableV2 EMPTY = new DrawableV2PixelsImpl(Size.ZERO_SIZE);

    private int[] argbPixels;
    private int[] rgbaPixels;
    private Color[] colorPixels;
    @Getter
    private Size size;

    private DrawableV2PixelsImpl() {

    }

    private DrawableV2PixelsImpl(Size size) {
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
    public void toArgbPixels(int[] dest, Size destSize, Vector2d position) {
        int[] pixels = asArgbPixels();
        ImageArrayHelper.copyRectangle(pixels, size, Vector2d.zero(), size, dest, destSize, position);
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

    public static DrawableV2ImplArgbBuilder builderArgb() {
        return new DrawableV2ImplArgbBuilder();
    }

    public static DrawableV2ImplRgbaBuilder buildeRgba() {
        return new DrawableV2ImplRgbaBuilder();
    }

    public static DrawableV2ImplColorBuilder builderColor() {
        return new DrawableV2ImplColorBuilder();
    }

    public static class DrawableV2ImplArgbBuilder {
        private DrawableV2ImplArgbBuilder() {
        }

        public DrawableV2ImplSizeBuilder argbPixels(int[] argbPixels) {
            DrawableV2ImplSizeBuilder response = new DrawableV2ImplSizeBuilder();
            response.argbPixels = argbPixels.clone();
            return response;
        }
    }

    public static class DrawableV2ImplRgbaBuilder {
        private DrawableV2ImplRgbaBuilder() {
        }

        public DrawableV2ImplSizeBuilder rgbAPixels(int[] rgbaPixels) {
            DrawableV2ImplSizeBuilder response = new DrawableV2ImplSizeBuilder();
            response.rgbaPixels = rgbaPixels.clone();
            return response;
        }
    }

    public static class DrawableV2ImplColorBuilder {
        private DrawableV2ImplColorBuilder() {
        }

        public DrawableV2ImplSizeBuilder rgbAPixels(Color[] colorPixels) {
            DrawableV2ImplSizeBuilder response = new DrawableV2ImplSizeBuilder();
            response.colorPixels = colorPixels.clone();
            return response;
        }
    }

    public static class DrawableV2ImplSizeBuilder {
        private int[] argbPixels;
        private int[] rgbaPixels;
        private Color[] colorPixels;

        private DrawableV2ImplSizeBuilder() {
            argbPixels = null;
            rgbaPixels = null;
            colorPixels = null;
        }

        public DrawableV2ImplBuilderEnd size(Size size) {
            DrawableV2ImplBuilderEnd response = new DrawableV2ImplBuilderEnd();
            response.argbPixels = this.argbPixels;
            response.rgbaPixels = this.rgbaPixels;
            response.colorPixels = this.colorPixels;
            response.size = size;
            return response;
        }

    }

    public static class DrawableV2ImplBuilderEnd {
        private int[] argbPixels;
        private int[] rgbaPixels;
        private Color[] colorPixels;
        private Size size;

        private DrawableV2ImplBuilderEnd() {
        }

        public DrawableV2PixelsImpl build() {
            DrawableV2PixelsImpl response = new DrawableV2PixelsImpl();
            response.argbPixels = this.argbPixels;
            response.rgbaPixels = this.rgbaPixels;
            response.colorPixels = this.colorPixels;
            response.size = this.size;
            return response;
        }
    }
}
