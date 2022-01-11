package net.eugenpaul.jlexi.gui.window;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.eugenpaul.jlexi.controller.AbstractController;

public class MainFrame extends AbstractPanel {
    private static final String TITLE_SUFFIX = "jLexi by Eugen Paul";

    private JFrame frame;
    private JPanel mainPanel;

    public MainFrame(AbstractController controller) {
        super(controller);
        frame = new JFrame(TITLE_SUFFIX);
    }

    @Override
    public boolean init() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.setMinimumSize(new Dimension(400, 300));
        frame.setLocation(100, 100);
        frame.setTitle(TITLE_SUFFIX);

        mainPanel = new JPanel();
        frame.add(mainPanel);
        return true;
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
