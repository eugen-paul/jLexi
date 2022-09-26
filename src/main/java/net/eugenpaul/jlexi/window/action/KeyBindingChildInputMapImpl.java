package net.eugenpaul.jlexi.window.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class KeyBindingChildInputMapImpl implements KeyBindingChildInputMap {

    private final Map<String, KeyBindingAction> keysToActionMap;
    private final Supplier<List<KeyBindingChildInputMap>> childListSupplier;

    public KeyBindingChildInputMapImpl(Supplier<List<KeyBindingChildInputMap>> childListSupplier) {
        this.keysToActionMap = new HashMap<>();
        this.childListSupplier = childListSupplier;
    }

    @Override
    public void addAction(String keys, KeyBindingRule rule, KeyBindingAction action) {
        this.keysToActionMap.put(keys, action);
    }

    @Override
    public void removeAction(String keys, KeyBindingRule rule, KeyBindingAction action) {
        this.keysToActionMap.remove(keys);
    }

    @Override
    public KeyBindingAction getAction(String keys) {
        return this.keysToActionMap.get(keys);
    }

    @Override
    public boolean isKeysSets(String keys) {
        if (keysToActionMap.containsKey(keys)) {
            return true;
        }

        for (var childKeys : getChildsMaps()) {
            if (childKeys.isKeysSets(keys)) {
                return true;
            }
        }

        return false;
    }

    // @Override
    private List<KeyBindingChildInputMap> getChildsMaps() {
        return this.childListSupplier.get();
    }

}
