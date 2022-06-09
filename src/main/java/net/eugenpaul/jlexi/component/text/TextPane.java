package net.eugenpaul.jlexi.component.text;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.helper.KeyEventAdapterToKeyPressable;
import net.eugenpaul.jlexi.component.helper.MouseEventAdapterToMouseClickable;
import net.eugenpaul.jlexi.component.interfaces.TextUpdateable;
import net.eugenpaul.jlexi.component.text.format.representation.TextPanePanel;
import net.eugenpaul.jlexi.component.text.format.structure.TextSection;
import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.visitor.Visitor;

public class TextPane extends GuiGlyph implements TextUpdateable {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(TextPane.class);

    private TextPanePanel textPanel;

    public TextPane(String cursorPrefix, Glyph parent, ResourceManager storage, AbstractController controller) {
        super(parent);

        this.textPanel = new TextPanePanel(cursorPrefix, this, storage, controller);

        this.mouseEventAdapter = new MouseEventAdapterToMouseClickable(textPanel);
        this.keyEventAdapter = new KeyEventAdapterToKeyPressable(textPanel);

        resizeTo(Size.ZERO_SIZE);
    }

    public String getCursorName() {
        return textPanel.getCursorName();
    }

    @Override
    public Drawable getDrawable() {
        cachedDrawable = new DrawableSketchImpl(Color.WHITE);
        cachedDrawable.addDrawable(textPanel.getDrawable(), 0, 0);
        return cachedDrawable.draw();
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
    public void setText(List<TextSection> text) {
        textPanel.setText(text);
    }

    @Override
    public void resizeTo(Size size) {
        textPanel.resizeTo(size);
    }

    @Override
    public Size getSize() {
        return textPanel.getSize();
    }

    @Override
    public boolean isResizeble() {
        return textPanel.isResizeble();
    }
}
