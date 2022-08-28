package net.eugenpaul.jlexi.component.text;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.helper.KeyEventAdapterToKeyPressable;
import net.eugenpaul.jlexi.component.helper.MouseEventAdapterToMouseClickable;
import net.eugenpaul.jlexi.component.interfaces.ChangeListener;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.component.interfaces.TextUpdateable;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPanePanel;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
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
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.window.interfaces.UndoRedoable;

@Slf4j
public class TextPane extends GuiGlyph implements TextUpdateable, ChangeListener, KeyHandlerable, MouseDraggable,
        UndoRedoable, InterfaceModel, ModelPropertyChangeListner {

    private TextPanePanel textPanel;
    private ResourceManager storage;

    private TextPaneDocument document;
    private AbstractKeyHandler keyHandler;

    @Getter
    private Cursor mouseCursor;

    @Getter
    private final String cursorName;

    private Size maxSize = Size.ZERO_SIZE;

    private TextPosition textSelectionFrom;

    public TextPane(String cursorPrefix, Glyph parent, ResourceManager storage, AbstractController controller) {
        super(parent);

        this.document = new TextPaneDocument(//
                storage, //
                this//
        );

        this.cursorName = cursorPrefix + "textPaneCursor";
        this.storage = storage;

        TextCommandsDeque commandDeque = new TextCommandsDeque();

        this.mouseCursor = new Cursor(null, controller, this.cursorName, commandDeque);

        this.textPanel = new TextPanePanel(this);

        this.keyHandler = new TextPaneExtendedKeyHandler(this, storage, commandDeque);

        this.textSelectionFrom = null;

        // TODO move the event-functions to the event-adapter-classes
        // this.mouseEventAdapter = new MouseEventAdapterToMouseClickable(this);
        // this.keyEventAdapter = new KeyEventAdapterToKeyPressable(this);

        resizeTo(Size.ZERO_SIZE);

        controller.addModel(this);
        controller.addView(this);
    }

    @Override
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        textPanel = new TextPanePanel(this);
        textPanel.set(document.getRepresentation(maxSize));

        this.cachedDrawable = new DrawableSketchImpl(Color.WHITE);
        this.cachedDrawable.addDrawable(textPanel.getDrawable(), 0, 0);

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
    public Size getSize() {
        return textPanel.getSize();
    }

    @Override
    public boolean isResizeble() {
        return true;
    }

    @Override
    public void onMouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
        Vector2d relPosToMain = getRelativPositionToMainParent();

        int mouseRelX = mouseX - relPosToMain.getX();
        int mouseRelY = mouseY - relPosToMain.getY();

        LOGGER.trace("MouseDragged on TextPane. Position ({},{}).", mouseRelX, mouseRelY);
        if (this.textSelectionFrom != null) {
            TextPosition textSelectionTo = textPanel.getCursorElementAt(new Vector2d(mouseRelX, mouseRelY));

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

    @Override
    public TextRepresentation getTextRepresentation() {
        return textPanel;
    }

    @Override
    public MouseDraggable onMousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        LOGGER.trace("MousePressed on TextPane. Position ({},{}).", mouseX, mouseY);
        mouseCursor.removeSelection();

        textSelectionFrom = textPanel.getCursorElementAt(new Vector2d(mouseX, mouseY));

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

        var clickedElement = textPanel.getCursorElementAt(new Vector2d(mouseX, mouseY));

        if (clickedElement != null) {
            LOGGER.trace("Document Click on Element: {}.", clickedElement);
            mouseCursor.moveCursorTo(clickedElement);
        } else {
            LOGGER.trace("Document Click on Element: NONE.");
        }
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
    public void onMouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
        // TODO
    }
}
