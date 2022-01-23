package net.eugenpaul.jlexi.utils.helper;

import net.eugenpaul.jlexi.utils.Size;

/**
 * Helper functions for ImageArrays
 */
public class ImageArrayHelper {
    private ImageArrayHelper() {

    }

    /**
     * Copy rectangle of size copySize from src on position (srcPosX, srxPosY) of size srcSize to dest to position
     * (destPosX, destPosY).
     * 
     * @param src
     * @param srcSize
     * @param srcPosX
     * @param srcPosY
     * @param copySize
     * @param dest
     * @param destSize
     * @param destPosX
     * @param destPosY
     */
    public static void copyRectangle(int[] src, Size srcSize, int srcPosX, int srcPosY, Size copySize, int[] dest,
            Size destSize, int destPosX, int destPosY) {
        if (srcSize.isZero() // src is Empty
                || copySize.isZero() // copyblock is Empty
                || destSize.isZero() // dest is Empty
                || destSize.getWidth() < destPosX // destination Position is out of range
                || destSize.getHight() < destPosY // destination Position is out of range
                || srcPosX < 0 //
                || srcPosY < 0 //
                || destPosX < 0 //
                || destPosY < 0 //
        ) {
            // Out of range or nothing to do
            return;
        }

        int sourcePosition = srcPosX + srcPosY * srcSize.getWidth();
        int targetPosition = destPosX + destPosY * destSize.getWidth();
        int realCopyWidth = Math.min(//
                Math.min(copySize.getWidth(), srcSize.getWidth() - srcPosX), //
                destSize.getWidth() - destPosX//
        );
        int realCopyHight = Math.min(//
                Math.min(copySize.getHight(), srcSize.getHight() - srcPosY), //
                destSize.getHight() - destPosY//
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
