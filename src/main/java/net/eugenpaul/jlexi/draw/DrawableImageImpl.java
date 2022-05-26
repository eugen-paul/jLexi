package net.eugenpaul.jlexi.draw;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import lombok.NonNull;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.ColorType;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class DrawableImageImpl implements Drawable {

    private DrawablePixelsImpl pixelsImpl;

    private DrawableImageImpl() {
    }

    @Override
    public Size getSize() {
        return this.pixelsImpl.getSize();
    }

    @Override
    public int[] asArgbPixels() {
        return this.pixelsImpl.asArgbPixels();
    }

    @Override
    public void toArgbPixels(int[] dest, Size destSize, Area drawArea, Vector2d relativePos) {
        this.pixelsImpl.toArgbPixels(dest, destSize, drawArea, relativePos);
    }

    @Override
    public int[] asRgbaPixels() {
        return this.pixelsImpl.asRgbaPixels();
    }

    @Override
    public void toRgbaPixels(int[] dest, Size destSize, Area drawArea, Vector2d relativePos) {
        this.pixelsImpl.toRgbaPixels(dest, destSize, drawArea, relativePos);
    }

    @Override
    public void toPixels(ColorType colorType, int[] dest, Size destSize, Area drawArea, Vector2d relativePos) {
        this.pixelsImpl.toPixels(colorType, dest, destSize, drawArea, relativePos);
    }

    @Override
    public Color[] asColorPixels() {
        // TODO
        throw new NotYetImplementedException();
    }

    public static DrawableImageImplBuilder builder() {
        return new DrawableImageImplBuilder();
    }

    public static class DrawableImageImplBuilder {
        private DrawableImageImplBuilder() {
        }

        public DrawableImageImplBufferBuilder fromPath(@NonNull Path path) throws IOException {
            DrawableImageImplBufferBuilder response = new DrawableImageImplBufferBuilder();
            response.image = ImageIO.read(path.toFile());
            response.originSize = new Size(//
                    response.image.getWidth(), //
                    response.image.getHeight()//
            );
            return response;
        }

    }

    public static class DrawableImageImplBufferBuilder {
        private BufferedImage image;
        private Size originSize;

        private DrawableImageImplBufferBuilder() {
        }

        public DrawableImplBuilderEnd size(@NonNull Size size) {
            DrawableImplBuilderEnd responseBuilder = new DrawableImplBuilderEnd();

            if (size.getHeight() > 0 && size.getWidth() > 0) {
                BufferedImage newResizedImage = new BufferedImage(size.getWidth(), size.getHeight(), image.getType());

                Graphics2D g = newResizedImage.createGraphics();

                // background transparent
                g.setComposite(AlphaComposite.Src);
                g.fillRect(0, 0, size.getWidth(), size.getHeight());

                // configure RenderingHints
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // puts the original image into the newResizedImage
                g.drawImage(image, 0, 0, size.getWidth(), size.getHeight(), null);

                responseBuilder.argbPixels = convertBufferedImageToPixelArray(newResizedImage);
            } else {
                responseBuilder.argbPixels = new int[0];
            }

            responseBuilder.size = size;
            return responseBuilder;
        }

        private static int[] convertBufferedImageToPixelArray(BufferedImage image) {
            final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            final int width = image.getWidth();
            final int height = image.getHeight();
            final boolean hasAlphaChannel = image.getAlphaRaster() != null;

            int[] result = new int[height * width];
            if (hasAlphaChannel) {
                final int pixelLength = 4;
                for (int pixel = 0, pos = 0; pixel + 3 < pixels.length; pixel += pixelLength, pos++) {
                    int argb = 0;
                    argb += ((pixels[pixel] & 0xff) << 24); // alpha
                    argb += (pixels[pixel + 1] & 0xff); // blue
                    argb += ((pixels[pixel + 2] & 0xff) << 8); // green
                    argb += ((pixels[pixel + 3] & 0xff) << 16); // red
                    result[pos] = argb;
                }
            } else {
                final int pixelLength = 3;
                for (int pixel = 0, pos = 0; pixel + 2 < pixels.length; pixel += pixelLength, pos++) {
                    int argb = 0;
                    argb += -16777216; // 255 alpha
                    argb += (pixels[pixel] & 0xff); // blue
                    argb += ((pixels[pixel + 1] & 0xff) << 8); // green
                    argb += ((pixels[pixel + 2] & 0xff) << 16); // red
                    result[pos] = argb;
                }
            }

            return result;
        }

        public DrawablePixelsImpl build() {
            return DrawablePixelsImpl.builderArgb()//
                    .argbPixels(convertBufferedImageToPixelArray(image))//
                    .size(originSize)//
                    .build();
        }

    }

    public static class DrawableImplBuilderEnd {
        private int[] argbPixels;
        private Size size;

        private DrawableImplBuilderEnd() {
        }

        public DrawablePixelsImpl build() {
            return DrawablePixelsImpl.builderArgb()//
                    .argbPixels(argbPixels)//
                    .size(size)//
                    .build();
        }
    }

}
