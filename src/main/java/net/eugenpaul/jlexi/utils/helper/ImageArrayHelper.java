package net.eugenpaul.jlexi.utils.helper;

import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Verctor2d;

/**
 * Helper functions for ImageArrays
 */
public class ImageArrayHelper {
    private ImageArrayHelper() {

    }

    /**
     * Copy rectangle of size copySize from src on position (srcPosition.getX(), srxPosY) of size srcSize to dest to position
     * (destPosition.getX(), destPosition.getY()).
     * 
     * @param src
     * @param srcSize
     * @param srcPosition.getX()
     * @param srcPosition.getY()
     * @param copySize
     * @param dest
     * @param destSize
     * @param destPosition.getX()
     * @param destPosition.getY()
     */
    public static void copyRectangle(int[] src, Size srcSize, Verctor2d srcPosition, Size copySize, int[] dest,
            Size destSize, Verctor2d destPosition) {
        if (srcSize.isZero() // src is Empty
                || copySize.isZero() // copyblock is Empty
                || destSize.isZero() // dest is Empty
                || destSize.getWidth() < destPosition.getX() // destination Position is out of range
                || destSize.getHight() < destPosition.getY() // destination Position is out of range
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
        int realCopyHight = Math.min(//
                Math.min(copySize.getHight(), srcSize.getHight() - srcPosition.getY()), //
                destSize.getHight() - destPosition.getY()//
        );

        for (int i = 0; i < realCopyHight; i++) {
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
}
