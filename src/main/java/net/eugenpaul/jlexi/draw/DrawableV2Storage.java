package net.eugenpaul.jlexi.draw;

import java.util.function.BiConsumer;

import net.eugenpaul.jlexi.utils.Vector2d;

public interface DrawableV2Storage {

    void add(DrawableV2 drawable, int x, int y, int z);

    DrawableV2Storage copy();

    void forEach(BiConsumer<DrawableV2, Vector2d> consumer);
}
