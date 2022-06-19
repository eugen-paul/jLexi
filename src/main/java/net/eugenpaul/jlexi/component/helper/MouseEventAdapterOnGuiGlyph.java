package net.eugenpaul.jlexi.component.helper;

import java.util.function.BiFunction;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;
import net.eugenpaul.jlexi.utils.reflection.TriConsumer;

@AllArgsConstructor
public class MouseEventAdapterOnGuiGlyph implements MouseEventAdapter {

    private GuiGlyph component;

    private BiFunction<Integer, Integer, Boolean> isPositionOnComponent;

    private TriConsumer<Integer, Integer, MouseButton> onMouseClickOutsideComponent;

    private TriConsumer<Integer, Integer, MouseButton> onMousePressedOutsideComponent;

    private TriConsumer<Integer, Integer, MouseButton> onMouseReleasedOutsideComponent;

    private TriConsumer<Integer, Integer, MouseWheelDirection> onMouseWhellMovedOutsideComponent;

    @Override
    public void mouseClicked(Integer mouseX, Integer mouseY, MouseButton button) {
        if (isPositionOnComponent.apply(mouseX, mouseY).booleanValue()) {
            component.onMouseClick(//
                    mouseX - component.getRelativPosition().getX(), //
                    mouseY - component.getRelativPosition().getY(), //
                    button//
            );
        } else {
            onMouseClickOutsideComponent.accept(mouseX, mouseY, button);
        }
    }

    @Override
    public void mousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        if (isPositionOnComponent.apply(mouseX, mouseY).booleanValue()) {
            component.onMousePressed(//
                    mouseX - component.getRelativPosition().getX(), //
                    mouseY - component.getRelativPosition().getY(), //
                    button//
            );
        } else {
            onMousePressedOutsideComponent.accept(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        if (isPositionOnComponent.apply(mouseX, mouseY).booleanValue()) {
            component.onMouseReleased(//
                    mouseX - component.getRelativPosition().getX(), //
                    mouseY - component.getRelativPosition().getY(), //
                    button//
            );
        } else {
            onMouseReleasedOutsideComponent.accept(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
        if (isPositionOnComponent.apply(mouseX, mouseY).booleanValue()) {
            component.onMouseDragged(//
                    mouseX - component.getRelativPosition().getX(), //
                    mouseY - component.getRelativPosition().getY(), //
                    button//
            );
        }
    }

    @Override
    public void mouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
        if (isPositionOnComponent.apply(mouseX, mouseY).booleanValue()) {
            component.onMouseWhellMoved(//
                    mouseX - component.getRelativPosition().getX(), //
                    mouseY - component.getRelativPosition().getY(), //
                    direction//
            );
        } else {
            onMouseWhellMovedOutsideComponent.accept(mouseX, mouseY, direction);
        }
    }
}
