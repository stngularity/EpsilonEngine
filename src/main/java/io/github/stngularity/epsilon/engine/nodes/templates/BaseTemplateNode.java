package io.github.stngularity.epsilon.engine.nodes.templates;

import io.github.stngularity.epsilon.engine.nodes.templates.if_template.IfTemplateNode;
import io.github.stngularity.epsilon.engine.EpsilonEngine;
import io.github.stngularity.epsilon.engine.nodes.BaseNode;
import io.github.stngularity.epsilon.engine.nodes.RootNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseTemplateNode extends BaseNode {
    private final String templateName;
    private final String templateData;
    private final String templateContent;

    public BaseTemplateNode(String name, String data, String content) {
        super("template");

        this.templateName = name;
        this.templateData = data;
        this.templateContent = content;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getData() {
        return templateData;
    }

    public String getContent() {
        return templateContent;
    }

    public abstract RootNode processTemplate(EpsilonEngine engine);

    /**
     * Process specified data and returns new template instance
     *
     * @param name    The name of the template. For example, `if`
     * @param data    The data of the template
     * @param content The content of the template
     *
     * @return New instance of template with specified name
     */
    public static @Nullable BaseTemplateNode processAbstractTemplate(@NotNull String name, String data, String content) {
        if(name.equals("if")) return new IfTemplateNode(data, content);
        if(name.equals("format")) return new FormatTemplateNode(data, content);
        return null;
    }
}
