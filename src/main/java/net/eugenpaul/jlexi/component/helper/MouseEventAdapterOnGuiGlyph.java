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
        if (this.isPositionOnComponent.apply(mouseX, mouseY).booleanValue()) {
            this.component.onMouseClick(//
                    mouseX - this.component.getRelativPosition().getX(), //
                    mouseY - this.component.getRelativPosition().getY(), //
                    button//
            );
        } else {
            this.onMouseClickOutsideComponent.accept(mouseX, mouseY, button);
        }
    }

    @Override
    public void mousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        if (this.isPositionOnComponent.apply(mouseX, mouseY).booleanValue()) {
            this.component.onMousePressed(//
                    mouseX - this.component.getRelativPosition().getX(), //
                    mouseY - this.component.getRelativPosition().getY(), //
                    button//
            );
        } else {
            this.onMousePressedOutsideComponent.accept(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        if (this.isPositionOnComponent.apply(mouseX, mouseY).booleanValue()) {
            this.component.onMouseReleased(//
                    mouseX - this.component.getRelativPosition().getX(), //
                    mouseY - this.component.getRelativPosition().getY(), //
                    button//
            );
        } else {
            this.onMouseReleasedOutsideComponent.accept(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
        if (this.isPositionOnComponent.apply(mouseX, mouseY).booleanValue()) {
            this.component.onMouseDragged(//
                    mouseX - this.component.getRelativPosition().getX(), //
                    mouseY - this.component.getRelativPosition().getY(), //
                    button//
            );
        }
    }

    @Override
    public void mouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
        if (this.isPositionOnComponent.apply(mouseX, mouseY).booleanValue()) {
            this.component.onMouseWhellMoved(//
                    mouseX - this.component.getRelativPosition().getX(), //
                    mouseY - this.component.getRelativPosition().getY(), //
                    direction//
            );
        } else {
            this.onMouseWhellMovedOutsideComponent.accept(mouseX, mouseY, direction);
        }
    }
}
