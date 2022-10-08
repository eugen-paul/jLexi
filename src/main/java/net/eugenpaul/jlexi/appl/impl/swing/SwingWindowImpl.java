package net.eugenpaul.jlexi.appl.impl.swing;

import java.util.concurrent.ExecutorService;

import net.eugenpaul.jlexi.appl.impl.swing.frame.MainFrame;
import net.eugenpaul.jlexi.appl.impl.swing.frame.MainPanel;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.controller.WindowController;
import net.eugenpaul.jlexi.window.AbstractView;
import net.eugenpaul.jlexi.window.Windowlmp;
import net.eugenpaul.jlexi.utils.Size;

public class SwingWindowImpl extends Windowlmp {

    private ExecutorService pool;

    public SwingWindowImpl(WindowController controller, ExecutorService pool) {
        super(controller);
        this.pool = pool;
    }

    @Override
    public AbstractView deviceCreateMainWindow(Size defaultSize, String name, GuiGlyph mainGlyph) {

        MainFrame mFrame = new MainFrame(controller, name, pool);
        mFrame.init();

        MainPanel dPanel = new MainPanel(defaultSize, controller, name, mainGlyph);
        dPanel.init();

        mFrame.setMainPanel(dPanel);
        mFrame.setVisible(true);

        controller.addViewChangeListner(mFrame);
        controller.addViewChangeListner(dPanel);

        return mFrame;
    }

}
