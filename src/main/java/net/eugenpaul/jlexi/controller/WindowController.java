package net.eugenpaul.jlexi.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;
import net.eugenpaul.jlexi.window.Window;
import reactor.core.publisher.Mono;

/**
 * Implementation of {@link AbstractController} for GUI-/Windows-events (like Mouseclick, key type, window resize, ...)
 */
public class WindowController extends AbstractController {

    private Map<String, Window> windowsMap;

    public WindowController(ExecutorService pool) {
        super(pool);
        this.windowsMap = new HashMap<>();
    }

    public void addWindow(Window glyph, String name) {
        this.windowsMap.put(name, glyph);
    }

    public Mono<Drawable> getDrawable(String source) {
        return Mono.fromCallable(() -> {
            Glyph destinationGlyph = this.windowsMap.get(source).getGlyph();
            if (destinationGlyph != null) {
                return destinationGlyph.getDrawable();
            }
            throw new IllegalArgumentException("cann't find glyph: " + source);
        }) //
                .publishOn(this.modelScheduler)//
        ;
    }

    /**
     * Window size was changed
     * 
     * @param size new size of window
     */
    public void resizeMainWindow(String name, Size size) {
        setModelProperty(ModelPropertyChangeType.FORM_RESIZE, name, size);
    }

    /**
     * Mouse was clicked
     * 
     * @param mouseX the horizontal x position of the event relative to the window component.
     * @param mouseY the horizontal y position of the event relative to the window component.
     * @param button which, if any, of the mouse buttons has changed state.
     */
    public void mouseClickOnWindow(String name, int mouseX, int mouseY, MouseButton button) {
        setModelProperty(ModelPropertyChangeType.MOUSE_CLICK, name, mouseX, mouseY, button);
    }

    public void mousePressedOnWindow(String name, int mouseX, int mouseY, MouseButton button) {
        setModelProperty(ModelPropertyChangeType.MOUSE_PRESSED, name, mouseX, mouseY, button);
    }

    public void mouseReleasedOnWindow(String name, int mouseX, int mouseY, MouseButton button) {
        setModelProperty(ModelPropertyChangeType.MOUSE_RELEASED, name, mouseX, mouseY, button);
    }

    public void mouseWheelMoved(String name, int mouseX, int mouseY, MouseWheelDirection direction) {
        setModelProperty(ModelPropertyChangeType.MOUSE_WHEEL, name, mouseX, mouseY, direction);
    }

    public void mouseDragged(String name, int mouseX, int mouseY, MouseButton button) {
        setModelProperty(ModelPropertyChangeType.MOUSE_DRAGGED, name, mouseX, mouseY, button);
    }

    public void keyTyped(String name, Character key) {
        setModelProperty(ModelPropertyChangeType.KEY_TYPED, name, key);
    }

    public void keyPressed(String name, KeyCode code) {
        setModelProperty(ModelPropertyChangeType.KEY_PRESSED, name, code);
    }

    public void keyReleased(String name, KeyCode code) {
        setModelProperty(ModelPropertyChangeType.KEY_RELEASED, name, code);
    }

}
