package net.eugenpaul.jlexi.utils.helper;

import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Verctor2d;

public class CollisionHelper {
    private CollisionHelper() {
    }

    public static boolean isPointOnArea(Verctor2d point, Verctor2d areaPosiion, Size areaSize) {
        return (//
        point.getX() >= areaPosiion.getX() //
                && point.getY() >= areaPosiion.getY() //
                && point.getX() < areaPosiion.getX() + areaSize.getWidth() //
                && point.getY() < areaPosiion.getY() + areaSize.getHight() //
        );

    }
}
