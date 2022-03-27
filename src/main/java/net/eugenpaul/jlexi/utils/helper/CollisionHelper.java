package net.eugenpaul.jlexi.utils.helper;

import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class CollisionHelper {
    private CollisionHelper() {
    }

    public static boolean isPointOnArea(Vector2d point, Vector2d areaPosiion, Size areaSize) {
        return isPointOnArea(point.getX(), point.getY(), areaPosiion, areaSize);
    }

    public static boolean isPointOnArea(int pointX, int pointY, Vector2d areaPosiion, Size areaSize) {
        return (//
        pointX >= areaPosiion.getX() //
                && pointY >= areaPosiion.getY() //
                && pointX < areaPosiion.getX() + areaSize.getWidth() //
                && pointY < areaPosiion.getY() + areaSize.getHeight() //
        );
    }
}
