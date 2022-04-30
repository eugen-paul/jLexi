package net.eugenpaul.jlexi.utils.helper;

/**
 * Converter functions for ImageArrays
 */
public final class ImageArrayConverter {

    private ImageArrayConverter() {

    }

    public static int[] argbToRgba(int[] argbData) {
        int[] rgbaArray = argbData.clone();

        for (int i = 0; i < rgbaArray.length; i++) {
            rgbaArray[i] = rgbaArray[i] << 8 | rgbaArray[i] >>> 24;
        }

        return rgbaArray;
    }

    public static int[] rgbaToArgb(int[] rgbaArray) {
        int[] argbData = rgbaArray.clone();

        for (int i = 0; i < argbData.length; i++) {
            argbData[i] = argbData[i] >>> 8 | argbData[i] << 24;
        }

        return argbData;
    }

}
