package net.eugenpaul.jlexi.design.dark;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.scrollpane.Scrollbar.ScrollbarType;
import net.eugenpaul.jlexi.component.scrollpane.Scrollbar;
import net.eugenpaul.jlexi.component.scrollpane.ScrollbarShowType;
import net.eugenpaul.jlexi.component.scrollpane.Scrollpane;
import net.eugenpaul.jlexi.utils.Color;

/**
 * DarkScrollpane.
 */
public class DarkScrollpane extends Scrollpane {

    private static final int BAR_WIDTH_DEFAULT = 11;

    /**
     * C'tor
     * 
     * @param component component that will be scrolled.
     */
    private DarkScrollpane(Glyph parent, GuiGlyph component, Scrollbar vBar, Scrollbar hBar) {
        super(parent, component, vBar, hBar);
    }

    public static DarkScrollpaneBuilder builder() {
        return new DarkScrollpaneBuilder();
    }

    public static class DarkScrollpaneBuilder {
        private Glyph parent;
        private GuiGlyph component;
        private Color scrollbarColor;
        private Color backgroundColor;
        private int barWidth = BAR_WIDTH_DEFAULT;

        private ScrollbarShowType showVBar = ScrollbarShowType.AT_NEED;
        private ScrollbarShowType showHBar = ScrollbarShowType.AT_NEED;

        private DarkScrollpaneBuilder() {

        }

        public DarkScrollpaneBuilder parent(Glyph parent) {
            this.parent = parent;
            return this;
        }

        public DarkScrollpaneBuilder component(GuiGlyph component) {
            this.component = component;
            return this;
        }

        public DarkScrollpaneBuilder scrollbarColor(Color scrollbarColor) {
            this.scrollbarColor = scrollbarColor;
            return this;
        }

        public DarkScrollpaneBuilder backgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public DarkScrollpaneBuilder barWidth(int barWidth) {
            this.barWidth = barWidth;
            return this;
        }

        public DarkScrollpane build() {
            Scrollbar vBar = DarkScrollbarImage.builder() //
                    .type(ScrollbarType.VERTICAL) //
                    .width(barWidth) //
                    .parent(parent) //
                    .build();

            Scrollbar hBar = DarkScrollbarImage.builder() //
                    .type(ScrollbarType.HORIZONTAL) //
                    .width(barWidth) //
                    .parent(parent) //
                    .build();

            DarkScrollpane response = new DarkScrollpane(parent, component, vBar, hBar);

            response.setScrollbarColor(scrollbarColor);
            response.setBackgroundColor(backgroundColor);
            response.setShowVBar(showVBar);
            response.setShowHBar(showHBar);

            return response;
        }
    }
}
