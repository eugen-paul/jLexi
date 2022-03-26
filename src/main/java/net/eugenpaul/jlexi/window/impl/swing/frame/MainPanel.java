package net.eugenpaul.jlexi.window.impl.swing.frame;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.draw.RedrawData;
import net.eugenpaul.jlexi.utils.Area;
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
        this.panel.addMouseListener(new MouseListner(name, controller));
        this.panel.addKeyListener(new KeyListener(name, controller));
        this.panel.setPreferredSize(new Dimension(defaultSize.getWidth(), defaultSize.getHeight()));
        this.panel.setFocusable(true);
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
        } else if (evt.getPropertyName().equalsIgnoreCase(ViewPropertyChangeType.TRIGGER_AREA_DRAW.toString())) {
            triggetAreaRedraw((String) evt.getSource(), (Area) evt.getNewValue());
        } else if (evt.getPropertyName().equalsIgnoreCase(ViewPropertyChangeType.DRAW_AREA.toString())) {
            drawArea((RedrawData) evt.getNewValue());
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

    private void triggetAreaRedraw(String source, Area area) {
        if (source.equals(name)) {
            controller.getDrawableArea(source, area) //
                    .doOnSuccess(v -> panel.updateArea(v, area)) //
                    .doOnError(throwable -> LOGGER.error("Failed to get drawable from \"{}\"", source, throwable)) //
                    .onErrorResume(throwable -> Mono.empty()) //
                    .subscribe();
        }
    }

    private void drawArea(RedrawData redrawData) {
        if (redrawData.getSource().equals(name)) {
            panel.updateArea(redrawData.getDrawable(), redrawData.getArea());
        }
    }

}
