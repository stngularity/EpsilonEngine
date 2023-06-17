package me.stngularity.epsilon.engine.nodes;

public class PlaceholderNode extends BaseNode {
    private final String name;
    private final String data;
    private final String original;

    public PlaceholderNode(String name, String data, String original) {
        super("placeholder");
        this.name = name;
        this.data = data;
        this.original = original;
    }

    public String getPlaceholderName() {
        return name;
    }

    public String getPlaceholderData() {
        return data;
    }

    public String getOriginal() {
        return original;
    }
}
