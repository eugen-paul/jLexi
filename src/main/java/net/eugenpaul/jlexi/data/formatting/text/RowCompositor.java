package net.eugenpaul.jlexi.data.formatting.text;

import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.formatting.Composition;
import net.eugenpaul.jlexi.data.formatting.Compositor;
import net.eugenpaul.jlexi.data.stucture.Row;

/**
 * Add character to rows
 */
public class RowCompositor implements Compositor<Glyph> {

    private Composition<Glyph> composition;

    public RowCompositor() {
        composition = null;
    }

    @Override
    public void setComposition(Composition<Glyph> composition) {
        this.composition = composition;
    }

    @Override
    public List<Glyph> compose(List<Glyph> data, final int width) {
        List<Glyph> responseList = new LinkedList<>();
        Row row = new Row();
        int currentWidth = 0;
        int position = 0;
        for (Glyph glyph : data) {
            int glyphWidth = glyph.getSize().getWidth();
            if (glyphWidth + currentWidth < width) {
                // add Glyph to current Row
                row.insert(glyph, position);
                currentWidth += glyphWidth;
                position++;
            } else {
                // save currwnt Row
                responseList.add(row);
                // add Glyph to new Row
                row = new Row();
                row.insert(glyph, 0);
                currentWidth = glyphWidth;
                position = 1;
            }
        }
        responseList.add(row);

        return responseList;
    }

}
