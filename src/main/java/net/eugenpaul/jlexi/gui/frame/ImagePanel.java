package net.eugenpaul.jlexi.gui.frame;

import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.eugenpaul.jlexi.utils.Area;

import java.awt.Image;
import java.util.LinkedList;
import java.util.List;

public class ImagePanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImagePanel.class);

    @AllArgsConstructor
    @Getter
    private static class ImageToDraw {
        private Image img = null;
        private Area area = null;
    }

    private transient List<ImageToDraw> nextImageList = null;

    private final transient Object imgSynch = new Object();

    public ImagePanel() {
        nextImageList = new LinkedList<>();
    }

    @Override
    public void paint(Graphics g) {
        synchronized (imgSynch) {
            for (ImageToDraw imageToDraw : nextImageList) {
                if (null == imageToDraw.getArea()) {
                    LOGGER.trace("draw full");
                    g.clearRect(0, 0, getWidth(), getHeight());
                    g.drawImage(imageToDraw.getImg(), 0, 0, this);
                } else {
                    LOGGER.trace("draw area. {}", imageToDraw.getArea());
                    g.clearRect(//
                            imageToDraw.getArea().getPosition().getX(), //
                            imageToDraw.getArea().getPosition().getY(), //
                            imageToDraw.getArea().getSize().getWidth(), //
                            imageToDraw.getArea().getSize().getHight() //
                    );
                    g.drawImage(//
                            imageToDraw.getImg(), //
                            imageToDraw.getArea().getPosition().getX(), //
                            imageToDraw.getArea().getPosition().getY(), //
                            null //
                    );
                }
            }
            nextImageList.clear();
        }
    }

    public void update(Image img) {
        SwingUtilities.invokeLater(() -> {
            synchronized (imgSynch) {
                nextImageList.clear();
                nextImageList.add(new ImageToDraw(img, null));
                repaint();
            }
        });
    }

    public void updateArea(Image img, Area area) {
        SwingUtilities.invokeLater(() -> {
            synchronized (imgSynch) {
                nextImageList.add(new ImageToDraw(img, area));
                repaint();
            }
        });
    }
}
