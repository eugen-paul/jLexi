package net.eugenpaul.jlexi.gui.window;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.window.Windowlmp;
import net.eugenpaul.jlexi.gui.AbstractPanel;
import net.eugenpaul.jlexi.gui.frame.DocumentPanel;
import net.eugenpaul.jlexi.gui.frame.MainFrame;

public class SwingWindowImpl extends Windowlmp {

    public SwingWindowImpl(ModelController controller) {
        super(controller);
    }

    @Override
    public AbstractPanel deviceCreateMainWindow(Glyph mGlyph) {

        MainFrame mFrame = new MainFrame(controller);
        mFrame.init();

        DocumentPanel dPanel = new DocumentPanel(mGlyph, controller);
        mFrame.setMainPanel(dPanel);
        mFrame.setVisible(true);

        controller.addView(mFrame);
        controller.addView(dPanel);

        return mFrame;
    }

}
