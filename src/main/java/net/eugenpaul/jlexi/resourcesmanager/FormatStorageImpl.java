package net.eugenpaul.jlexi.resourcesmanager;

import java.util.HashMap;
import java.util.Map;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;

public class FormatStorageImpl implements FormatStorage {

    private Map<FormatAttribute, TextFormat> storage;

    public FormatStorageImpl() {
        storage = new HashMap<>();
    }

    @Override
    public TextFormat of(FormatAttribute format) {
        return storage.computeIfAbsent(format, FormatAttribute::toTextFormat);
    }

}
