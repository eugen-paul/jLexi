package net.eugenpaul.jlexi.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Immutable size of the Element. width or hight cann't be negative. Negative value will be sets to 0;
 */
@Getter
@EqualsAndHashCode
public class Size {

    public static final Size ZERO_SIZE = new Size(0, 0);

    /** width of the element */
    private int width;
    /** hight of the element */
    private int hight;

    public Size(int width, int hight) {
        this.width = Math.max(0, width);
        this.hight = Math.max(0, hight);
    }

    public Size(Size a) {
        this(a.width, a.hight);
    }

    /**
     * 
     * @return true if width or hight is 0
     */
    public boolean isZero() {
        return 0 == width || 0 == hight;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append("[width]=");
        response.append(width);
        response.append(" [hight]=");
        response.append(hight);
        return response.toString();
    }
}
