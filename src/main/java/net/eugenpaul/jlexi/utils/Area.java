package net.eugenpaul.jlexi.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Area {
    private Vector2d position;
    private Size size;

    public Area copy() {
        return new Area(this.position.copy(), this.size);
    }

    /**
     * Check if size of the Area is zero
     * 
     * @return
     */
    public boolean isZero() {
        return size.isZero();
    }
}
