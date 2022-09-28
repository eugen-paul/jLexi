package net.eugenpaul.jlexi.component.text;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.appl.interfaces.CopyPasteable;
import net.eugenpaul.jlexi.appl.interfaces.UndoRedoable;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.interfaces.ChangeListener;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.component.interfaces.TextUpdateable;
import net.eugenpaul.jlexi.component.text.format.compositor.HorizontalAlignmentRepresentationCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextRepresentationToColumnCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextRepresentationToRowCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneSite;
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
import net.eugenpaul.jlexi.design.listener.KeyEventAdapter;
import net.eugenpaul.jlexi.design.listener.MouseDragAdapter;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
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
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.window.action.KeyBindingAction;
import net.eugenpaul.jlexi.window.action.KeyBindingRule;

@Slf4j
public class TextPane extends GuiGlyph implements TextUpdateable, ChangeListener, KeyHandlerable, UndoRedoable,
        CopyPasteable, InterfaceModel, ModelPropertyChangeListner {

    @Getter
    private TextRepresentation textRepresentation;

    private TextPaneDocument document;
    private AbstractKeyHandler keyHandler;

    private List<TextCompositor<TextRepresentation>> compositors;

    @Getter
    private Cursor mouseCursor;

    @Getter
    private final String cursorName;

    private Size maxSize = Size.ZERO_SIZE;

    private TextPosition textSelectionFrom;
    private Color backgroundColor;

    public TextPane(String cursorPrefix, Glyph parent, ResourceManager storage, AbstractController controller) {
        super(parent);

        this.document = new TextPaneDocument(//
                storage, //
                this//
        );

        this.cursorName = cursorPrefix + "textPaneCursor";

        TextCommandsDeque commandDeque = new TextCommandsDeque();

        this.mouseCursor = new Cursor(null, controller, this.cursorName, commandDeque);

        this.keyHandler = new TextPaneExtendedKeyHandler(this, storage, commandDeque);

        this.textSelectionFrom = null;
        this.textRepresentation = null;

        this.mouseEventAdapter = new MouseEventAdapterIntern(this);
        this.keyEventAdapter = new KeyEventAdapterIntern(this);
        this.mouseDragAdapter = new MouseDraggedIntern(this);

        this.backgroundColor = Color.INVISIBLE;

        this.compositors = List.of(//
                TextRepresentationToRowCompositor.builder().build(), //
                new HorizontalAlignmentRepresentationCompositor(backgroundColor, AligmentH.CENTER_POSITIV),
                new TextRepresentationToColumnCompositor(Color.INVISIBLE, 0, 0) //
        );

        resizeTo(Size.ZERO_SIZE);

        controller.addModel(this);
        controller.addViewChangeListner(this);

        registerTestKeyBindings();
    }

    private void registerTestKeyBindings() {
        getKeyBindingMap().addAction("ctrl pressed C", KeyBindingRule.FOCUS_WINDOW, new KeyBindingAction() {
            @Override
            public void doAction() {
                keyHandler.copy();
            }
        });
        getKeyBindingMap().addAction("ctrl pressed V", KeyBindingRule.FOCUS_WINDOW, new KeyBindingAction() {
            @Override
            public void doAction() {
                keyHandler.paste();
            }
        });
    }

    @Override
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        var currentIterator = this.document.getRepresentation(this.maxSize).iterator();
        List<TextRepresentation> finalRepresentation = Collections.emptyList();

        for (var compositor : compositors) {
            finalRepresentation = compositor.compose(currentIterator, this.maxSize);
            currentIterator = finalRepresentation.iterator();
        }

        if (finalRepresentation.isEmpty()) {
            this.textRepresentation = new TextPaneSite(this, new Size(320, 240));
        } else {
            this.textRepresentation = finalRepresentation.get(0);
            this.textRepresentation.setParent(this);
        }

        this.cachedDrawable = new DrawableSketchImpl(Color.INVISIBLE);
        this.cachedDrawable.addDrawable(this.textRepresentation.getDrawable(), 0, 0);

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
        this.document.setText(text);
        notifyChange();
    }

    @Override
    public void resizeTo(Size size) {
        notifyChange();
        this.maxSize = size;
        this.document.notifyChangeDown();
    }

    @Override
    public Size getSize() {
        getDrawable();
        return this.textRepresentation.getSize();
    }

    @Override
    public boolean isResizeble() {
        return true;
    }

    @Override
    public void undo(String name) {
        this.keyHandler.undo();
    }

    @Override
    public void redo(String name) {
        this.keyHandler.redo();
    }

    @Override
    public void copy(String name) {
        this.keyHandler.copy();
    }

    @Override
    public void paste(String name) {
        this.keyHandler.paste();
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

    @AllArgsConstructor
    private class MouseEventAdapterIntern implements MouseEventAdapter {
        private TextPane textpane;

        @Override
        public MouseDraggable mousePressed(Integer mouseX, Integer mouseY, MouseButton button) {

            LOGGER.trace("MousePressed on TextPane. Position ({},{}).", mouseX, mouseY);
            this.textpane.mouseCursor.removeSelection();

            this.textpane.textSelectionFrom = this.textpane.textRepresentation
                    .getCursorElementAt(new Vector2d(mouseX, mouseY));

            if (this.textpane.textSelectionFrom != null) {
                LOGGER.trace("MousePressed on TextPane. Position ({},{}). Element {}", mouseX, mouseY,
                        this.textpane.textSelectionFrom.getTextElement());
            } else {
                LOGGER.trace("MousePressed on TextPane. Position ({},{}).", mouseX, mouseY);
            }

            return this.textpane;
        }

        @Override
        public MouseDraggable mouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
            LOGGER.trace("MouseReleased on TextPane. Position ({},{}).", mouseX, mouseY);
            this.textpane.textSelectionFrom = null;
            return this.textpane;
        }

        @Override
        public void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button) {
            LOGGER.trace("Click on TextPane. Position ({},{}).", mouseX, mouseY);

            this.textpane.mouseCursor.removeSelection();

            var clickedElement = this.textpane.textRepresentation.getCursorElementAt(new Vector2d(mouseX, mouseY));

            if (clickedElement != null) {
                LOGGER.trace("Document Click on Element: {}.", clickedElement);
                this.textpane.mouseCursor.moveCursorTo(clickedElement);
            } else {
                LOGGER.trace("Document Click on Element: NONE.");
            }
        }
    }

    @AllArgsConstructor
    private class KeyEventAdapterIntern implements KeyEventAdapter {

        private TextPane textpane;

        @Override
        public void keyTyped(Character key) {
            LOGGER.trace("Key typed: {}", key);
            this.textpane.keyHandler.onKeyTyped(key);
        }

        @Override
        public void keyPressed(KeyCode keyCode) {
            LOGGER.trace("Key pressed: {}", keyCode);
            this.textpane.keyHandler.onKeyPressed(keyCode);
        }

        @Override
        public void keyReleased(KeyCode keyCode) {
            LOGGER.trace("Key released: {}", keyCode);
            this.textpane.keyHandler.onKeyReleased(keyCode);
        }
    }

    @AllArgsConstructor
    private class MouseDraggedIntern implements MouseDragAdapter {
        private TextPane textpane;

        @Override
        public void mouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
            Vector2d relPosToMain = this.textpane.getRelativPositionToMainParent();

            int mouseRelX = mouseX - relPosToMain.getX();
            int mouseRelY = mouseY - relPosToMain.getY();

            LOGGER.trace("MouseDragged on TextPane. Position ({},{}).", mouseRelX, mouseRelY);
            if (this.textpane.textSelectionFrom != null) {
                TextPosition textSelectionTo = this.textpane.textRepresentation
                        .getCursorElementAt(new Vector2d(mouseRelX, mouseRelY));

                if (textSelectionTo != null) {
                    LOGGER.trace("Selection from: {} to: {}", //
                            this.textpane.textSelectionFrom.getTextElement(), //
                            textSelectionTo.getTextElement() //
                    );

                    List<TextElement> selectedText = getSelectedText(//
                            textSelectionFrom.getTextElement(), //
                            textSelectionTo.getTextElement() //
                    );

                    if (!selectedText.isEmpty()) {
                        this.textpane.mouseCursor.setTextSelection(selectedText);
                    }

                    this.textpane.mouseCursor.moveCursorTo(textSelectionTo);
                }
            }
        }

        private List<TextElement> getSelectedText(TextElement posA, TextElement posB) {
            Optional<Boolean> aIsFirst = this.textpane.document.isABeforB(posA, posB);

            if (aIsFirst.isEmpty()) {
                LOGGER.trace("Empty selection");
                return Collections.emptyList();
            }

            if (aIsFirst.get().booleanValue()) {
                LOGGER.trace("{} is first", posA);
                return this.textpane.document.getAllTextElementsBetween(posA.getTextElement(), posB.getTextElement());
            }

            LOGGER.trace("{} is first", posB);
            return this.textpane.document.getAllTextElementsBetween(posB.getTextElement(), posA.getTextElement());
        }
    }
}
