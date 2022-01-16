package net.eugenpaul.jlexi.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DrawableImpl implements Drawable {
    private static final int[] EMPTY_PIXELS = new int[0];
    public static final Drawable EMPTY_DRAWABLE = new DrawableImpl(EMPTY_PIXELS, Size.ZERO_SIZE);

    private int[] pixels;
    private Size pixelSize;
}
