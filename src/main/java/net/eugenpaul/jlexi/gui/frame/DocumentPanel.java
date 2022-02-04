package net.eugenpaul.jlexi.gui.frame;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.draw.RedrawData;
import net.eugenpaul.jlexi.gui.AbstractPanel;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Size;
import reactor.core.publisher.Mono;

public class DocumentPanel extends AbstractPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentPanel.class);

    @Getter
    private ImagePanel panel;

    public DocumentPanel(Size defaultSize, ModelController controller) {
        super(controller);
        this.panel = new ImagePanel();
        this.panel.setDoubleBuffered(true);
        this.panel.addComponentListener(new ResizeListner(controller));
        this.panel.addMouseListener(new MouseListner(controller));
        this.panel.addKeyListener(new KeyListener(controller));
        this.panel.setPreferredSize(new Dimension(defaultSize.getWidth(), defaultSize.getHeight()));
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
        controller.getDrawable(source) //
                .doOnSuccess(panel::update) //
                .doOnError(throwable -> LOGGER.error("Failed to get drawable from \"{}\"", source, throwable)) //
                .onErrorResume(throwable -> Mono.empty()) //
                .subscribe();
    }

    private void triggetAreaRedraw(String source, Area area) {
        controller.getDrawableArea(source, area) //
                .doOnSuccess(v -> panel.updateArea(v, area)) //
                .doOnError(throwable -> LOGGER.error("Failed to get drawable from \"{}\"", source, throwable)) //
                .onErrorResume(throwable -> Mono.empty()) //
                .subscribe();
    }

    private void drawArea(RedrawData redrawData) {
        panel.updateArea(redrawData.getDrawable(), redrawData.getArea());
    }

}
