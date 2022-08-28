package net.eugenpaul.jlexi.component.text.format.representation;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.ChangeListener;
import net.eugenpaul.jlexi.component.interfaces.GuiEvents;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.component.interfaces.TextUpdateable;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.component.text.format.compositor.HorizontalAlignmentRepresentationCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextPaneDocument;
import net.eugenpaul.jlexi.component.text.format.structure.TextSection;
import net.eugenpaul.jlexi.component.text.keyhandler.AbstractKeyHandler;
import net.eugenpaul.jlexi.component.text.keyhandler.KeyHandlerable;
import net.eugenpaul.jlexi.component.text.keyhandler.TextCommandsDeque;
import net.eugenpaul.jlexi.component.text.keyhandler.TextPaneExtendedKeyHandler;
import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.controller.ModelPropertyChangeListner;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.model.InterfaceModel;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.AligmentH;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.window.interfaces.UndoRedoable;

@Slf4j
public class TextPanePanel extends TextRepresentationOfRepresentation implements ChangeListener, GuiEvents,
        TextUpdateable, KeyHandlerable, MouseDraggable, UndoRedoable, InterfaceModel, ModelPropertyChangeListner {

    private TextPaneDocument document;

    private TextCompositor<TextRepresentation> compositor;

    private TreeMap<Integer, TextRepresentation> yPositionToSite;

    private AbstractKeyHandler keyHandler;

    @Getter
    private Cursor mouseCursor;

    private Color backgroundColor;

    private Size maxSize = Size.ZERO_SIZE;

    private ResourceManager storage;

    private TextPosition textSelectionFrom;

    @Getter
    private final String cursorName;

    public TextPanePanel(String cursorPrefix, Glyph parent, ResourceManager storage, AbstractController controller) {
        super(parent);
        this.backgroundColor = Color.GREY;
        this.compositor = new HorizontalAlignmentRepresentationCompositor(backgroundColor, AligmentH.CENTER_POSITIV);
        this.cursorName = cursorPrefix + "textPaneCursor";

        TextCommandsDeque commandDeque = new TextCommandsDeque();

        this.document = new TextPaneDocument(//
                storage, //
                this//
        );

        this.mouseCursor = new Cursor(null, controller, this.cursorName, commandDeque);

        this.yPositionToSite = new TreeMap<>();

        this.storage = storage;

        this.keyHandler = new TextPaneExtendedKeyHandler(this, storage, commandDeque);
        this.textSelectionFrom = null;

        controller.addModel(this);
        controller.addView(this);
    }

    @Override
    public Drawable getDrawable() {
        if (cachedDrawable != null) {
            return cachedDrawable.draw();
        }

        this.children.clear();

        Size fullAreaSize = new Size(maxSize.getWidth(), Integer.MAX_VALUE);

        var sites = document.getRepresentation(fullAreaSize);

        int maxSiteWidth = 0;

        for (var site : sites) {
            maxSiteWidth = Math.max(maxSiteWidth, site.getSize().getWidth());
        }

        var centeredSites = compositor.compose(sites.iterator(), new Size(maxSiteWidth, Integer.MAX_VALUE));

        this.children.addAll(centeredSites);

        this.cachedDrawable = new DrawableSketchImpl(backgroundColor);

        this.yPositionToSite.clear();

        int currentY = 0;

        for (var el : children) {
            this.cachedDrawable.addDrawable(el.getDrawable(), 0, currentY);

            this.yPositionToSite.put(currentY, el);

            el.setRelativPosition(new Vector2d(0, currentY));
            el.setParent(this);

            currentY += el.getSize().getHeight();
        }

        this.setSize(new Size(maxSiteWidth, currentY));

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
    public void setText(List<TextSection> text) {
        LOGGER.trace("Set Document.text from List<TextSection>");
        document = new TextPaneDocument(text, this, this.storage);
        notifyChange();
    }

    @Override
    public void resizeTo(Size size) {
        notifyChange();
        this.maxSize = size;
        document.notifyChangeDown();
    }

    @Override
    public boolean isResizeble() {
        return true;
    }

    @Override
    public MouseDraggable onMousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("MousePressed on TextPane. Position ({},{}).", mouseX, mouseY);
        mouseCursor.removeSelection();

        var row = this.yPositionToSite.floorEntry(mouseY);
        if (null != row) {
            textSelectionFrom = row.getValue().getCursorElementAt(//
                    new Vector2d(//
                            mouseX - row.getValue().getRelativPosition().getX(), //
                            mouseY - row.getValue().getRelativPosition().getY() //
                    )//
            );
        }

        if (textSelectionFrom != null) {
            LOGGER.trace("MousePressed on TextPane. Position ({},{}). Element {}", mouseX, mouseY,
                    textSelectionFrom.getTextElement());
        } else {
            LOGGER.trace("MousePressed on TextPane. Position ({},{}).", mouseX, mouseY);
        }

        return this;
    }

    @Override
    public MouseDraggable onMouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("MouseReleased on TextPane. Position ({},{}).", mouseX, mouseY);
        textSelectionFrom = null;
        return this;
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("Click on TextPane. Position ({},{}).", mouseX, mouseY);

        mouseCursor.removeSelection();

        var row = this.yPositionToSite.floorEntry(mouseY);
        if (null == row) {
            return;
        }

        TextPosition clickedElement = row.getValue().getCursorElementAt(//
                new Vector2d(//
                        mouseX - row.getValue().getRelativPosition().getX(), //
                        mouseY - row.getValue().getRelativPosition().getY() //
                )//
        );
        if (clickedElement != null) {
            LOGGER.trace("Document Click on Element: {}.", clickedElement);
            mouseCursor.moveCursorTo(clickedElement);
        } else {
            LOGGER.trace("Document Click on Element: NONE.");
        }
    }

    @Override
    public void onMouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
        // TODO
    }

    @Override
    public void onKeyTyped(Character key) {
        LOGGER.trace("Key typed: {}", key);
        this.keyHandler.onKeyTyped(key);
    }

    @Override
    public void onKeyPressed(KeyCode keyCode) {
        LOGGER.trace("Key pressed: {}", keyCode);
        this.keyHandler.onKeyPressed(keyCode);
    }

    @Override
    public void onKeyReleased(KeyCode keyCode) {
        LOGGER.trace("Key released: {}", keyCode);
        this.keyHandler.onKeyReleased(keyCode);
    }

    @Override
    public TextRepresentation getTextRepresentation() {
        return this;
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

    @Override
    public void onMouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
        Vector2d relPosToMain = getRelativPositionToMainParent();

        int mouseRelX = mouseX - relPosToMain.getX();
        int mouseRelY = mouseY - relPosToMain.getY();

        LOGGER.trace("MouseDragged on TextPane. Position ({},{}).", mouseRelX, mouseRelY);
        if (this.textSelectionFrom != null) {
            var row = this.yPositionToSite.floorEntry(mouseRelY);
            if (null != row) {
                TextPosition textSelectionTo = row.getValue().getCursorElementAt(//
                        new Vector2d(//
                                mouseRelX - row.getValue().getRelativPosition().getX(), //
                                mouseRelY - row.getValue().getRelativPosition().getY() //
                        )//
                );

                if (textSelectionTo != null) {
                    LOGGER.trace("Selection from: {} to: {}", //
                            this.textSelectionFrom.getTextElement(), //
                            textSelectionTo.getTextElement() //
                    );

                    List<TextElement> selectedText = getSelectedText(//
                            textSelectionFrom.getTextElement(), //
                            textSelectionTo.getTextElement() //
                    );

                    if (!selectedText.isEmpty()) {
                        mouseCursor.setTextSelection(selectedText);
                    }

                    mouseCursor.moveCursorTo(textSelectionTo);
                }
            }
        }
    }

    private List<TextElement> getSelectedText(TextElement posA, TextElement posB) {
        Optional<Boolean> aIsFirst = document.isABeforB(posA, posB);

        if (aIsFirst.isEmpty()) {
            LOGGER.trace("Empty selection");
            return Collections.emptyList();
        }

        if (aIsFirst.get().booleanValue()) {
            LOGGER.trace("{} is first", posA);
            return document.getAllTextElementsBetween(posA.getTextElement(), posB.getTextElement());
        }

        LOGGER.trace("{} is first", posB);
        return document.getAllTextElementsBetween(posB.getTextElement(), posA.getTextElement());
    }

    @Override
    public void undo(String name) {
        keyHandler.undo();
    }

    @Override
    public void redo(String name) {
        keyHandler.redo();
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (!evt.getSource().equals(this.cursorName)) {
            return;
        }

        ViewPropertyChangeType type = ViewPropertyChangeType.fromValue(evt.getPropertyName());
        if (type != ViewPropertyChangeType.CURSOR_MOVE || !(evt.getNewValue() instanceof TextElement)) {
            return;
        }

        var pos = ((TextElement) evt.getNewValue()).getRelativPositionTo(this);

        LOGGER.debug("TextPanel get ViewPropertyChangeType.CURSOR_MOVE. " + pos);
    }
}
