package net.eugenpaul.jlexi.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Top left (p1) and bottom right (p2) points of a rectangle
 */
@AllArgsConstructor
@Data
public class Bounds {
    private int p1X;
    private int p1Y;
    private int p2X;
    private int p2Y;

    public Bounds(Bounds a) {
        p1X = a.p1X;
        p2X = a.p2X;
        p1Y = a.p1Y;
        p2Y = a.p2Y;
    }
}
