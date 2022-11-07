package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.TreeMap;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@Slf4j
public class TextPanePageV2 extends TextRepresentationOfRepresentationV2 {

    private static final int DEFAULT_MARGIN_TOP = 5;
    private static final int DEFAULT_MARGIN_BOTTOM = 5;
    private static final int DEFAULT_MARGIN_LEFT = 5;
    private static final int DEFAULT_MARGIN_RIGHT = 5;

    private TextRepresentationV2 header;
    private TextRepresentationV2 body;
    private TextRepresentationV2 footer;

    private TreeMap<Integer, TextRepresentationV2> yPositionToElement;

    public TextPanePageV2(Glyph parent, Size size) {
        super(parent);
        setSize(size);
        setMarginTop(DEFAULT_MARGIN_TOP);
        setMarginBottom(DEFAULT_MARGIN_BOTTOM);
        setMarginLeft(DEFAULT_MARGIN_LEFT);
        setMarginRight(DEFAULT_MARGIN_RIGHT);

        this.header = null;
        this.body = null;
        this.footer = null;

        this.yPositionToElement = new TreeMap<>();
    }

    public void setHeader(TextRepresentationV2 header) {
        this.header = header;
        this.header.setParent(this);
        this.header.setFieldType(TextFieldType.HEADER);
        this.cachedDrawable = null;
        initChildren();
    }

    public void setBody(TextRepresentationV2 body) {
        this.body = body;
        this.body.setParent(this);
        this.body.setFieldType(TextFieldType.BODY);
        this.cachedDrawable = null;
        initChildren();
    }

    public void setFooter(TextRepresentationV2 footer) {
        this.footer = footer;
        this.footer.setParent(this);
        this.footer.setFieldType(TextFieldType.FOOTER);
        this.cachedDrawable = null;
        initChildren();
    }

    private void initChildren() {
        this.children.clear();
        if (this.header != null) {
            this.children.add(this.header);
        }
        if (this.body != null) {
            this.children.add(this.body);
        }
        if (this.footer != null) {
            this.children.add(this.footer);
        }
    }

    @Override
    public TextPositionV2 getCursorElementAt(Vector2d pos) {
        var row = this.yPositionToElement.floorEntry(pos.getY());
        if (null == row) {
            return null;
        }

        var clickedElement = row.getValue().getCursorElementAt(//
                new Vector2d(//
                        pos.sub(row.getValue().getRelativPosition())//
                )//
        );
        if (clickedElement != null) {
            LOGGER.trace("Page Click on {}: {}.", clickedElement);
        } else {
            LOGGER.trace("Page Click on Element: NONE.");
        }
        return clickedElement;
    }

    @Override
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        this.cachedDrawable = new DrawableSketchImpl(Color.WHITE, getSize());
        this.yPositionToElement.clear();

        if (this.header != null) {
            this.cachedDrawable.addDrawable(//
                    header.getDrawable(), //
                    header.getRelativPosition().getX(), //
                    header.getRelativPosition().getY() //
            );
            this.yPositionToElement.put(header.getRelativPosition().getY(), header);
        }

        for (var el : children) {
            this.cachedDrawable.addDrawable(//
                    el.getDrawable(), //
                    el.getRelativPosition().getX(), //
                    el.getRelativPosition().getY() //
            );

            this.yPositionToElement.put(el.getRelativPosition().getY(), el);
        }

        if (this.footer != null) {
            this.cachedDrawable.addDrawable(//
                    footer.getDrawable(), //
                    footer.getRelativPosition().getX(), //
                    footer.getRelativPosition().getY() //
            );
            this.yPositionToElement.put(footer.getRelativPosition().getY(), footer);
        }

        return this.cachedDrawable.draw();
    }

    @Override
    protected TextPositionV2 getLastText(int x) {
        var col = this.yPositionToElement.lastEntry();
        if (col == null) {
            return null;
        }

        var pos = col.getValue().getRelativPositionTo(this);

        return col.getValue().getLastText(x - pos.getX());
    }

    @Override
    protected TextPositionV2 getFirstText(int x) {
        var col = this.yPositionToElement.firstEntry();
        if (col == null) {
            return null;
        }

        var pos = col.getValue().getRelativPositionTo(this);

        return col.getValue().getFirstText(x - pos.getX());
    }

}
