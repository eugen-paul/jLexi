package net.eugenpaul.jlexi.window.impl.swing.frame;

import java.beans.PropertyChangeEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.window.AbstractView;
import net.eugenpaul.jlexi.window.propertychanges.UpdateTitle;

public class MainFrame extends AbstractView {
    private static final String TITLE_PREFIX = "jLexi: ";

    private JFrame frame;
    private MainPanel mainPanel;
    private String name;

    public MainFrame(ModelController controller, String name) {
        super(controller);
        this.name = name;
        frame = new JFrame(TITLE_PREFIX + name);
        mainPanel = null;
    }

    @Override
    public boolean init() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(100, 100);
        return true;
    }

    /**
     * Add/set main panel to frame
     * 
     * @param panel
     */
    public void setMainPanel(MainPanel panel) {
        if (null != this.mainPanel) {
            frame.remove(this.mainPanel.getPanel());
        }
        this.mainPanel = panel;

        if (null == panel) {
            return;
        }

        frame.add(mainPanel.getPanel());
        mainPanel.getPanel().requestFocusInWindow();
        frame.pack();
    }

    @Override
    public void setVisible(boolean status) {
        SwingUtilities.invokeLater(() -> frame.setVisible(status));
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase(ViewPropertyChangeType.SET_TITLE.toString())) {
            updateTitle((UpdateTitle) evt.getNewValue());
        }

        mainPanel.modelPropertyChange(evt);
    }

    private void updateTitle(UpdateTitle data) {
        if (data.getTarget().equals(name)) {
            SwingUtilities.invokeLater(() -> this.frame.setTitle(data.getNewTitle()));
        }
    }

}
