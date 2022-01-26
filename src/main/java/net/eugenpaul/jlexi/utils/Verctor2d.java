package net.eugenpaul.jlexi.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 2D Point
 */
@AllArgsConstructor
@Data
public class Verctor2d {
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
}
