package me.stngularity.epsilon.engine;

import me.stngularity.epsilon.engine.actions.Action;
import me.stngularity.epsilon.engine.actions.Actions;
import me.stngularity.epsilon.engine.nodes.ActionNode;
import me.stngularity.epsilon.engine.nodes.PlaceholderNode;
import me.stngularity.epsilon.engine.nodes.RootNode;
import me.stngularity.epsilon.engine.nodes.TextNode;
import me.stngularity.epsilon.engine.nodes.templates.BaseTemplateNode;
import me.stngularity.epsilon.engine.placeholders.IPlaceholder;
import me.stngularity.epsilon.engine.placeholders.MapPlaceholder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class EpsilonEngine {
    private final Parser parser;
    private final List<IPlaceholder> placeholders;
    private final List<Action> actions;

    public EpsilonEngine() {
        this.parser = new Parser();
        this.placeholders = new ArrayList<>();

        this.actions = new ArrayList<>();
        actions.add(Actions.IGNORE_LINE);
    }

    public EpsilonEngine(Pattern placeholder, Pattern templateStart, Pattern templateEnd, Pattern action) {
        this.parser = new Parser(placeholder, templateStart, templateEnd, action);
        this.placeholders = new ArrayList<>();

        this.actions = new ArrayList<>();
        actions.add(Actions.IGNORE_LINE);
    }

    public Parser getParser() {
        return parser;
    }

    public List<IPlaceholder> getPlaceholders() {
        return placeholders;
    }

    public void addPlaceholder(IPlaceholder placeholder) {
        placeholders.add(placeholder);
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void addPlaceholders(IPlaceholder... placeholders) {
        Collections.addAll(this.placeholders, placeholders);
    }

    public void addActions(Action... actions) {
        Collections.addAll(this.actions, actions);
    }

    public void removePlaceholder(int index) {
        placeholders.remove(index);
    }

    public void removeAction(int index) {
        actions.remove(index);
    }

    public void removePlaceholder(IPlaceholder placeholder) {
        placeholders.remove(placeholder);
    }

    public void removeAction(Action action) {
        actions.remove(action);
    }

    public void clearPlaceholders() {
        placeholders.clear();
    }

    public void clearActions() {
        actions.clear();
    }

    public boolean placeholderEqual(IPlaceholder placeholder, String name) {
        if(!(placeholder instanceof MapPlaceholder) && !placeholder.getName().equals(name)) return false;
        return !(placeholder instanceof MapPlaceholder) || name.startsWith(placeholder.getName());
    }

    public String getPlaceholderValue(@NotNull IPlaceholder placeholder, String name, String data) {
        return placeholder.getValue(placeholder instanceof MapPlaceholder ? (name + "\u007F" + data) : data);
    }

    public String process(String content) {
        return process(parser.parse(content));
    }

    public String process(@NotNull RootNode root) {
        StringBuilder buffer = new StringBuilder();

        root.getChildren().forEach(line -> {
            StringBuilder lineBuffer = new StringBuilder();
            line.getChildren().forEach(child -> {
                if(lineBuffer.toString().endsWith("\u007Fremove\u007F"))
                    return;

                if(child instanceof PlaceholderNode) {
                    PlaceholderNode node = (PlaceholderNode) child;
                    placeholders.forEach(placeholder -> {
                        String name = node.getPlaceholderName();
                        if(!placeholderEqual(placeholder, name)) return;

                        String value = getPlaceholderValue(placeholder, name, node.getPlaceholderData());
                        lineBuffer.append(value.equals("null") ? node.getOriginal() : value);
                    });
                }

                if(child instanceof ActionNode) {
                    ActionNode node = (ActionNode) child;
                    actions.forEach(action -> {
                        if(!action.getName().equals(node.getActionName())) return;
                        String newLine = action.processLine(lineBuffer.toString(), node.getActionData());
                        lineBuffer.replace(0, lineBuffer.length(), (newLine == null ? "\u007Fremove\u007F" : newLine));
                    });
                }

                if(child instanceof BaseTemplateNode) {
                    String lines = process(((BaseTemplateNode) child).processTemplate(this));
                    lineBuffer.append(lines);
                }

                if(child instanceof TextNode && !lineBuffer.toString().endsWith("\u007Fremove\u007F"))
                    lineBuffer.append(((TextNode) child).getContent());
            });

            if(lineBuffer.toString().endsWith("\u007Fremove\u007F")) {
                if(root.getChildren().size() == 1) buffer.append("\u007Fremove\u007F");
                return;
            }

            buffer.append(lineBuffer.append("\n"));
        });

        if(!buffer.toString().endsWith("\u007Fremove\u007F") && buffer.length() > 0)
            buffer.deleteCharAt(buffer.length()-1);

        return buffer.toString();
    }
}
