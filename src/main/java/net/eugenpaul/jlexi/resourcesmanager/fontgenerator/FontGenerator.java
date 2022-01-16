package net.eugenpaul.jlexi.resourcesmanager.fontgenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JLabel;

public class FontGenerator implements FontPixelsGenerator {

    @Override
    public int[] ofChar(char c, String font, int style, int size) {
        return extractedChar(c, font, style, size);
    }

    @Override
    public int getMaxAscent(String fontName, int style, int size) {
        Font font = new Font(fontName, style, size);
        FontMetrics metrics = new JLabel().getFontMetrics(font);
        return metrics.getMaxAscent();
    }

    private static int[] extractedChar(char c, String fontName, int style, int size) {
        Font font = new Font(fontName, style, size);

        // use a JLabel to get a FontMetrics object
        FontMetrics metrics = new JLabel().getFontMetrics(font);
        int width = metrics.stringWidth(c + "");
        int height = metrics.getMaxAscent();

        // use ARGB or the background will be black as well
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // create a Graphics2D object from the BufferedImage
        Graphics2D g2d = bi.createGraphics();
        g2d.setFont(font);
        g2d.setColor(Color.black);
        g2d.drawString(c + "", 0, height - metrics.getDescent() + 1);
        g2d.dispose();

        return convertBufferedImageToPixelArray(bi);
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

}
