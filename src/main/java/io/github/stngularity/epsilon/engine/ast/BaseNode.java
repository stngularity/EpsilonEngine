package io.github.stngularity.epsilon.engine.ast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BaseNode {
    public @NotNull String name;
    public @NotNull List<BaseNode> children;

    public BaseNode(@NotNull String name) {
        this.name = name;
        children = new ArrayList<>();
    }
}
