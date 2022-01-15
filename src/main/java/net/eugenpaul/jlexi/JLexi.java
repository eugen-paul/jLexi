package net.eugenpaul.jlexi;

import java.beans.PropertyChangeEvent;

import javax.swing.JPanel;

import net.eugenpaul.jlexi.controller.DefaultController;
import net.eugenpaul.jlexi.data.design.EmptyPanel;
import net.eugenpaul.jlexi.data.framing.MenuBar;
import net.eugenpaul.jlexi.gui.frame.DocumentPanel;
import net.eugenpaul.jlexi.gui.frame.MainFrame;

public class JLexi {

    public static void main(String[] args) {
        DefaultController controller = new DefaultController();
        MainFrame mFrame = new MainFrame(controller);
        mFrame.init();

        EmptyPanel ePanel = new EmptyPanel(800, 540);
        MenuBar menubar = new MenuBar(ePanel, 800, 600);
        DocumentPanel dPanel = new DocumentPanel(menubar, controller);
        mFrame.setMainPanel(dPanel);

        controller.addView(mFrame);
        controller.addView(dPanel);

        mFrame.setVisible(true);

        controller.propertyChange(new PropertyChangeEvent(menubar, "UPDATE", 1, 2));
    }
}
