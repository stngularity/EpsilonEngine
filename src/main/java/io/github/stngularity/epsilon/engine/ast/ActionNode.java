package io.github.stngularity.epsilon.engine.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActionNode extends PlaceholderNode {
    public ActionNode(@NotNull String name, @Nullable String data) {
        super(name, data);
    }
}
