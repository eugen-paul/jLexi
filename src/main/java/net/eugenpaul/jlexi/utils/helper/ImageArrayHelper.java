package net.eugenpaul.jlexi.utils.helper;

import java.util.Arrays;

import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

/**
 * Helper functions for ImageArrays
 */
public final class ImageArrayHelper {

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

    public static void fillRectangle(Color color, int[] dest, Size destSize, Size rectangleSize,
            Vector2d rectangleposition) {
        if (rectangleSize.isZero() // destSize is Empty
                || rectangleposition.getX() + rectangleSize.getWidth() < 0 //
                || rectangleposition.getY() + rectangleSize.getHeight() < 0 //
                || rectangleposition.getX() > destSize.getWidth() //
                || rectangleposition.getY() > destSize.getHeight() //
        ) {
            // Out of range or nothing to do
            return;
        }

        int targetPosition = Math.max(0, rectangleposition.getX() + rectangleposition.getY() * destSize.getWidth());

        int realCopyWidth;
        if (rectangleposition.getX() < 0) {
            realCopyWidth = Math.min(//
                    rectangleSize.getWidth() + rectangleposition.getX(), //
                    destSize.getWidth() //
            );
        } else {
            realCopyWidth = Math.min(//
                    rectangleSize.getWidth(), //
                    destSize.getWidth() - rectangleposition.getX() //
            );
        }

        int realCopyHeight;
        if (rectangleposition.getY() < 0) {
            realCopyHeight = Math.min(//
                    rectangleSize.getHeight() + rectangleposition.getY(), //
                    destSize.getHeight() //
            );
        } else {
            realCopyHeight = Math.min(//
                    rectangleSize.getHeight(), //
                    destSize.getHeight() - rectangleposition.getY() //
            );
        }

        for (int i = 0; i < realCopyHeight; i++) {
            Arrays.fill(//
                    dest, //
                    targetPosition, //
                    targetPosition + realCopyWidth, //
                    color.getArgb()//
            );
            targetPosition += destSize.getWidth();
        }
    }
}
