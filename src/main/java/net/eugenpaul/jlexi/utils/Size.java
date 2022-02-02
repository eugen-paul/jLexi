package net.eugenpaul.jlexi.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Immutable size of the Element. width or height cann't be negative. Negative value will be sets to 0;
 */
@Getter
@EqualsAndHashCode
public class Size {

    public static final Size ZERO_SIZE = new Size(0, 0);
    public static final Size ZERO_MAX = new Size(Integer.MAX_VALUE, Integer.MAX_VALUE);

    /** width of the element */
    private int width;
    /** height of the element */
    private int height;

    public Size(int width, int height) {
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
    }

    public Size(Size a) {
        this(a.width, a.height);
    }

    /**
     * 
     * @return true if width or height is 0
     */
    public boolean isZero() {
        return 0 == width || 0 == height;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append("[width]=");
        response.append(width);
        response.append(" [height]=");
        response.append(height);
        return response.toString();
    }
}
