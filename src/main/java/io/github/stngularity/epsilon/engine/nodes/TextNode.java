package io.github.stngularity.epsilon.engine.nodes;

public class TextNode extends BaseNode {
    private final String content;

    public TextNode(String content) {
        super("text");
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
