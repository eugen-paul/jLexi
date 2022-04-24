package net.eugenpaul.jlexi.draw;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;
import lombok.var;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class DrawableV2AreasImpl implements DrawableV2 {

    private TreeMap<Integer, // z
            TreeMap<Integer, // x
                    TreeMap<Integer, // y
                            List<DrawableV2>>>> data;
    @Getter
    @Setter
    private Color background;

    private Area area;

    public DrawableV2AreasImpl(//
            Map<Integer, Map<Integer, Map<Integer, List<DrawableV2>>>> data, //
            Area area //
    ) {
        this.area = area.copy();

        this.data = new TreeMap<>();
        for (var zData : data.entrySet()) {
            for (var xData : zData.getValue().entrySet()) {
                for (var yData : xData.getValue().entrySet()) {
                    this.data.computeIfAbsent(zData.getKey(), k -> new TreeMap<>())//
                            .computeIfAbsent(xData.getKey(), k -> new TreeMap<>())//
                            .computeIfAbsent(yData.getKey(), k -> new LinkedList<>())//
                            .addAll(yData.getValue());
                }
            }
        }
    }

    @Override
    public int[] asArgbPixels() {
        int[] response = new int[(int) this.area.getSize().compArea()];
        for (var zData : data.entrySet()) {
            for (var xData : zData.getValue().entrySet()) {
                for (var yData : xData.getValue().entrySet()) {
                    for (var drawable : yData.getValue()) {
                        ImageArrayHelper.copyRectangle(//
                                drawable.asArgbPixels(), //
                                drawable.getSize(), //
                                Vector2d.zero(), //
                                drawable.getSize(), //
                                response, //
                                this.area.getSize(), //
                                new Vector2d(xData.getKey(), yData.getKey()) //
                        );
                    }
                }
            }
        }
        return response;
    }

    @Override
    public int[] asRgbaPixels() {
        int[] response = new int[(int) this.area.getSize().compArea()];
        for (var zData : data.entrySet()) {
            for (var xData : zData.getValue().entrySet()) {
                for (var yData : xData.getValue().entrySet()) {
                    for (var drawable : yData.getValue()) {
                        ImageArrayHelper.copyRectangle(//
                                drawable.asRgbaPixels(), //
                                drawable.getSize(), //
                                Vector2d.zero(), //
                                drawable.getSize(), //
                                response, //
                                this.area.getSize(), //
                                new Vector2d(xData.getKey(), yData.getKey()) //
                        );
                    }
                }
            }
        }
        return response;
    }

    @Override
    public Color[] asColorPixels() {
        // TODO
        return null;
    }

    @Override
    public Size getSize() {
        return area.getSize();
    }
}
