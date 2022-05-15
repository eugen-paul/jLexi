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

    private final int paddingLeft;
    private final int paddingTop;

    public TextRepresentationToSiteCompositor(Size siteSize, int columnPerSite, Color background) {
        this(siteSize, columnPerSite, 20, 40, background);
    }

    public TextRepresentationToSiteCompositor(Size siteSize, int columnPerSite, int paddingLeft, int paddingTop,
            Color background) {
        this.siteSize = siteSize;
        this.columnPerSite = columnPerSite;
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.background = background;
    }

    @Override
    public List<TextRepresentation> compose(Iterator<TextRepresentation> iterator, Size maxSize) {
        List<TextRepresentation> responseSites = new LinkedList<>();

        TextPaneSite site = new TextPaneSite(null, siteSize.addNew(0, 10));

        int currentColumn = 1;
        while (iterator.hasNext()) {
            var column = iterator.next();

            site.add(column);
            column.setParent(site);

            column.setRelativPosition(new Vector2d(paddingLeft * currentColumn, paddingTop));

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
