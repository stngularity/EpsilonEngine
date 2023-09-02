package io.github.stngularity.epsilon.engine.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TemplateNode extends BaseNode {
    public @Nullable String data;
    public @NotNull List<BaseNode> templateReturn;

    public TemplateNode(@NotNull String name, @Nullable String data) {
        super(name);
        this.data = data;
        this.templateReturn = new ArrayList<>();
    }
}
