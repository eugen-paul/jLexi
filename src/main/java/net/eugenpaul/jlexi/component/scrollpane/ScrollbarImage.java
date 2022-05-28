package net.eugenpaul.jlexi.component.scrollpane;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.formatting.HorizontalAlignmentGlypthCompositor;
import net.eugenpaul.jlexi.component.formatting.ToSingleGlyphCompositor;
import net.eugenpaul.jlexi.component.formatting.VerticalAlignmentGlypthCompositor;
import net.eugenpaul.jlexi.component.panes.ImageGlyph;
import net.eugenpaul.jlexi.design.GuiFactory;
import net.eugenpaul.jlexi.draw.DrawableImageImpl;
import net.eugenpaul.jlexi.utils.AligmentH;
import net.eugenpaul.jlexi.utils.AligmentV;
import net.eugenpaul.jlexi.utils.Color;

public class ScrollbarImage extends Scrollbar {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScrollbarImage.class);

    private static final String ARROW_UP_DEFAULT = "src/main/resources/textures/gui/arrow_up.jpg";
    private static final String ARROW_DOWN_DEFAULT = "src/main/resources/textures/gui/arrow_down.jpg";
    private static final String ARROW_LEFT_DEFAULT = "src/main/resources/textures/gui/arrow_left.jpg";
    private static final String ARROW_RIGHT_DEFAULT = "src/main/resources/textures/gui/arrow_right.jpg";
    private static final String RUNNER_DEFAULT = "src/main/resources/textures/gui/runner.jpg";
    private static final String SCROLLBAR_BG_DEFAULT = "src/main/resources/textures/gui/scrollbar_bg.jpg";

    private ScrollbarImage(Glyph parent) {
        super(parent);
    }

    public static ScrollbarImageBuilder builder() {
        return new ScrollbarImageBuilder();
    }

    public static class ScrollbarImageBuilder {
        private Glyph parent;
        private Color scrollbarColor = Color.INVISIBLE;

        private int width = DEFAULT_WIDTH;

        private ScrollbarType type = ScrollbarType.VERTICAL;
        private ToSingleGlyphCompositor<GuiGlyph> compositor = new HorizontalAlignmentGlypthCompositor<>(//
                scrollbarColor, //
                AligmentH.LEFT //
        );

        private String arrowFirstPath = null;
        private String arrowLastPath = null;
        private String runnerPath = null;
        private String backgroundPath = null;

        private GuiFactory factory;

        private ScrollbarImageBuilder() {

        }

        public ScrollbarImageBuilder parent(Glyph parent) {
            this.parent = parent;
            return this;
        }

        public ScrollbarImageBuilder factory(GuiFactory factory) {
            this.factory = factory;
            return this;
        }

        public ScrollbarImageBuilder type(ScrollbarType type) {
            this.type = type;
            if (type == ScrollbarType.VERTICAL) {
                compositor = new HorizontalAlignmentGlypthCompositor<>(//
                        scrollbarColor, //
                        AligmentH.LEFT //
                );
            } else {
                compositor = new VerticalAlignmentGlypthCompositor<>(//
                        scrollbarColor, //
                        AligmentV.TOP //
                );
            }
            return this;
        }

        public ScrollbarImageBuilder scrollbarColor(Color scrollbarColor) {
            this.scrollbarColor = scrollbarColor;
            return this;
        }

        public ScrollbarImageBuilder arrowFirstPath(String arrowFirstPath) {
            this.arrowFirstPath = arrowFirstPath;
            return this;
        }

        public ScrollbarImageBuilder arrowLastPath(String arrowLastPath) {
            this.arrowLastPath = arrowLastPath;
            return this;
        }

        public ScrollbarImageBuilder runnerPath(String runnerPath) {
            this.runnerPath = runnerPath;
            return this;
        }

        public ScrollbarImageBuilder backgroundPath(String backgroundPath) {
            this.backgroundPath = backgroundPath;
            return this;
        }

        public ScrollbarImageBuilder width(int width) {
            this.width = width;
            return this;
        }

        public ScrollbarImage build() {
            ScrollbarImage response = new ScrollbarImage(parent);

            try {
                switch (type) {
                case VERTICAL:
                    response.buttonFirst = factory.createImageButton(parent, getPath(arrowFirstPath, ARROW_UP_DEFAULT));
                    response.buttonLast = factory.createImageButton(parent, getPath(arrowLastPath, ARROW_DOWN_DEFAULT));
                    break;
                case HORIZONTAL:
                    response.buttonFirst = factory.createImageButton(parent,
                            getPath(arrowFirstPath, ARROW_LEFT_DEFAULT));
                    response.buttonLast = factory.createImageButton(parent,
                            getPath(arrowLastPath, ARROW_RIGHT_DEFAULT));
                    break;
                default:
                    break;
                }
                response.buttonFirst.resizeTo(width, width);
                response.buttonLast.resizeTo(width, width);

                response.backgroundGlyph = ImageGlyph.builder()//
                        .parent(null)//
                        .imagePath(getPath(backgroundPath, SCROLLBAR_BG_DEFAULT))//
                        .imageBuilder(DrawableImageImpl.builder())//
                        .build()//
                ;

                response.runnerGlyph = ImageGlyph.builder()//
                        .parent(null)//
                        .imagePath(getPath(runnerPath, RUNNER_DEFAULT))//
                        .imageBuilder(DrawableImageImpl.builder())//
                        .build()//
                ;

            } catch (IOException e) {
                response.buttonFirst = null;
                response.buttonLast = null;
                response.backgroundGlyph = null;
                response.runnerGlyph = null;
                LOGGER.error("Cann't load arrow image", e);
            }

            response.width = width;
            response.type = type;
            response.scrollbarColor = scrollbarColor;
            response.compositor = compositor;

            return response;
        }

        private Path getPath(String path, String defaultPath) throws IOException {
            if (path != null) {
                return Paths.get(path);
            }
            return Paths.get(defaultPath);
        }
    }

}
