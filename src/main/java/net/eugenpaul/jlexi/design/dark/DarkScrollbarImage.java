package net.eugenpaul.jlexi.design.dark;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.scrollpane.ScrollbarImage;
import net.eugenpaul.jlexi.utils.Color;

public class DarkScrollbarImage extends ScrollbarImage {

    private static final String ARROW_UP_DEFAULT = "src/main/resources/textures/gui/arrow_up.jpg";
    private static final String ARROW_DOWN_DEFAULT = "src/main/resources/textures/gui/arrow_down.jpg";
    private static final String ARROW_LEFT_DEFAULT = "src/main/resources/textures/gui/arrow_left.jpg";
    private static final String ARROW_RIGHT_DEFAULT = "src/main/resources/textures/gui/arrow_right.jpg";
    private static final String RUNNER_DEFAULT = "src/main/resources/textures/gui/runner.jpg";
    private static final String SCROLLBAR_BG_DEFAULT = "src/main/resources/textures/gui/scrollbar_bg.jpg";

    private DarkScrollbarImage(Glyph parent, ScrollbarType type, String arrowFirstPath, String arrowLastPath,
            String backgroundPath, String runnerPath) {
        super(parent, type, arrowFirstPath, arrowLastPath, backgroundPath, runnerPath);
    }

    public static DarkScrollbarImageBuilder builder() {
        return new DarkScrollbarImageBuilder();
    }

    public static class DarkScrollbarImageBuilder {
        private Glyph parent;
        private Color scrollbarColor = Color.INVISIBLE;

        private int width = DEFAULT_WIDTH;

        private ScrollbarType type = ScrollbarType.VERTICAL;

        private String arrowFirstPath = null;
        private String arrowLastPath = null;
        private String runnerPath = null;
        private String backgroundPath = null;

        private DarkScrollbarImageBuilder() {

        }

        public DarkScrollbarImageBuilder parent(Glyph parent) {
            this.parent = parent;
            return this;
        }

        public DarkScrollbarImageBuilder type(ScrollbarType type) {
            this.type = type;
            return this;
        }

        public DarkScrollbarImageBuilder scrollbarColor(Color scrollbarColor) {
            this.scrollbarColor = scrollbarColor;
            return this;
        }

        public DarkScrollbarImageBuilder arrowFirstPath(String arrowFirstPath) {
            this.arrowFirstPath = arrowFirstPath;
            return this;
        }

        public DarkScrollbarImageBuilder arrowLastPath(String arrowLastPath) {
            this.arrowLastPath = arrowLastPath;
            return this;
        }

        public DarkScrollbarImageBuilder runnerPath(String runnerPath) {
            this.runnerPath = runnerPath;
            return this;
        }

        public DarkScrollbarImageBuilder backgroundPath(String backgroundPath) {
            this.backgroundPath = backgroundPath;
            return this;
        }

        public DarkScrollbarImageBuilder width(int width) {
            this.width = width;
            return this;
        }

        public DarkScrollbarImage build() {

            DarkScrollbarImage response;

            if (type == ScrollbarType.VERTICAL) {
                response = new DarkScrollbarImage(//
                        parent, //
                        type, //
                        getPath(arrowFirstPath, ARROW_UP_DEFAULT), //
                        getPath(arrowLastPath, ARROW_DOWN_DEFAULT), //
                        getPath(backgroundPath, SCROLLBAR_BG_DEFAULT), //
                        getPath(runnerPath, RUNNER_DEFAULT) //
                );
            } else {
                response = new DarkScrollbarImage(//
                        parent, //
                        type, //
                        getPath(arrowFirstPath, ARROW_LEFT_DEFAULT), //
                        getPath(arrowLastPath, ARROW_RIGHT_DEFAULT), //
                        getPath(backgroundPath, SCROLLBAR_BG_DEFAULT), //
                        getPath(runnerPath, RUNNER_DEFAULT) //
                );
            }

            response.setBarWidth(width);
            response.setScrollbarColor(scrollbarColor);

            return response;
        }

        private String getPath(String path, String defaultPath) {
            return (path == null) ? defaultPath : path;
        }

    }

}
