package net.eugenpaul.jlexi.gui.window;

import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.window.Windowlmp;
import net.eugenpaul.jlexi.gui.AbstractPanel;
import net.eugenpaul.jlexi.gui.frame.DocumentPanel;
import net.eugenpaul.jlexi.gui.frame.MainFrame;
import net.eugenpaul.jlexi.utils.Size;

public class SwingWindowImpl extends Windowlmp {

    public SwingWindowImpl(ModelController controller) {
        super(controller);
    }

    @Override
    public AbstractPanel deviceCreateMainWindow(Size defaultSize) {

        MainFrame mFrame = new MainFrame(controller);
        mFrame.init();

        DocumentPanel dPanel = new DocumentPanel(defaultSize, controller);
        mFrame.setMainPanel(dPanel);
        mFrame.setVisible(true);

        controller.addView(mFrame);
        controller.addView(dPanel);

        return mFrame;
    }

}
