package io.github.stngularity.epsilon.engine.ast;

import io.github.stngularity.epsilon.engine.parser.RawNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AbstractSyntaxTree {
    public static @NotNull RootNode process(@NotNull List<RawNode> nodes) {
        RootNode root = new RootNode();
        nodes.forEach(raw -> {
            if(raw.name.equals("text")) {
                assert raw.value != null;
                root.children.add(new TextNode(raw.value));
            }

            if(raw.name.equals("placeholder")) {
                // placeholder.name always on 0 index
                String name = raw.children.get(0).value;
                assert name != null;

                // placeholder.data always on 1 index
                String data = (raw.children.size() > 1 ? raw.children.get(1).value : null);

                root.children.add(new PlaceholderNode(name, data));
            }

            if(raw.name.equals("action")) {
                // action.name always on 0 index
                String name = raw.children.get(0).value;
                assert name != null;

                // action.data always on 1 index
                String data = (raw.children.size() > 1 ? raw.children.get(1).value : null);

                root.children.add(new ActionNode(name, data));
            }

            if(raw.name.equals("template")) {
                TemplateNode node = new TemplateNode("null", null);
                raw.children.forEach(template -> {
                    // template.name always on 0 index
                    String name = template.children.get(0).value;
                    assert name != null;

                    // template.data always on 1 index
                    String data = (template.children.size() > 1 ? template.children.get(1).value : null);

                    // template.return always on 2 (if data exists) or 1 (else) index
                    int index = (data == null ? 1 : 2);
                    RawNode ret = (template.children.size() > index ? template.children.get(index) : null);

                    TemplateNode child = new TemplateNode(name, data);
                    if(ret != null)
                        child.templateReturn.addAll(process(ret.children).children);

                    node.children.add(child);
                });

                root.children.add(node);
            }
        });

        return root;
    }
}
