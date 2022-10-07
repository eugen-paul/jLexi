package net.eugenpaul.jlexi.appl.impl.swing.frame;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.controller.WindowController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.window.AbstractView;
import reactor.core.publisher.Mono;

@Slf4j
public class MainPanel extends AbstractView {

    @Getter
    private ImagePanel panel;

    private String name;

    @Getter
    private GuiGlyph mainGlyph;

    public MainPanel(Size defaultSize, WindowController controller, String name, GuiGlyph mainGlyph) {
        super(controller);
        this.name = name;
        this.panel = new ImagePanel();
        this.panel.setDoubleBuffered(true);
        this.panel.addComponentListener(new ResizeListner(name, controller));
        this.panel.setPreferredSize(new Dimension(defaultSize.getWidth(), defaultSize.getHeight()));
        this.panel.setFocusable(true);

        this.mainGlyph = mainGlyph;

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
        if (isType(evt, ViewPropertyChangeType.TRIGGER_FULL_DRAW)) {
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
