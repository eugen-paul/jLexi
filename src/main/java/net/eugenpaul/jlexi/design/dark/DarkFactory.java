package net.eugenpaul.jlexi.design.dark;

import java.nio.file.Path;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.component.border.Border;
import net.eugenpaul.jlexi.component.border.Border.BorderBuilderComponent;
import net.eugenpaul.jlexi.component.button.ImageButton;
import net.eugenpaul.jlexi.component.button.TextButton;
import net.eugenpaul.jlexi.component.label.Label;
import net.eugenpaul.jlexi.component.menubar.MenuBar;
import net.eugenpaul.jlexi.component.menubar.MenuBarColored;
import net.eugenpaul.jlexi.component.panes.ImageGlyph;
import net.eugenpaul.jlexi.component.scrollpane.Scrollpane;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.design.GuiFactory;
import net.eugenpaul.jlexi.draw.DrawableImageImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public class DarkFactory implements GuiFactory {

    public static final Color MENU_BACKGROUND_COLOR = Color.fromHexArgb("0xFF505050");

    public static final Color BUTTON_BACKGROUND_COLOR = Color.fromHexArgb("0xFFBFBFBF");
    public static final Color BUTTON_BACKGROUND_FOCUS_COLOR = Color.fromHexArgb("0xFFE2E2E2");
    public static final Color BUTTON_BACKGROUND_PUSH_COLOR = Color.fromHexArgb("0xFFE2E2E2");
    public static final Color BUTTON_BACKGROUND_CHECK_COLOR = Color.fromHexArgb("0xFFB2B2B3");

    public static final Color BORDER_COLOR = Color.fromHexArgb("0xFF000000");
    public static final Color BORDER_BACKGROUND_COLOR = Color.fromHexArgb("0xFFBFBFBF");

    public static final int BORDER_SIZE = 2;

    private static final TextFormat format = TextFormat.DEFAULT.withBackgroundColor(BUTTON_BACKGROUND_COLOR);

    @Override
    public TextButton createTextButton(Glyph parent, String text, ResourceManager storage) {
        return DarkTextButton.builder()//
                .parent(parent)//
                .text(text)//
                .format(format)//
                .storage(storage)//
                .build();
    }

    @Override
    public Label createLabel(Glyph parent, String text, ResourceManager storage) {
        return new Label(parent, text, format, storage);
    }

    @Override
    public MenuBar createMenuBar(Glyph parent, GuiGlyph component, Size size) {
        return new MenuBarColored(parent, component, size, MENU_BACKGROUND_COLOR);
    }

    @Override
    public Border createBorder(Glyph parent, GuiGlyph component) {
        return Border.builder()//
                .parent(parent)//
                .component(component)//
                .borderColor(BORDER_COLOR)//
                .backgroundColor(BORDER_BACKGROUND_COLOR)//
                .borderSize(BORDER_SIZE)//
                .build();
    }

    @Override
    public Scrollpane createScrollpane(Glyph parent, GuiGlyph component) {
        return DarkScrollpane.builder()//
                .parent(parent)//
                .component(component)//
                .scrollbarColor(MENU_BACKGROUND_COLOR)//
                .backgroundColor(BORDER_BACKGROUND_COLOR)//
                .barWidth(35)//
                .build();
    }

    @Override
    public ImageButton createImageButton(Glyph parent, Path imagePath) {
        BorderBuilderComponent borderBuilderComponent = Border.builder()//
                .parent(null)//
                .borderColor(BORDER_COLOR)//
                .backgroundColor(BORDER_BACKGROUND_COLOR)//
                .borderSize(0)//
                .getBuilderComponent()//
        ;

        ImageGlyph image = ImageGlyph.builder()//
                .parent(null)//
                .imagePath(imagePath)//
                .imageBuilder(DrawableImageImpl.builder())//
                .build()//
        ;

        return DarkImageButton.builder()//
                .parent(parent)//
                .image(image)//
                .borderBuilderComponent(borderBuilderComponent)//
                .build()//
        ;
    }

}
