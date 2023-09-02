package io.github.stngularity.epsilon.engine.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderNode extends BaseNode {
    public @Nullable String data;

    public PlaceholderNode(@NotNull String name, @Nullable String data) {
        super(name);
        this.data = data;
    }
}
