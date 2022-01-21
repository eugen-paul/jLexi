package net.eugenpaul.jlexi.data.formatting.text;

import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.formatting.Compositor;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.stucture.Row;

/**
 * Add character to rows
 */
public class RowCompositor implements Compositor<Glyph> {

    private Glyph parent;

    public RowCompositor(Glyph parent) {
        this.parent = parent;
    }

    @Override
    public List<Glyph> compose(List<Glyph> data, final int width) {
        List<Glyph> responseList = new LinkedList<>();

        List<Glyph> childrenList = new LinkedList<>();

        int currentWidth = 0;
        for (Glyph glyph : data) {
            int glyphWidth = glyph.getPreferredSize().getWidth();
            if (glyphWidth + currentWidth < width) {
                // add Glyph to current Row
                childrenList.add(glyph);
                currentWidth += glyphWidth;
            } else {
                // save current Row
                Row row = new Row(parent, childrenList);
                responseList.add(row);

                // add Glyph to new Row
                childrenList = new LinkedList<>();
                childrenList.add(glyph);
                currentWidth = glyphWidth;
            }
        }
        Row row = new Row(parent, childrenList);
        responseList.add(row);

        return responseList;
    }

    @Override
    public List<Glyph> compose(GlyphIterator iterator, int width) {
        List<Glyph> responseList = new LinkedList<>();

        List<Glyph> childrenList = new LinkedList<>();

        iterator.first();
        int currentWidth = 0;
        while (iterator.hasNext()) {
            Glyph glyph = iterator.next();
            int glyphWidth = glyph.getPreferredSize().getWidth();
            if (glyphWidth + currentWidth < width) {
                // add Glyph to current Row
                childrenList.add(glyph);
                currentWidth += glyphWidth;
            } else {
                // save current Row
                Row row = new Row(parent, childrenList);
                responseList.add(row);

                // add Glyph to new Row
                childrenList = new LinkedList<>();
                childrenList.add(glyph);
                currentWidth = glyphWidth;
            }
        }
        Row row = new Row(parent, childrenList);
        responseList.add(row);

        return responseList;
    }

}
