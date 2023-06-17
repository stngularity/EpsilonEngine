package io.github.stngularity.epsilon.engine;

import io.github.stngularity.epsilon.engine.nodes.ActionNode;
import io.github.stngularity.epsilon.engine.nodes.PlaceholderNode;
import io.github.stngularity.epsilon.engine.nodes.RootNode;
import io.github.stngularity.epsilon.engine.nodes.TextNode;
import io.github.stngularity.epsilon.engine.nodes.templates.BaseTemplateNode;
import io.github.stngularity.epsilon.engine.placeholders.MapPlaceholder;
import io.github.stngularity.epsilon.engine.actions.Action;
import io.github.stngularity.epsilon.engine.actions.Actions;
import io.github.stngularity.epsilon.engine.placeholders.IPlaceholder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The primary class of Epsilon Engine
 */
public class EpsilonEngine {
    private final Parser parser;
    private final List<IPlaceholder> placeholders;
    private final List<Action> actions;

    /**
     * Initializes the engine class with default patterns
     */
    public EpsilonEngine() {
        this.parser = new Parser();
        this.placeholders = new ArrayList<>();

        this.actions = new ArrayList<>();
        actions.add(Actions.IGNORE_LINE);
    }

    /**
     * Initializes the engine class with custom patterns
     *
     * @param placeholder   The pattern of placeholders.
     *                      Group 1 — placeholder name; group 2 — placeholder data
     * @param templateStart The pattern of template's start.
     *                      Group 1 — template name; group 2 — template data
     * @param templateEnd   The pattern of template's end.
     *                      Group 1 — “end” + template name;
     * @param action        The pattern of actions.
     *                      Group 1 — action name; group 2 — action data
     */
    public EpsilonEngine(Pattern placeholder, Pattern templateStart, Pattern templateEnd, Pattern action) {
        this.parser = new Parser(placeholder, templateStart, templateEnd, action);
        this.placeholders = new ArrayList<>();

        this.actions = new ArrayList<>();
        actions.add(Actions.IGNORE_LINE);
    }

    /**
     * Returns parser object for this engine
     * @return {@link Parser} object
     */
    public Parser getParser() {
        return parser;
    }

    /**
     * Returns list of placeholders that storage at this engine instance
     * @return The list of {@link IPlaceholder} instances
     */
    public List<IPlaceholder> getPlaceholders() {
        return placeholders;
    }

    /**
     * Adds a placeholder to the engine instance
     * @param placeholder Placeholder itself
     */
    public void addPlaceholder(IPlaceholder placeholder) {
        placeholders.add(placeholder);
    }

    /**
     * Adds action to the engine instance
     * @param action Action itself
     */
    public void addAction(Action action) {
        actions.add(action);
    }

    /**
     * Adds all placeholders from specified array to the engine instance
     * @param placeholders The list of {@link IPlaceholder} instances
     */
    public void addPlaceholders(IPlaceholder... placeholders) {
        Collections.addAll(this.placeholders, placeholders);
    }

    /**
     * Adds all actions from specified array to the engine instance
     * @param actions The list of {@link Action} instances
     */
    public void addActions(Action... actions) {
        Collections.addAll(this.actions, actions);
    }

    /**
     * Removes any placeholder from the engine instance by its index
     * @param index The index of placeholder that you want to remove
     */
    public void removePlaceholder(int index) {
        placeholders.remove(index);
    }

    /**
     * Removes any action from the engine instance by its index
     * @param index The index of action that you want to remove
     */
    public void removeAction(int index) {
        actions.remove(index);
    }

    /**
     * Removes any placeholder from the engine instance by its instance
     * @param placeholder The placeholder instance you added to the engine earlier
     */
    public void removePlaceholder(IPlaceholder placeholder) {
        placeholders.remove(placeholder);
    }

    /**
     * Removes any action from the engine instance by its instance
     * @param action The action instance you added to the engine earlier
     */
    public void removeAction(Action action) {
        actions.remove(action);
    }

    /**
     * Clears all placeholders in the engine instance
     */
    public void clearPlaceholders() {
        placeholders.clear();
    }

    /**
     * Clears all actions in the engine instance
     */
    public void clearActions() {
        actions.clear();
    }

    /**
     * Checks if the name of specified placeholder NOT equals to specified name
     *
     * @param placeholder The placeholder whose name you want to check
     * @param name        The name
     * @return {@code true} or {@code false}
     */
    public boolean placeholderNotEqual(IPlaceholder placeholder, String name) {
        if(!(placeholder instanceof MapPlaceholder) && !placeholder.getName().equals(name)) return true;
        return placeholder instanceof MapPlaceholder && !name.startsWith(placeholder.getName());
    }

    /**
     * Returns the value of specified placeholder and its data
     *
     * @param placeholder The instance of placeholder
     * @param name        The full name of placeholder (from {@link PlaceholderNode})
     * @param data        The data of placeholder (from {@link PlaceholderNode})
     * @return The value of the placeholder
     */
    public String getPlaceholderValue(@NotNull IPlaceholder placeholder, String name, String data) {
        return placeholder.getValue(placeholder instanceof MapPlaceholder ? (name + "\u007F" + data) : data);
    }

    /**
     * Processes specified content and returns result
     *
     * @param content The content you need to process
     * @return The result of processing
     */
    public String process(String content) {
        return process(parser.parse(content));
    }

    /**
     * Processes specified {@link RootNode} and returns result
     *
     * @param root The node you need to process
     * @return The result of processing
     * @see EpsilonEngine#process(String)
     */
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
                        if(placeholderNotEqual(placeholder, name)) return;

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
