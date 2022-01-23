package net.eugenpaul.jlexi.data.formatting.text;

import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.formatting.Compositor;
import net.eugenpaul.jlexi.data.iterator.GlyphIterator;
import net.eugenpaul.jlexi.data.stucture.EndOfLine;
import net.eugenpaul.jlexi.data.stucture.TextRow;

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
        return null;
        // List<Glyph> responseList = new LinkedList<>();

        // List<Glyph> childrenList = new LinkedList<>();

        // int currentWidth = 0;
        // for (Glyph glyph : data) {
        // int glyphWidth = glyph.getSize().getWidth();
        // if (glyphWidth + currentWidth < width || (glyph instanceof EndOfLine)) {
        // // add Glyph to current Row
        // childrenList.add(glyph);
        // currentWidth += glyphWidth;

        // // it was a new line
        // if ((glyph instanceof EndOfLine)) {
        // // end current Row
        // TextRow row = new TextRow(parent, childrenList);
        // responseList.add(row);
        // childrenList = new LinkedList<>();
        // currentWidth = 0;
        // }
        // } else {
        // // save current Row
        // TextRow row = new TextRow(parent, childrenList);
        // responseList.add(row);

        // // add Glyph to new Row
        // childrenList = new LinkedList<>();
        // childrenList.add(glyph);
        // currentWidth = glyphWidth;
        // }
        // }
        // TextRow row = new TextRow(parent, childrenList);
        // responseList.add(row);

        // return responseList;
    }

    @Override
    public List<Glyph> compose(GlyphIterator iterator, int width) {
        List<Glyph> responseList = new LinkedList<>();

        List<Glyph> childrenList = new LinkedList<>();

        iterator.first();
        int currentWidth = 0;
        while (iterator.hasNext()) {
            Glyph glyph = iterator.next();
            int glyphWidth = glyph.getSize().getWidth();
            if (glyphWidth + currentWidth < width || (glyph instanceof EndOfLine)) {
                // add Glyph to current Row
                childrenList.add(glyph);
                currentWidth += glyphWidth;

                // it was a new line
                if ((glyph instanceof EndOfLine)) {
                    // save nodes to new Row
                    TextRow row = new TextRow(parent, childrenList);
                    responseList.add(row);

                    childrenList = new LinkedList<>();
                    currentWidth = 0;
                }
            } else {
                // save nodes to new Row
                TextRow row = new TextRow(parent, childrenList);
                responseList.add(row);

                childrenList = new LinkedList<>();
                childrenList.add(glyph);
                currentWidth = glyphWidth;
            }
        }
        TextRow row = new TextRow(parent, childrenList);
        responseList.add(row);

        return responseList;
    }

}
