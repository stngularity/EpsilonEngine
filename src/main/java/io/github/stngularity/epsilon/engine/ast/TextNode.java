package io.github.stngularity.epsilon.engine.ast;

import org.jetbrains.annotations.NotNull;

public class TextNode extends BaseNode {
    public @NotNull String value;

    public TextNode(@NotNull String value) {
        super("text");
        this.value = value;
    }
}
