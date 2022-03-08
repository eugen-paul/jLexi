package net.eugenpaul.jlexi.component.text.format;

import java.util.List;

public interface Splitable<T extends TextFormat> {
    public List<T> getSplits();

    public void clearSplitter();
}
