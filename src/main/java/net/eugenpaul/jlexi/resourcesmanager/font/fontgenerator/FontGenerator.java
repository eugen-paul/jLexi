package net.eugenpaul.jlexi.resourcesmanager.font.fontgenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JLabel;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.resourcesmanager.font.FontPixelsGenerator;

public class FontGenerator implements FontPixelsGenerator {

    @Override
    public int[] ofChar(Character c, TextFormat format) {
        return extractedChar(c, format);
    }

    @Override
    public int getMaxAscent(String fontName, int fontSize) {
        Font font = new Font(//
                fontName, //
                Font.PLAIN, //
                fontSize //
        );

        FontMetrics metrics = new JLabel().getFontMetrics(font);
        return metrics.getMaxAscent();
    }

    @Override
    public int getStyle(TextFormat format) {
        return ((format.getBold() != null && format.getBold().booleanValue()) ? Font.BOLD : 0)//
                | ((format.getItalic() != null && format.getItalic().booleanValue()) ? Font.ITALIC : 0)//
        ;
    }

    private int[] extractedChar(Character c, TextFormat format) {
        Font font = new Font(//
                format.getFontName(), //
                getStyle(format), //
                format.getFontsize() //
        );

        // use a JLabel to get a FontMetrics object
        FontMetrics metrics = new JLabel().getFontMetrics(font);
        int width = metrics.stringWidth(c + "");
        int height = metrics.getMaxAscent();

        // use ARGB or the background will be black as well
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

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
