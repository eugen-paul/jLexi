package net.eugenpaul.jlexi.draw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.eugenpaul.jlexi.utils.Size;

@AllArgsConstructor
@Getter
public class DrawableImpl implements Drawable {
    private static final int[] EMPTY_PIXELS = new int[0];
    public static final Drawable EMPTY_DRAWABLE = new DrawableImpl(EMPTY_PIXELS, Size.ZERO_SIZE);

    private int[] pixels;
    private Size pixelSize;

    @Override
    public Drawable copy() {
        return new DrawableImpl(pixels.clone(), pixelSize);
    }
}
