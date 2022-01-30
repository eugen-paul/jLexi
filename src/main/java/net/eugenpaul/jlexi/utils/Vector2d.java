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

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append("[x]=");
        response.append(x);
        response.append(" [y]=");
        response.append(y);
        return response.toString();
    }

    public Vector2d subNew(Vector2d subVector){
        return new Vector2d(x - subVector.getX(), y- subVector.getY());
    }
}
