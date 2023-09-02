package io.github.stngularity.epsilon.engine.parser;

import io.github.stngularity.epsilon.engine.EPattern;
import io.github.stngularity.epsilon.engine.Constants;
import io.github.stngularity.epsilon.engine.tokenizer.Token;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * The class for a parser of text content.
 */
public class Parser {
    public final EPattern template;
    public final EPattern placeholder;
    public final EPattern action;

    /**
     * The class for a parser of text content.
     *
     * @param template    The format of templates
     * @param placeholder The format of placeholders
     * @param action      The format of actions
     */
    public Parser(EPattern template, EPattern placeholder, EPattern action) {
        this.template = template;
        this.placeholder = placeholder;
        this.action = action;
    }

    /**
     * The class for a parser of text content
     * with default values of parameters.
     */
    public Parser() {
        this.template = Constants.DEFAULT_TEMPLATE_PATTERN;
        this.placeholder = Constants.DEFAULT_PLACEHOLDER_PATTERN;
        this.action = Constants.DEFAULT_ACTION_PATTERN;
    }

    /**
     * Parses specified tokens.
     *
     * @param tokens List of {@link Token} from tokenizer
     * @return List with {@link RawNode}s
     *
     * @throws IllegalArgumentException If found invalid token
     */
    public List<RawNode> parse(@NotNull List<Token> tokens) throws IllegalArgumentException {
        List<RawNode> nodes = new ArrayList<>();

        RawNode globalTemplate = null;
        RawNode template = null;
        RawNode tReturn = null;
        for(Token token : tokens) {
            if(token.type.equals("text"))
                (tReturn == null ? nodes : tReturn.children).add(new RawNode("text", token.data, ""));

            if(token.type.equals("placeholder"))
                (tReturn == null ? nodes : tReturn.children).add(parsePlaceholder(token.data));

            if(token.type.equals("action"))
                (tReturn == null ? nodes : tReturn.children).add(parseAction(token.data));

            if(!token.type.equals("template"))
                continue;

            if(tReturn != null)
                template.children.add(tReturn);

            globalTemplate = (globalTemplate == null ? new RawNode("template", "") : globalTemplate);
            RawNode node = parseTemplate(token.data);

            String name = node.get("template.name").value;
            if(template != null && name != null && name.startsWith("end")) {
                globalTemplate.children.add(template);
                nodes.add(globalTemplate);

                globalTemplate = null;
                template = null;
            }

            if(template != null)
                globalTemplate.children.add(template);

            template = node;
            tReturn = new RawNode("template.return", "");
        }

        return nodes;
    }

    public RawNode parseTemplate(String data) {
        RawNode node = new RawNode("template", "");
        Matcher nameMatcher = template.get("name").getRegex().matcher(data);
        if(!nameMatcher.find())
            throw new IllegalArgumentException("Failed to parse template token: name is required");

        node.children.add(new RawNode("template.name", nameMatcher.group(), nameMatcher.group()));

        Matcher dataMatcher = template.get("data").getRegex().matcher(data);
        if(dataMatcher.find())
            node.children.add(new RawNode("template.data", dataMatcher.group(1), ""));

        return node;
    }

    public RawNode parsePlaceholder(String data) throws IllegalArgumentException {
        RawNode node = new RawNode("placeholder", data);
        Matcher nameMatcher = placeholder.get("name").getRegex().matcher(data);
        if(!nameMatcher.find())
            throw new IllegalArgumentException("Failed to parse placeholder token: name is required");

        node.children.add(new RawNode("placeholder.name", nameMatcher.group(), nameMatcher.group()));

        Matcher dataMatcher = placeholder.get("data").getRegex().matcher(data);
        if(dataMatcher.find())
            node.children.add(new RawNode("placeholder.data", dataMatcher.group(1), ""));

        return node;
    }

    public RawNode parseAction(String data) throws IllegalArgumentException {
        RawNode node = new RawNode("action", data);
        Matcher nameMatcher = action.get("name").getRegex().matcher(data);
        if(!nameMatcher.find())
            throw new IllegalArgumentException("Failed to parse action token: name is required");

        node.children.add(new RawNode("action.name", nameMatcher.group(), nameMatcher.group()));

        Matcher dataMatcher = action.get("data").getRegex().matcher(data);
        if(dataMatcher.find())
            node.children.add(new RawNode("action.data", dataMatcher.group(1), ""));

        return node;
    }
}
