package net.eugenpaul.jlexi.data.design.bright;

import net.eugenpaul.jlexi.data.design.Button;
import net.eugenpaul.jlexi.data.design.GuiFactory;

public class BrightFactory extends GuiFactory {

    @Override
    public Button createButton() {
        return new BrightButton();
    }

}
