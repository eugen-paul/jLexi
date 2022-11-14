package net.eugenpaul.jlexi.component.text.keyhandler;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.command.TextCommandV2;
import net.eugenpaul.jlexi.component.text.CursorV2;
import net.eugenpaul.jlexi.component.text.converter.ClipboardConverter;
import net.eugenpaul.jlexi.component.text.converter.clipboard.ClipboardConverterImpl;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactoryV2;
import net.eugenpaul.jlexi.component.text.format.representation.MovePosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

@Slf4j
public class AbstractKeyHandlerV2 {

    private final KeyHandlerableV2 component;
    private final ResourceManager storage;
    private final CommandsDeque<TextPositionV2, TextCommandV2> commandDeque;
    private final ClipboardConverter clipboardConverter;

    protected AbstractKeyHandlerV2(KeyHandlerableV2 component, ResourceManager storage,
            CommandsDeque<TextPositionV2, TextCommandV2> commandDeque) {
        this.component = component;
        this.storage = storage;
        this.commandDeque = commandDeque;
        this.clipboardConverter = new ClipboardConverterImpl(storage);
    }

    public void onKeyTyped(Character key) {
        if (!CharacterHelper.isPrintable(key)) {
            return;
        }

        var cursor = this.component.getMouseCursor();

        TextElementV2 element = TextElementFactoryV2.fromChar(//
                this.storage, //
                key, //
                cursor.getTextFormat(), //
                cursor.getTextFormatEffect()//
        );
        addTextElement(cursor, element);
    }

    public void onKeyPressed(KeyCode keyCode) {
        switch (keyCode) {
        case DELETE:
            keyPressedDelete();
            break;
        case BACK_SPACE:
            keyPressedBackSpace();
            break;
        default:
            break;
        }
    }

    public void addSpecialCharacter(SpecialCharacter sc) {
        var cursor = this.component.getMouseCursor();

        switch (sc) {
        case SIDE_BREAK:
            var sideBreakElement = TextElementFactoryV2.genNewSectionChar(//
                    this.storage, //
                    cursor.getTextFormat(), //
                    cursor.getTextFormatEffect()//
            );
            addTextElement(cursor, sideBreakElement);
            break;
        case NEW_LINE:
            var newLineElement = TextElementFactoryV2.genNewLineChar(//
                    this.storage, //
                    cursor.getTextFormat(), //
                    cursor.getTextFormatEffect()//
            );
            addTextElement(cursor, newLineElement);
            break;
        default:
            break;
        }
    }

    public void moveCursor(MovePosition moving) {
        var cursor = this.component.getMouseCursor();
        var representation = this.component.getTextRepresentation();

        if (null == representation) {
            return;
        }

        var cursorNewPosition = cursor.getPosition().afterMove(moving);

        if (cursorNewPosition != null) {
            this.component.getMouseCursor().removeSelection();
            cursor.moveCursorTo(cursorNewPosition);
        }
    }

    public void undo() {
        var cursorPosition = this.commandDeque.undo();

        var cursor = this.component.getMouseCursor();
        cursor.moveCursorTo(cursorPosition);

        this.component.getTextRepresentation().redraw();
    }

    public void redo() {
        var cursorPosition = this.commandDeque.redo();

        var cursor = this.component.getMouseCursor();
        cursor.moveCursorTo(cursorPosition);

        this.component.getTextRepresentation().redraw();
    }

    public void copy() {
        LOGGER.trace("COPY");
        var cursor = this.component.getMouseCursor();
        //TODO
        LOGGER.trace("COPY - TODO");
        // clipboardConverter.write(cursor.getSelectedText());
    }

    public void paste() {
        LOGGER.trace("PASTE");

        var cursor = this.component.getMouseCursor();
        List<TextElement> textFromClipboard;

        //TODO
        LOGGER.trace("PASTE - TODO");

        // try {
        //     textFromClipboard = clipboardConverter.read(cursor.getTextFormat(), cursor.getTextFormatEffect());
        // } catch (UnsupportedException e) {
        //     LOGGER.error("Can't read data from clipboard. ", e);
        //     return;
        // }

        // cursor.removeSelection();

        // var command = new TextElementAddFormatTextBeforeCommand(textFromClipboard, cursor.getPosition());

        // doTextCommand(command);
    }

    private void doTextCommand(TextCommandV2 command) {
        command.execute();

        if (command.isEmpty()) {
            return;
        }

        this.component.getTextRepresentation().redraw();

        this.commandDeque.addCommand(command);
    }

    public void onKeyReleased(KeyCode keyCode) {
        switch (keyCode) {
        default:
            break;
        }
    }

    private void addTextElement(CursorV2 cursor, TextElementV2 addedElement) {
        TextCommandV2 addCommand;
        //TODO
        // if (cursor.isTextSelected()) {
        //     addCommand = new TextElementReplaceCommand(//
        //             addedElement, //
        //             cursor.getSelectedText());
        //     cursor.removeSelection();
        // } else {
        //     addCommand = new TextElementAddBeforeCommand(//
        //             addedElement, //
        //             cursor.getPosition());
        // }

        // doTextCommand(addCommand);

        // cursor.moveCursorTo(addCommand.getData());
    }

    private void keyPressedBackSpace() {
        var cursor = this.component.getMouseCursor();

        TextCommandV2 deleteCommand;
        //TODO
        // if (cursor.isTextSelected()) {
        //     deleteCommand = new TextElementRemoveSelectedCommand(cursor.getSelectedText());
        //     cursor.removeSelection();
        // } else {
        //     deleteCommand = new TextRemoveBevorCommand(cursor.getPosition());
        // }

        // doTextCommand(deleteCommand);

        // cursor.moveCursorTo(deleteCommand.getData());
    }

    private void keyPressedDelete() {
        var cursor = this.component.getMouseCursor();

        TextCommandV2 deleteCommand;
        //TODO
        // if (cursor.isTextSelected()) {
        //     deleteCommand = new TextElementRemoveSelectedCommand(cursor.getSelectedText());
        //     cursor.removeSelection();
        // } else {
        //     deleteCommand = new TextElementRemoveCommand(cursor.getPosition());
        // }

        // doTextCommand(deleteCommand);

        // cursor.moveCursorTo(deleteCommand.getData());

    }

}
