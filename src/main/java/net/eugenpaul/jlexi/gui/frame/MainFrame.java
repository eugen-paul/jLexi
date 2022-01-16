package net.eugenpaul.jlexi.gui.frame;

import java.beans.PropertyChangeEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.eugenpaul.jlexi.controller.AbstractController;
import net.eugenpaul.jlexi.gui.AbstractPanel;

public class MainFrame extends AbstractPanel {
    private static final String TITLE_SUFFIX = "jLexi by Eugen Paul";

    private JFrame frame;
    private DocumentPanel mainPanel;

    public MainFrame(AbstractController controller) {
        super(controller);
        frame = new JFrame(TITLE_SUFFIX);
        mainPanel = null;
    }

    @Override
    public boolean init() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setTitle(TITLE_SUFFIX);
        return true;
    }

    /**
     * Add/set main panel to frame
     * 
     * @param panel
     */
    public void setMainPanel(DocumentPanel panel) {
        if (null != this.mainPanel) {
            frame.remove(this.mainPanel.getPanel());
        }
        this.mainPanel = panel;

        if (null == panel) {
            return;
        }

        frame.add(mainPanel.getPanel());
        frame.pack();
    }

    @Override
    public void setVisible(boolean status) {
        SwingUtilities.invokeLater(() -> frame.setVisible(status));
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub
    }

}
