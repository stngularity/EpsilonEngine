package io.github.stngularity.epsilon.engine.nodes;

public class ActionNode extends BaseNode {
    private final String name;
    private final String data;

    public ActionNode(String name, String data) {
        super("action");
        this.name = name;
        this.data = data;
    }

    public String getActionName() {
        return name;
    }

    public String getActionData() {
        return data;
    }
}
