package net.eugenpaul.jlexi.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Size of the Element
 */
@AllArgsConstructor
@Data
public class Size {
    /** width of the element */
    private int width;
    /** hight of the element */
    private int hight;

    public Size(Size a) {
        this.width = a.width;
        this.hight = a.hight;
    }
}
