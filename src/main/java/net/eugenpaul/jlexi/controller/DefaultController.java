package net.eugenpaul.jlexi.controller;

import net.eugenpaul.jlexi.data.Size;

/**
 * Implementation of {@link AbstractController} for jLexi
 */
public class DefaultController extends AbstractController {

    public static final String RESIZE_MAIN_WINDOW = "Size";

    public void resizeMainWindow(Size size) {
        setModelProperty(RESIZE_MAIN_WINDOW, size);
    }
}
