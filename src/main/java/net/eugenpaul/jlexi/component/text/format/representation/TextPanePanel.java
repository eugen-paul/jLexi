package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import lombok.var;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.compositor.HorizontalAlignmentRepresentationCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.AligmentH;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.visitor.Visitor;

@Slf4j
public class TextPanePanel extends TextRepresentationOfRepresentation {

    private TextCompositor<TextRepresentation> compositor;

    private TreeMap<Integer, TextRepresentation> yPositionToSite;

    private Color backgroundColor;

    public TextPanePanel(Glyph parent) {
        super(parent);
        this.backgroundColor = Color.GREY;
        this.compositor = new HorizontalAlignmentRepresentationCompositor(backgroundColor, AligmentH.CENTER_POSITIV);

        this.yPositionToSite = new TreeMap<>();
    }

    public void add(TextRepresentation child) {
        this.children.add(child);
        child.setParent(this);

        this.cachedDrawable = null;
    }

    @Override
    public Drawable getDrawable() {
        if (cachedDrawable != null) {
            return cachedDrawable.draw();
        }

        int maxSiteWidth = 0;
        for (var site : this.children) {
            maxSiteWidth = Math.max(maxSiteWidth, site.getSize().getWidth());
        }

        var centeredSites = compositor.compose(this.children.iterator(), new Size(maxSiteWidth, Integer.MAX_VALUE));

        this.cachedDrawable = new DrawableSketchImpl(backgroundColor);
        this.yPositionToSite.clear();

        int currentY = 0;

        for (var el : centeredSites) {
            this.cachedDrawable.addDrawable(el.getDrawable(), 0, currentY);

            this.yPositionToSite.put(currentY, el);

            el.setRelativPosition(new Vector2d(0, currentY));
            el.setParent(this);

            currentY += el.getSize().getHeight();
        }

        this.setSize(new Size(maxSiteWidth, currentY));

        return this.cachedDrawable.draw();
    }

    public void set(List<TextRepresentation> sites) {
        this.children.clear();

        int maxSiteWidth = 0;

        for (var site : sites) {
            maxSiteWidth = Math.max(maxSiteWidth, site.getSize().getWidth());
        }

        var centeredSites = compositor.compose(sites.iterator(), new Size(maxSiteWidth, Integer.MAX_VALUE));

        this.children.addAll(centeredSites);

        this.cachedDrawable = new DrawableSketchImpl(backgroundColor);

        this.yPositionToSite.clear();

        int currentY = 0;

        for (var el : this.children) {
            this.cachedDrawable.addDrawable(el.getDrawable(), 0, currentY);

            this.yPositionToSite.put(currentY, el);

            el.setRelativPosition(new Vector2d(0, currentY));
            el.setParent(this);

            currentY += el.getSize().getHeight();
        }

        this.setSize(new Size(maxSiteWidth, currentY));
    }

    public Drawable getDrawableOld() {
        if (cachedDrawable != null) {
            return cachedDrawable.draw();
        }

        this.cachedDrawable = new DrawableSketchImpl(this.backgroundColor);

        for (var el : this.children) {
            this.cachedDrawable.addDrawable( //
                    el.getDrawable(), //
                    el.getRelativPosition().getX(), //
                    el.getRelativPosition().getY() //
            );
        }

        return this.cachedDrawable.draw();
    }

    @Override
    public Size getSize() {
        getDrawable();
        return super.getSize();
    }

    @Override
    public Iterator<Glyph> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public void visit(Visitor checker) {
        // Nothing to do
    }

    @Override
    public TextPosition getCursorElementAt(Vector2d pos) {
        var row = this.yPositionToSite.floorEntry(pos.getY());
        if (null == row) {
            return null;
        }

        var clickedElement = row.getValue().getCursorElementAt(//
                new Vector2d(//
                        pos.sub(row.getValue().getRelativPosition())//
                )//
        );
        if (clickedElement != null) {
            LOGGER.trace("Document Click on Element: {}.", clickedElement);
        } else {
            LOGGER.trace("Document Click on Element: NONE.");
        }
        return clickedElement;
    }

    @Override
    protected TextPosition getLastText(int x) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TextPosition getFirstText(int x) {
        // TODO Auto-generated method stub
        return null;
    }

}
