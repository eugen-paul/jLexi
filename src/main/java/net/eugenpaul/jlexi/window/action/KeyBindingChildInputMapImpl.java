package net.eugenpaul.jlexi.window.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class KeyBindingChildInputMapImpl implements KeyBindingChildInputMap {

    @AllArgsConstructor
    @Getter
    private class Data {
        private String name;
        private String keys;
        private KeyBindingRule rule;
        private KeyBindingAction action;
    }

    private final Map<String, Data> nameToActionMap;
    private final Supplier<List<KeyBindingChildInputMap>> childListSupplier;

    public KeyBindingChildInputMapImpl(Supplier<List<KeyBindingChildInputMap>> childListSupplier) {
        this.nameToActionMap = new HashMap<>();
        this.childListSupplier = childListSupplier;
    }

    @Override
    public void addAction(String name, String keys, KeyBindingRule rule, KeyBindingAction action) {
        var data = new Data(name, keys, rule, action);
        this.nameToActionMap.put(name, data);
    }

    @Override
    public void removeAction(String name) {
        this.nameToActionMap.remove(name);
    }

    @Override
    public List<KeyBindingAction> getCurrentByKeyAction(String keys) {
        return this.nameToActionMap.values().stream()//
                .filter(v -> v.keys.equals(keys))//
                .map(Data::getAction)//
                .collect(Collectors.toList());
    }

    @Override
    public boolean isKeysSets(String keys) {
        boolean isInSelf = this.nameToActionMap.values().stream()//
                .anyMatch(v -> v.getKeys().equals(keys));

        if (isInSelf) {
            return true;
        }

        for (var childKeys : getChildsMaps()) {
            if (childKeys.isKeysSets(keys)) {
                return true;
            }
        }

        return false;
    }

    private List<KeyBindingChildInputMap> getChildsMaps() {
        return this.childListSupplier.get();
    }

    @Override
    public List<String> getAllKeys() {
        Set<String> response = new HashSet<>();
        nameToActionMap.values().forEach(v -> response.add(v.getKeys()));

        for (var child : childListSupplier.get()) {
            response.addAll(child.getAllKeys());
        }
        return response.stream().collect(Collectors.toList());
    }

    @Override
    public List<KeyBindingAction> getAllByKeyActions(String keys) {
        List<KeyBindingAction> response = new LinkedList<>();

        response.addAll(getCurrentByKeyAction(keys));

        for (var child : childListSupplier.get()) {
            response.addAll(child.getAllByKeyActions(keys));
        }

        return response;
    }

}
