package net.eugenpaul.jlexi.gui.frame;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import lombok.AllArgsConstructor;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.utils.Size;

@AllArgsConstructor
public class ResizeListner extends ComponentAdapter {

    private ModelController controller;

    @Override
    public void componentResized(ComponentEvent e) {
        Size size = new Size(//
                ((JPanel) e.getSource()).getWidth(), //
                ((JPanel) e.getSource()).getHeight()//
        );

        controller.resizeMainWindow(size);
    }
}
