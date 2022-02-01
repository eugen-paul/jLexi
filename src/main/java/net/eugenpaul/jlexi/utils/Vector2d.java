package net.eugenpaul.jlexi.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 2D Point
 */
@AllArgsConstructor
@Data
public class Vector2d {
    private int x;
    private int y;

    public Vector2d(Vector2d vector) {
        this.x = vector.getX();
        this.y = vector.getY();
    }

    public static Vector2d zero() {
        return new Vector2d(0, 0);
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append("[x]=");
        response.append(x);
        response.append(" [y]=");
        response.append(y);
        return response.toString();
    }

    public Vector2d subNew(Vector2d subVector) {
        return new Vector2d(x - subVector.getX(), y - subVector.getY());
    }

    public Vector2d addNew(Vector2d addVector) {
        return new Vector2d(x + addVector.getX(), y + addVector.getY());
    }

    public Vector2d add(Vector2d addVector) {
        x += addVector.getX();
        y += addVector.getY();
        return this;
    }
}
