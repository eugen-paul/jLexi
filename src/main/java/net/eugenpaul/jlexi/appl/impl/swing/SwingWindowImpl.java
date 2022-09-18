package net.eugenpaul.jlexi.appl.impl.swing;

import net.eugenpaul.jlexi.appl.impl.swing.frame.MainFrame;
import net.eugenpaul.jlexi.appl.impl.swing.frame.MainPanel;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.window.AbstractView;
import net.eugenpaul.jlexi.window.Windowlmp;
import net.eugenpaul.jlexi.utils.Size;

public class SwingWindowImpl extends Windowlmp {

    public SwingWindowImpl(ModelController controller) {
        super(controller);
    }

    @Override
    public AbstractView deviceCreateMainWindow(Size defaultSize, String name) {

        MainFrame mFrame = new MainFrame(controller, name);
        mFrame.init();

        MainPanel dPanel = new MainPanel(defaultSize, controller, name);
        dPanel.init();

        mFrame.setMainPanel(dPanel);
        mFrame.setVisible(true);

        controller.addView(mFrame);

        return mFrame;
    }

}
