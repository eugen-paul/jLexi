package net.eugenpaul.jlexi.utils.event;

/**
 * Mouse Mapping for internal use.
 */
public enum MouseWheelDirection {
    UP, //
    DOWN, //
    ;

    /**
     * Get Mousebutton from Integer
     * 
     * @param button
     * @return Mousebutton
     */
    public static MouseWheelDirection ofRotation(int rotation) {
        if (rotation > 0) {
            return DOWN;
        } else {
            return UP;
        }
    }
}
