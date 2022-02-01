package net.eugenpaul.jlexi.gui.frame;

import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.eugenpaul.jlexi.utils.Area;

import java.awt.Image;
import java.awt.image.MemoryImageSource;

public class ImagePanel extends JPanel {
    private transient Image img = null;
    private transient Image nextImage = null;
    private transient Area area = null;
    private final transient Object imgSynch = new Object();

    @Override
    public void paint(Graphics g) {
        synchronized (imgSynch) {
            if (this.img != null) {
                if (nextImage != null) {
                    g.clearRect(//
                            area.getPosition().getX(), //
                            area.getPosition().getY(), //
                            area.getSize().getWidth(), //
                            area.getSize().getHight() //
                    );
                    g.drawImage(nextImage, area.getPosition().getX(), area.getPosition().getY(), null);
                    nextImage = null;
                } else {
                    g.clearRect(0, 0, getWidth(), getHeight());
                    g.drawImage(img, 0, 0, this);
                }
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

    public void updateArea(Image img, Area area) {
        SwingUtilities.invokeLater(() -> {
            synchronized (imgSynch) {
                nextImage = img;
                this.area = area;
                repaint();
            }
        });
    }
    public void updateArea(MemoryImageSource img, Area area) {
        SwingUtilities.invokeLater(() -> {
            synchronized (imgSynch) {
                nextImage = createImage(img);
                this.area = area;
                repaint();
            }
        });
    }
}
