package net.eugenpaul.jlexi.controller;

import net.eugenpaul.jlexi.data.Size;

/**
 * Implementation of {@link AbstractController} for jLexi
 */
public class DefaultController extends AbstractController {

    public void resizeMainWindow(Size size) {
        setModelProperty(ModelPropertyChangeType.FORM_RESIZE, size);
    }
}
