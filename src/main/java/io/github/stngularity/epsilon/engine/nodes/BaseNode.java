package io.github.stngularity.epsilon.engine.nodes;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseNode {
    private final String name;
    private final List<BaseNode> children;

    protected BaseNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<BaseNode> getChildren() {
        return children;
    }

    public void addChild(BaseNode child) {
        if(child == null) return;
        children.add(child);
    }

    public void insertChild(int index, BaseNode child) {
        children.add(index, child);
    }

    public int size() {
        return children.size();
    }
}
