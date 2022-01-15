package net.eugenpaul.jlexi.data.framing;

import net.eugenpaul.jlexi.data.Size;

public interface Resizeable {

    public default void setSize(Size size) {
        setSize(size.getWidth(), size.getHight());
    }

    public void setSize(int width, int hight);

}
