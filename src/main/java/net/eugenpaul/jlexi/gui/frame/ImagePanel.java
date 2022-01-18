package net.eugenpaul.jlexi.gui.frame;

import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Image;
import java.awt.image.MemoryImageSource;

public class ImagePanel extends JPanel {
    private Image img = null;
    private final Object imgSynch = new Object();

    @Override
    public void paint(Graphics g) {
        synchronized (imgSynch) {
            if (this.img != null) {
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(img, 0, 0, this);
            }
        }
    }

    public void update(MemoryImageSource img) {
        SwingUtilities.invokeLater(() -> {
            synchronized (imgSynch) {
                this.img = createImage(img);
                repaint();
            }
        });
    }
}
