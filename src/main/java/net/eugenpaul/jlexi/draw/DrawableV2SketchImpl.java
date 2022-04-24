package net.eugenpaul.jlexi.draw;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public class DrawableV2SketchImpl implements DrawableV2Sketch {

    private TreeMap<Integer, // z
            TreeMap<Integer, // x
                    TreeMap<Integer, // y
                            List<DrawableV2>>>> data;
    @Getter
    @Setter
    private Color background;

    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public DrawableV2SketchImpl() {
        data = new TreeMap<>();
        minX = 0;
        maxX = 0;
        minY = 0;
        maxY = 0;
    }

    @Override
    public void addDrawable(DrawableV2 drawable, int x, int y, int z) {
        this.data.computeIfAbsent(z, k -> new TreeMap<>())//
                .computeIfAbsent(x, k -> new TreeMap<>())//
                .computeIfAbsent(y, k -> new LinkedList<>())//
                .add(drawable);
    }

    @Override
    public DrawableV2 draw() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DrawableV2 draw(Area area) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DrawableV2Sketch sketchAt(Area area) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DrawableV2Sketch copy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Size getSize() {
        return new Size(maxX - minX, maxY - minY);
    }

}
