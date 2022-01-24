package net.eugenpaul.jlexi.component.formatting.text;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.formatting.Compositor;
import net.eugenpaul.jlexi.component.iterator.GlyphIteratorGen;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.component.text.structure.TextRow;

/**
 * Add character to rows
 */
public class RowCompositor implements Compositor<TextPaneElement> {

    private Glyph parent;

    public RowCompositor(Glyph parent) {
        this.parent = parent;
    }

    @Override
    public List<TextPaneElement> compose(List<TextPaneElement> data, final int width) {
        return Collections.emptyList();
    }

    @Override
    public List<TextPaneElement> compose(GlyphIteratorGen<TextPaneElement> iterator, int width) {
        List<TextPaneElement> responseList = new LinkedList<>();

        List<TextPaneElement> childrenList = new LinkedList<>();

        iterator.first();
        int currentWidth = 0;
        while (iterator.hasNext()) {
            TextPaneElement glyph = iterator.next();
            int glyphWidth = glyph.getSize().getWidth();
            if (glyphWidth + currentWidth < width || glyph.isEndOfLine() || glyph.isPlaceHolder()) {
                // add Glyph to current Row
                childrenList.add(glyph);
                currentWidth += glyphWidth;

                // it was a new line
                if (glyph.isEndOfLine() || glyph.isPlaceHolder()) {
                    // save nodes to new Row
                    TextRow<TextPaneElement> row = new TextRow<>(parent, childrenList);
                    responseList.add(row);

                    childrenList = new LinkedList<>();
                    currentWidth = 0;
                }
            } else {
                // save nodes to new Row
                TextRow<TextPaneElement> row = new TextRow<>(parent, childrenList);
                responseList.add(row);

                childrenList = new LinkedList<>();
                childrenList.add(glyph);
                currentWidth = glyphWidth;
            }
        }
        TextRow<TextPaneElement> row = new TextRow<>(parent, childrenList);
        responseList.add(row);

        return responseList;
    }

}
