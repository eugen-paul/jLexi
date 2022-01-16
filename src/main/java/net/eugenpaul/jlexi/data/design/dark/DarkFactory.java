package net.eugenpaul.jlexi.data.design.dark;

import net.eugenpaul.jlexi.data.design.Button;
import net.eugenpaul.jlexi.data.design.GuiFactory;

public class DarkFactory extends GuiFactory {

    @Override
    public Button createButton() {
        return new DarkButton();
    }

}