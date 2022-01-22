package net.eugenpaul.jlexi.utils;

import net.eugenpaul.jlexi.data.Position;
import net.eugenpaul.jlexi.data.Size;

public class Collisions {
    private Collisions() {
    }

    public static boolean isPointOnArea(Position point, Position areaPosiion, Size areaSize) {
        return (//
        point.getPosW() >= areaPosiion.getPosW() //
                && point.getPosH() >= areaPosiion.getPosH() //
                && point.getPosW() <= areaPosiion.getPosW() + areaSize.getWidth() //
                && point.getPosH() <= areaPosiion.getPosH() + areaSize.getHight() //
        );

    }
}
