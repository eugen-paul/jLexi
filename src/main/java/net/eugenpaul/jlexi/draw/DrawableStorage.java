package net.eugenpaul.jlexi.draw;

import java.util.function.BiConsumer;

import net.eugenpaul.jlexi.utils.Vector2d;

public interface DrawableStorage {

    void add(Drawable drawable, int x, int y, int z);

    DrawableStorage copy();

    void forEach(BiConsumer<Drawable, Vector2d> consumer);
}
