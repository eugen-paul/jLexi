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

    /**
     * C'tor to copy a Vector
     * 
     * @param vector
     */
    public Vector2d(Vector2d vector) {
        this.x = vector.getX();
        this.y = vector.getY();
    }

    /**
     * Create and return (0,0)-Vector
     * 
     * @return (0,0)-Vector
     */
    public static Vector2d zero() {
        return new Vector2d(0, 0);
    }

    /**
     * Subtract subVector from current Object (Object will be NOT edited) and return new Object
     * 
     * @param subVector
     * @return result
     */
    public Vector2d subNew(Vector2d subVector) {
        return new Vector2d(x - subVector.getX(), y - subVector.getY());
    }

    /**
     * Add addVector to current Object (Object will be NOT edited) and return new Object
     * 
     * @param addVector
     * @return result
     */
    public Vector2d addNew(Vector2d addVector) {
        return new Vector2d(x + addVector.getX(), y + addVector.getY());
    }

    /**
     * Subtract subVector from current Object (Object will be edited) and return current Object
     * 
     * @param subVector
     * @return result
     */
    public Vector2d sub(Vector2d subVector) {
        x -= subVector.getX();
        y -= subVector.getY();
        return this;
    }

    /**
     * Add addVector to current Object (Object will be edited) and return current Object
     * 
     * @param addVector
     * @return result
     */
    public Vector2d add(Vector2d addVector) {
        x += addVector.getX();
        y += addVector.getY();
        return this;
    }

    public Vector2d add(Size addSize) {
        x += addSize.getWidth();
        y += addSize.getHeight();
        return this;
    }

    public Size toSize() {
        return new Size(x, y);
    }

    public Vector2d copy(){
        return new Vector2d(x, y);
    }
}
