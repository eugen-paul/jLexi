package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneSite;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextRepresentationToSiteCompositor implements TextCompositor<TextRepresentation> {

    @Getter
    @Setter
    private Color background;

    private Size siteSize;
    private int columnPerSite;

    private final int columnSpacing;
    private final int paddingLeft;
    private final int paddingTop;

    private final int columnWidth;

    public TextRepresentationToSiteCompositor(Size siteSize, int columnPerSite, int columnWidth, int columnSpacing,
            int paddingLeft, int paddingTop, Color background) {
        this.siteSize = siteSize;
        this.columnPerSite = columnPerSite;
        this.columnSpacing = columnSpacing;
        this.columnWidth = columnWidth;
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.background = background;
    }

    @Override
    public List<TextRepresentation> compose(Iterator<TextRepresentation> iterator, Size maxSize) {
        List<TextRepresentation> responseSites = new LinkedList<>();

        TextPaneSite site = new TextPaneSite(null, siteSize);

        int currentColumn = 0;
        while (iterator.hasNext()) {
            var column = iterator.next();

            site.add(column);

            column.setRelativPosition(new Vector2d(
                    paddingLeft + (currentColumn + columnWidth + columnSpacing) * currentColumn, paddingTop));

            currentColumn++;

            if (currentColumn > columnPerSite - 1) {
                currentColumn = 0;
                responseSites.add(site);
                site = new TextPaneSite(null, siteSize);
            }
        }

        if (!site.isEmpty()) {
            responseSites.add(site);
        }

        return responseSites;
    }

}
