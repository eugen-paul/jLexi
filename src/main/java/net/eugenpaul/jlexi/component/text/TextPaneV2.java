package net.eugenpaul.jlexi.component.text;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.appl.subscriber.GlobalSubscribeTypes;
import net.eugenpaul.jlexi.command.TextCommandV2;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.interfaces.ChangeListener;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.component.interfaces.TextUpdateableV2;
import net.eugenpaul.jlexi.component.text.action.TextPaneCursorActionV2;
import net.eugenpaul.jlexi.component.text.converter.TextDataV2;
import net.eugenpaul.jlexi.component.text.format.compositor.HorizontalAlignmentRepresentationCompositorV2;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositorV2;
import net.eugenpaul.jlexi.component.text.format.compositor.TextRepresentationToColumnCompositorV2;
import net.eugenpaul.jlexi.component.text.format.compositor.TextRepresentationToRowCompositorV2;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.MovePosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextPanePageV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextPaneDocumentRoot;
import net.eugenpaul.jlexi.component.text.format.structure.TextPaneDocumentV2;
import net.eugenpaul.jlexi.component.text.keyhandler.AbstractKeyHandlerV2;
import net.eugenpaul.jlexi.component.text.keyhandler.CommandsDeque;
import net.eugenpaul.jlexi.component.text.keyhandler.KeyHandlerableV2;
import net.eugenpaul.jlexi.component.text.keyhandler.TextPaneExtendedKeyHandlerV2;
import net.eugenpaul.jlexi.design.listener.KeyEventAdapter;
import net.eugenpaul.jlexi.design.listener.MouseDragAdapter;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.pubsub.EventManager;
import net.eugenpaul.jlexi.pubsub.EventSubscriber;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.AligmentH;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.window.action.KeyBindingRule;

@Slf4j
public class TextPaneV2 extends GuiGlyph
        implements TextUpdateableV2, ChangeListener, KeyHandlerableV2, EventSubscriber, TextPaneDocumentRoot {

    @Getter
    private TextRepresentationV2 textRepresentation;

    private TextPaneDocumentV2 document;
    private AbstractKeyHandlerV2 keyHandler;

    private List<TextCompositorV2<TextRepresentationV2>> compositors;

    @Getter
    private CursorV2 mouseCursor;

    @Getter
    private final String cursorName;

    private Size maxSize = Size.ZERO;

    private TextPositionV2 textSelectionFrom;
    private Color backgroundColor;

    public TextPaneV2(String cursorPrefix, String name, Glyph parent, ResourceManager storage,
            EventManager eventManager) {
        super(parent);

        this.document = new TextPaneDocumentV2(//
                storage, //
                this//
        );

        this.cursorName = cursorPrefix + "textPaneCursor";

        CommandsDeque<TextPositionV2, TextCommandV2> commandDeque = new CommandsDeque<>();

        this.mouseCursor = new CursorV2(null, eventManager, this.cursorName, commandDeque, this);

        this.keyHandler = new TextPaneExtendedKeyHandlerV2(this, storage, commandDeque);

        this.textSelectionFrom = null;
        this.textRepresentation = null;

        this.name = name;

        this.mouseEventAdapter = new MouseEventAdapterIntern(this);
        this.keyEventAdapter = new KeyEventAdapterIntern(this);
        this.mouseDragAdapter = new MouseDraggedIntern(this);

        this.backgroundColor = Color.INVISIBLE;

        this.compositors = List.of(//
                TextRepresentationToRowCompositorV2.builder().build(), //
                new HorizontalAlignmentRepresentationCompositorV2(backgroundColor, AligmentH.CENTER_POSITIV),
                new TextRepresentationToColumnCompositorV2(Color.INVISIBLE, 0, 0) //
        );

        resizeTo(Size.ZERO);

        eventManager.addSubscriber(this);

        registerDefaultKeyBindings();
    }

    private void registerDefaultKeyBindings() {
        // TODO
        // addDefaultKeyBindings("bold", new TextPaneBoldAction(this.mouseCursor));
        // addDefaultKeyBindings("italic", new TextPaneItalicAction(this.mouseCursor));
        // addDefaultKeyBindings("copy", new TextPaneCopyAction(this.keyHandler));
        // addDefaultKeyBindings("paste", new TextPanePasteAction(this.keyHandler));
        // addDefaultKeyBindings("undo", new TextPaneUndoAction(this.keyHandler));
        // addDefaultKeyBindings("redo", new TextPaneRedoAction(this.keyHandler));

        addDefaultKeyBindings("cursorNext", new TextPaneCursorActionV2(this.keyHandler, MovePosition.NEXT));
        addDefaultKeyBindings("cursorPrevious", new TextPaneCursorActionV2(this.keyHandler, MovePosition.PREVIOUS));
        addDefaultKeyBindings("cursorUp", new TextPaneCursorActionV2(this.keyHandler, MovePosition.UP));
        addDefaultKeyBindings("cursorDown", new TextPaneCursorActionV2(this.keyHandler, MovePosition.DOWN));
        addDefaultKeyBindings("cursorBol", new TextPaneCursorActionV2(this.keyHandler, MovePosition.BENIG_OF_LINE));
        addDefaultKeyBindings("cursorEol", new TextPaneCursorActionV2(this.keyHandler, MovePosition.END_OF_LINE));

        // addDefaultKeyBindings("addNewLine",
        // new TextPaneAddSpecialCharracter(this.keyHandler, SpecialCharacter.NEW_LINE));
        // addDefaultKeyBindings("addSideBreak",
        // new TextPaneAddSpecialCharracter(this.keyHandler, SpecialCharacter.SIDE_BREAK));
    }

    public boolean registerKeyAction(String keys, String actionName) {
        return registerDefaultKeyBindings(actionName, KeyBindingRule.FOCUS_WINDOW, keys);
    }

    @Override
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        var currentIterator = this.document.getRepresentation(this.maxSize).listIterator();
        List<TextRepresentationV2> finalRepresentation = Collections.emptyList();

        for (var compositor : compositors) {
            finalRepresentation = compositor.compose(currentIterator, this.maxSize);
            currentIterator = finalRepresentation.listIterator();
        }

        if (finalRepresentation.isEmpty()) {
            this.textRepresentation = new TextPanePageV2(this, new Size(320, 240));
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
    public void setText(TextDataV2 text) {
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
    public void update(Object source, Object type, Object data) {
        if (type != GlobalSubscribeTypes.TEXT_CURSOR_MOVE || !(data instanceof TextElement)) {
            return;
        }

        var textElement = (TextElement) data;

        var pos = textElement.getRelativPositionTo(this);

        LOGGER.debug("TextPanel get GlobalSubscribeTypes.TEXT_CURSOR_MOVE. " + pos);
    }

    @AllArgsConstructor
    private class MouseEventAdapterIntern implements MouseEventAdapter {
        private TextPaneV2 textpane;

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

        private TextPaneV2 textpane;

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
        private TextPaneV2 textpane;

        @Override
        public void mouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
            // TODO
            // Vector2d relPosToMain = this.textpane.getRelativPositionToMainParent();

            // int mouseRelX = mouseX - relPosToMain.getX();
            // int mouseRelY = mouseY - relPosToMain.getY();

            // LOGGER.trace("MouseDragged on TextPane. Position ({},{}).", mouseRelX, mouseRelY);
            // if (this.textpane.textSelectionFrom != null) {
            // TextPositionV2 textSelectionTo = this.textpane.textRepresentation
            // .getCursorElementAt(new Vector2d(mouseRelX, mouseRelY));

            // if (textSelectionTo != null) {
            // LOGGER.trace("Selection from: {} to: {}", //
            // this.textpane.textSelectionFrom.getTextElement(), //
            // textSelectionTo.getTextElement() //
            // );

            // List<TextElementV2> selectedText = getSelectedText(//
            // textSelectionFrom.getTextElement(), //
            // textSelectionTo.getTextElement() //
            // );

            // if (!selectedText.isEmpty()) {
            // this.textpane.mouseCursor.setTextSelection(selectedText);
            // }

            // this.textpane.mouseCursor.moveCursorTo(textSelectionTo);
            // }
            // }
        }

        private List<TextElement> getSelectedText(TextElement posA, TextElement posB) {
            // TODO
            return Collections.emptyList();
            // Optional<Boolean> aIsFirst = this.textpane.document.isABeforB(posA, posB);

            // if (aIsFirst.isEmpty()) {
            // LOGGER.trace("Empty selection");
            // return Collections.emptyList();
            // }

            // if (aIsFirst.get().booleanValue()) {
            // LOGGER.trace("{} is first", posA);
            // return this.textpane.document.getAllTextElementsBetween(posA.getTextElement(), posB.getTextElement());
            // }

            // LOGGER.trace("{} is first", posB);
            // return this.textpane.document.getAllTextElementsBetween(posB.getTextElement(), posA.getTextElement());
        }
    }

    @Override
    public void redrawDocument() {
        redraw();
    }
}
