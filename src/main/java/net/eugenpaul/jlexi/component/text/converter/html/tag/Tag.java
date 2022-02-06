package net.eugenpaul.jlexi.component.text.converter.html.tag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Getter;

public abstract class Tag {
    private Map<String, Attribute> attributes;
    private List<Tag> content;

    @Getter
    private final String name;

    @Getter
    private final boolean singleTag;

    protected Tag(String name) {
        this(name, false);
    }

    protected Tag(String name, boolean singleTag) {
        this.name = name;
        this.singleTag = singleTag;
        this.attributes = new HashMap<>();
        this.content = new LinkedList<>();
    }

    public void addAttribute(Attribute attribute) {
        attributes.put(attribute.getName(), attribute);
    }

    public Attribute getAttribute(String name) {
        return attributes.get(name);
    }

    public Iterator<Tag> iterator() {
        return content.iterator();
    }
}
