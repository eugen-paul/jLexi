package net.eugenpaul.jlexi.utils.helper;

import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public final class CollisionHelper {
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

    public static boolean doOverlap(Vector2d pos1, Size size1, Vector2d pos2, Size size2) {
        // If one rectangle is on left side of other
        if (pos1.getX() >= pos2.getX() + size2.getWidth() //
                || pos2.getX() >= pos1.getX() + size1.getWidth()) {
            return false;
        }

        // If one rectangle is above other
        return !(pos1.getY() + size1.getHeight() <= pos2.getY() //
                || pos2.getY() + size2.getHeight() <= pos1.getY());
    }

    public static Area getOverlapping(Vector2d pos1, Size size1, Vector2d pos2, Size size2) {
        int xPos = Math.max(pos1.getX(), pos2.getX());
        int yPos = Math.max(pos1.getY(), pos2.getY());

        int xSize;
        if (xPos == pos1.getX()) {
            xSize = Math.min(size1.getWidth(), size2.getWidth() - (pos1.getX() - pos2.getX()));
        } else {
            xSize = Math.min(size2.getWidth(), size1.getWidth() - (pos2.getX() - pos1.getX()));
        }

        int ySize;
        if (yPos == pos1.getY()) {
            ySize = Math.min(size1.getHeight(), size2.getHeight() - (pos1.getY() - pos2.getY()));
        } else {
            ySize = Math.min(size2.getHeight(), size1.getHeight() - (pos2.getY() - pos1.getY()));
        }

        if (xSize <= 0 || ySize <= 0) {
            return new Area(Vector2d.zero(), Size.ZERO_SIZE);
        }

        return new Area(new Vector2d(xPos, yPos), new Size(xSize, ySize));
    }
}
