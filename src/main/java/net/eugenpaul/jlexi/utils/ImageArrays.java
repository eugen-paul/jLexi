package net.eugenpaul.jlexi.utils;

import net.eugenpaul.jlexi.data.Size;

/**
 * Helper functions for ImageArrays
 */
public class ImageArrays {
    private ImageArrays() {

    }

    /**
     * Copy Rectangle from src to dest
     * 
     * @param src
     * @param srcSize
     * @param dest
     * @param destSize
     * @param destPosX
     * @param destPosY
     */
    public static void copyRectangle(int[] src, Size srcSize, int[] dest, Size destSize, int destPosX, int destPosY) {
        if (srcSize.isZero()) {
            return;
        }

        int sourcePosition = 0;
        int targetPosition = destPosX + destPosY * destSize.getWidth();
        for (int i = 0; i < srcSize.getHight(); i++) {
            System.arraycopy(//
                    src, //
                    sourcePosition, //
                    dest, //
                    targetPosition, //
                    srcSize.getWidth()//
            );
            sourcePosition += srcSize.getWidth();
            targetPosition += destSize.getWidth();
        }
    }
}
