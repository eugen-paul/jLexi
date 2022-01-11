package net.eugenpaul.jlexi;

import net.eugenpaul.jlexi.controller.DefaultController;
import net.eugenpaul.jlexi.gui.window.MainFrame;

public class JLexi {

    public static void main(String[] args) {
        DefaultController controller = new DefaultController();
        MainFrame mFrame = new MainFrame(controller);
        mFrame.init();

        controller.addView(mFrame);

        mFrame.setVisible(true);
    }
}
