package net.eugenpaul.jlexi.window.impl.swing.frame;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.window.AbstractView;
import reactor.core.publisher.Mono;

public class MainPanel extends AbstractView {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainPanel.class);

    @Getter
    private ImagePanel panel;

    private String name;

    public MainPanel(Size defaultSize, ModelController controller, String name) {
        super(controller);
        this.name = name;
        this.panel = new ImagePanel();
        this.panel.setDoubleBuffered(true);
        this.panel.addComponentListener(new ResizeListner(name, controller));
        this.panel.setPreferredSize(new Dimension(defaultSize.getWidth(), defaultSize.getHeight()));
        this.panel.setFocusable(true);

        var mouseListner = new MouseListner(name, controller);
        this.panel.addMouseListener(mouseListner);
        this.panel.addMouseMotionListener(mouseListner);
        this.panel.addMouseWheelListener(mouseListner);
        this.panel.addKeyListener(new KeyListener(name, controller));
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void setVisible(boolean status) {
        // Nothing to do
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase(ViewPropertyChangeType.TRIGGER_FULL_DRAW.toString())) {
            triggerFullDraw((String) evt.getSource());
        }
    }

    private void triggerFullDraw(String source) {
        if (source.equals(name)) {
            controller.getDrawable(source) //
                    .doOnSuccess(panel::update) //
                    .doOnError(throwable -> LOGGER.error("Failed to get drawable from \"{}\"", source, throwable)) //
                    .onErrorResume(throwable -> Mono.empty()) //
                    .subscribe();
        }
    }

}
