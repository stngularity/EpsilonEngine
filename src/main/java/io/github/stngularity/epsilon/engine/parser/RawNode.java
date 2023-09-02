package io.github.stngularity.epsilon.engine.parser;

import io.github.stngularity.epsilon.engine.EPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RawNode {
    public @NotNull String name;
    public @Nullable String value;
    public @NotNull String original;
    public @NotNull List<RawNode> children;

    public RawNode(@NotNull String name, @NotNull String original) {
        this.name = name;
        value = null;
        this.original = original;
        children = new ArrayList<>();
    }

    public RawNode(@NotNull String name, @Nullable String value, @NotNull String original) {
        this.name = name;
        this.value = value;
        this.original = original;
        children = new ArrayList<>();
    }

    public @NotNull RawNode get(String name) throws IllegalArgumentException {
        for(RawNode child : children) {
            if(!child.name.equals(name)) continue;
            return child;
        }

        throw new IllegalArgumentException("Child with \"" + name + "\" name not found");
    }
}
