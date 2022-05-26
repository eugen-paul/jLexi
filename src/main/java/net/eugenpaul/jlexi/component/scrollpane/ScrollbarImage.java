package net.eugenpaul.jlexi.component.scrollpane;

import java.io.IOException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.DrawableImageImpl;
import net.eugenpaul.jlexi.draw.DrawableImageImpl.DrawableImageImplBufferBuilder;
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

        private String arrowFirstPath = null;
        private String arrowLastPath = null;
        private String runnerPath = null;
        private String backgroundPath = null;

        private ScrollbarImageBuilder() {

        }

        public ScrollbarImageBuilder parent(Glyph parent) {
            this.parent = parent;
            return this;
        }

        public ScrollbarImageBuilder type(ScrollbarType type) {
            this.type = type;
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
                    response.arrowFirstBuffer = getDrawImgBuilder(arrowFirstPath, ARROW_UP_DEFAULT);
                    response.arrowLastBuffer = getDrawImgBuilder(arrowLastPath, ARROW_DOWN_DEFAULT);
                    break;
                case HORIZONTAL:
                    response.arrowFirstBuffer = getDrawImgBuilder(arrowFirstPath, ARROW_LEFT_DEFAULT);
                    response.arrowLastBuffer = getDrawImgBuilder(arrowLastPath, ARROW_RIGHT_DEFAULT);
                    break;
                default:
                    break;
                }

                response.runner = getDrawImgBuilder(runnerPath, RUNNER_DEFAULT);
                response.background = getDrawImgBuilder(backgroundPath, SCROLLBAR_BG_DEFAULT);
            } catch (IOException e) {
                response.arrowFirstBuffer = null;
                response.arrowLastBuffer = null;
                response.runner = null;
                response.background = null;
                LOGGER.error("Cann't load arrow image", e);
            }

            response.width = width;
            response.type = type;
            response.scrollbarColor = scrollbarColor;

            return response;
        }

        private DrawableImageImplBufferBuilder getDrawImgBuilder(String path, String defaultPath) throws IOException {
            if (path != null) {
                return DrawableImageImpl.builder().fromPath(Paths.get(path));
            }
            return DrawableImageImpl.builder().fromPath(Paths.get(defaultPath));
        }
    }

}
