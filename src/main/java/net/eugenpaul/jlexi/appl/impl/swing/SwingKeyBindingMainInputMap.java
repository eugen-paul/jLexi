package net.eugenpaul.jlexi.appl.impl.swing;

import java.util.stream.Collectors;

import javax.swing.ComponentInputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import net.eugenpaul.jlexi.component.GuiGlyph;

public class SwingKeyBindingMainInputMap extends ComponentInputMap {

    private final transient GuiGlyph glyph;

    public SwingKeyBindingMainInputMap(JComponent component, GuiGlyph glyph) {
        super(component);
        this.glyph = glyph;
    }

    @Override
    public Object get(KeyStroke keyStroke) {

        var isSet = glyph.getKeyBindingMap().isKeysSets(keyStroke.toString());

        return (isSet) ? null : "found";
    }

    @Override
    public KeyStroke[] keys() {
        return allKeys();
    }

    @Override
    public KeyStroke[] allKeys() {
        return glyph.getKeyBindingMap().getAllKeys().stream()//
                .map(KeyStroke::getKeyStroke) //
                .collect(Collectors.toList()) //
                .toArray(new KeyStroke[0]);
    }

}
