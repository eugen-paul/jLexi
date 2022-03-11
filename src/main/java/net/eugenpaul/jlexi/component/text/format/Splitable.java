package net.eugenpaul.jlexi.component.text.format;

import java.util.List;

public interface Splitable<T extends TextDocumentElement> {
    public List<T> getSplits();

    public void clearSplitter();
}
