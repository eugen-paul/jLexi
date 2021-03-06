package net.eugenpaul.jlexi.draw;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import net.eugenpaul.jlexi.utils.Vector2d;

public class DrawableStorageImpl implements DrawableStorage {
    private TreeMap<Integer, // z
            TreeMap<Integer, // x
                    TreeMap<Integer, // y
                            List<Drawable>>>> storage;

    public DrawableStorageImpl() {
        storage = new TreeMap<>();
    }

    @Override
    public void add(Drawable drawable, int x, int y, int z) {
        this.storage.computeIfAbsent(z, k -> new TreeMap<>())//
                .computeIfAbsent(x, k -> new TreeMap<>())//
                .computeIfAbsent(y, k -> new LinkedList<>())//
                .add(drawable);
    }

    @Override
    public DrawableStorage copy() {
        DrawableStorageImpl response = new DrawableStorageImpl();

        response.storage = new TreeMap<>();
        for (var zData : this.storage.entrySet()) {
            for (var xData : zData.getValue().entrySet()) {
                for (var yData : xData.getValue().entrySet()) {
                    response.storage.computeIfAbsent(zData.getKey(), k -> new TreeMap<>())//
                            .computeIfAbsent(xData.getKey(), k -> new TreeMap<>())//
                            .computeIfAbsent(yData.getKey(), k -> new LinkedList<>())//
                            .addAll(yData.getValue());
                }
            }
        }

        return response;
    }

    @Override
    public void forEach(BiConsumer<Drawable, Vector2d> consumer) {
        for (var zData : this.storage.entrySet()) {
            for (var xData : zData.getValue().entrySet()) {
                for (var yData : xData.getValue().entrySet()) {
                    for (var drawable : yData.getValue()) {
                        consumer.accept(drawable, new Vector2d(xData.getKey(), yData.getKey()));
                    }
                }
            }
        }
    }

}
