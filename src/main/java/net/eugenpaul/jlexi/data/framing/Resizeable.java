package net.eugenpaul.jlexi.data.framing;

import net.eugenpaul.jlexi.data.Size;

public interface Resizeable {

    public void setSize(Size size);

    public default void setSize(int width, int hight) {
        setSize(new Size(width, hight));
    }

}
