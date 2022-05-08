package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.representation.TextPaneSite;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextRepresentationToSiteCompositor implements TextCompositor<TextRepresentation> {

    private Size siteSize;
    private int columnPerSite;

    private final int marginLeft;
    private final int marginTop;

    public TextRepresentationToSiteCompositor(Size siteSize, int columnPerSite) {
        this(siteSize, columnPerSite, 20, 40);
    }

    public TextRepresentationToSiteCompositor(Size siteSize, int columnPerSite, int marginLeft, int marginTop) {
        this.siteSize = siteSize;
        this.columnPerSite = columnPerSite;
        this.marginLeft = marginLeft;
        this.marginTop = marginTop;
    }

    @Override
    public List<TextRepresentation> compose(Iterator<TextRepresentation> iterator, Size maxSize) {
        List<TextRepresentation> responseSites = new LinkedList<>();

        TextPaneSite site = new TextPaneSite(null, siteSize);

        int currentColumn = 1;
        while (iterator.hasNext()) {
            var column = iterator.next();

            site.add(column);
            column.setParent(site);

            column.setRelativPosition(new Vector2d(marginLeft * currentColumn, marginTop));

            currentColumn++;

            if (currentColumn > columnPerSite) {
                currentColumn = 1;
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
