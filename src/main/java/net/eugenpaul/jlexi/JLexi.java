package net.eugenpaul.jlexi;

import java.beans.PropertyChangeEvent;

import net.eugenpaul.jlexi.controller.DefaultController;
import net.eugenpaul.jlexi.data.design.Panel;
import net.eugenpaul.jlexi.data.framing.MenuBar;
import net.eugenpaul.jlexi.data.window.Window;
import net.eugenpaul.jlexi.gui.window.SwingWindowImpl;

public class JLexi {

    public static void main(String[] args) {
        DefaultController controller = new DefaultController();

        Window windowAbstraction = new Window(new SwingWindowImpl(controller));

        Panel ePanel = new Panel(800, 560);
        MenuBar menubar = new MenuBar(ePanel, 800, 600);

        windowAbstraction.createMainWindow(menubar);

        controller.propertyChange(new PropertyChangeEvent(menubar, "UPDATE", 1, 2));
    }
}
