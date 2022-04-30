package net.eugenpaul.jlexi.utils.helper;

import java.util.Arrays;

import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

/**
 * Helper functions for ImageArrays
 */
public final class ImageArrayHelper {

    private static final Vector2d ZERO_VECTOR = Vector2d.zero();

    private ImageArrayHelper() {

    }

    /**
     * Copy rectangle of size copySize from src on position (srcPosition.getX(), srxPosY) of size srcSize to dest to
     * position (destPosition.getX(), destPosition.getY()).
     * 
     * @param src
     * @param srcSize
     * @param srcPosition
     * @param copySize
     * @param dest
     * @param destSize
     * @param destPosition
     */
    public static void copyRectangle(int[] src, Size srcSize, Vector2d srcPosition, Size copySize, int[] dest,
            Size destSize, Vector2d destPosition) {
        if (srcSize.isZero() // src is Empty
                || copySize.isZero() // copyblock is Empty
                || destSize.isZero() // dest is Empty
                || destSize.getWidth() < destPosition.getX() // destination Position is out of range
                || destSize.getHeight() < destPosition.getY() // destination Position is out of range
                || srcPosition.getX() < 0 //
                || srcPosition.getY() < 0 //
                || destPosition.getX() < 0 //
                || destPosition.getY() < 0 //
        ) {
            // Out of range or nothing to do
            return;
        }

        int sourcePosition = srcPosition.getX() + srcPosition.getY() * srcSize.getWidth();
        int targetPosition = destPosition.getX() + destPosition.getY() * destSize.getWidth();
        int realCopyWidth = Math.min(//
                Math.min(copySize.getWidth(), srcSize.getWidth() - srcPosition.getX()), //
                destSize.getWidth() - destPosition.getX()//
        );
        int realCopyHeight = Math.min(//
                Math.min(copySize.getHeight(), srcSize.getHeight() - srcPosition.getY()), //
                destSize.getHeight() - destPosition.getY()//
        );

        for (int i = 0; i < realCopyHeight; i++) {
            System.arraycopy(//
                    src, //
                    sourcePosition, //
                    dest, //
                    targetPosition, //
                    realCopyWidth //
            );
            sourcePosition += srcSize.getWidth();
            targetPosition += destSize.getWidth();
        }
    }

    /**
     * Copy rectangle of size copySize from src on position (srcPosition.getX(), srxPosY) of size srcSize to dest to
     * position (destPosition.getX(), destPosition.getY()).
     * 
     * @param src
     * @param srcPosition
     * @param copySize
     * @param dest
     * @param destPosition
     */
    public static void copyRectangle(Drawable src, Vector2d srcPosition, Size copySize, Drawable dest,
            Vector2d destPosition) {
        copyRectangle(//
                src.getPixels(), //
                src.getPixelSize(), //
                srcPosition, //
                copySize, //
                dest.getPixels(), //
                dest.getPixelSize(), //
                destPosition //
        );
    }

    public static void copyRectangle(Drawable src, Drawable dest, Vector2d destPosition) {
        copyRectangle(//
                src.getPixels(), //
                src.getPixelSize(), //
                ZERO_VECTOR, //
                src.getPixelSize(), //
                dest.getPixels(), //
                dest.getPixelSize(), //
                destPosition //
        );
    }

    public static void copyRectangle(int[] src, Size srcSize, Vector2d srcPosition, Size copySize, Drawable dest,
            Vector2d destPosition) {
        copyRectangle(//
                src, //
                srcSize, //
                srcPosition, //
                copySize, //
                dest.getPixels(), //
                dest.getPixelSize(), //
                destPosition //
        );
    }

    public static void copyRectangle(int[] src, Size srcSize, Drawable dest, Vector2d destPosition) {
        copyRectangle(//
                src, //
                srcSize, //
                ZERO_VECTOR, //
                srcSize, //
                dest.getPixels(), //
                dest.getPixelSize(), //
                destPosition //
        );
    }

    public static void fillRectangle(Color color, Drawable dest, Size size, Vector2d position) {
        if (size.isZero() // destSize is Empty
                || position.getX() + size.getWidth() < 0 //
                || position.getY() + size.getHeight() < 0 //
                || position.getX() > dest.getPixelSize().getWidth() //
                || position.getY() > dest.getPixelSize().getHeight() //
        ) {
            // Out of range or nothing to do
            return;
        }

        int targetPosition = position.getX() + position.getY() * size.getWidth();

        int realCopyWidth;
        if (position.getX() < 0) {
            realCopyWidth = Math.min(//
                    size.getWidth() + position.getX(), //
                    dest.getPixelSize().getWidth() //
            );
        } else {
            realCopyWidth = Math.min(//
                    size.getWidth(), //
                    dest.getPixelSize().getWidth() - position.getX() //
            );
        }

        int realCopyHeight;
        if (position.getY() < 0) {
            realCopyHeight = Math.min(//
                    size.getHeight() + position.getY(), //
                    dest.getPixelSize().getHeight() //
            );
        } else {
            realCopyHeight = Math.min(//
                    size.getHeight(), //
                    dest.getPixelSize().getHeight() - position.getY() //
            );
        }

        for (int i = 0; i < realCopyHeight; i++) {
            Arrays.fill(//
                    dest.getPixels(), //
                    targetPosition, //
                    targetPosition + realCopyWidth, //
                    color.getArgb()//
            );
            targetPosition += dest.getPixelSize().getWidth();
        }
    }

    public static void fillRectangle(Color color, Drawable dest, Area area) {
        fillRectangle(color, dest, area.getSize(), area.getPosition());
    }
}
