package io.github.stngularity.epsilon.engine;

import io.github.stngularity.epsilon.engine.nodes.*;
import io.github.stngularity.epsilon.engine.nodes.templates.BaseTemplateNode;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class for a parser of text content.
 */
public class Parser {
    private final static Pattern textPattern = Pattern.compile("(?:\u007F([0-9]+)\u007F)?([^\u007F]+)?");

    public final Pattern placeholderPattern;
    public final Pattern templateStartPattern;
    public final Pattern templateEndPattern;
    public final Pattern actionPattern;

    /**
     * The class for a parser of text content.
     *
     * @param placeholder   The format of placeholders
     * @param templateStart The format of templates start
     * @param templateEnd   The format of templates end
     * @param action        The format of actions
     */
    public Parser(Pattern placeholder, Pattern templateStart, Pattern templateEnd, Pattern action) {
        this.placeholderPattern = placeholder;
        this.templateStartPattern = templateStart;
        this.templateEndPattern = templateEnd;
        this.actionPattern = action;
    }

    /**
     * The class for a parser of text content
     * with default values of parameters.
     */
    public Parser() {
        this.placeholderPattern = Pattern.compile("\\{([a-zA-Z0-9_\\-:.]+)(?:\\[([^\\[]+)])?}");
        this.templateStartPattern = Pattern.compile("\\$([a-zA-Z0-9]+)(?:\\[([^]]+)])?\\$");
        this.templateEndPattern = Pattern.compile("\\$([a-zA-Z0-9]+)\\$");
        this.actionPattern = Pattern.compile("\\[([a-zA-Z0-9_\\-:.]+)(?:: *([^]]+))?]");
    }

    /**
     * Parses specified content and returns root node with parsed elements as children.
     *
     * @param content The content to processed and split into the nodes
     * @return Root node with elements as children
     *
     * @throws IllegalArgumentException If end of template doesn't match its beginning
     */
    public RootNode parse(@NotNull String content) throws IllegalArgumentException {
        RootNode root = new RootNode();
        for(String line : content.split("\n")) {
            LineNode lineNode = new LineNode();

            Matcher templates = Pattern.compile(templateStartPattern.pattern() + "(.*)" +
                    templateEndPattern.pattern()).matcher(line);

            while(templates.find()) {
                line = line.replace(templates.group(0), "\u007F" + lineNode.size() + "\u007F");

                String tName = templates.group(1);
                String tData = templates.group(2);
                String tContent = templates.group(3);
                String tEnd = templates.group(4);
                if(!("end" + tName).equals(tEnd.toLowerCase()))
                    throw new IllegalArgumentException("End of template doesn't match its beginning");

                lineNode.addChild(BaseTemplateNode.processAbstractTemplate(tName, tData, tContent));
            }

            Matcher placeholders = placeholderPattern.matcher(line);
            while(placeholders.find()) {
                line = line.replace(placeholders.group(0), "\u007F" + lineNode.size() + "\u007F");
                lineNode.addChild(new PlaceholderNode(placeholders.group(1), placeholders.group(2), placeholders.group(0)));
            }


            Matcher actions = actionPattern.matcher(line);
            while(actions.find()) {
                line = line.replace(actions.group(0), "\u007F" + lineNode.size() + "\u007F");
                lineNode.addChild(new ActionNode(actions.group(1), actions.group(2)));
            }

            int offset = 0;
            Matcher textMatcher = textPattern.matcher(line);
            while(textMatcher.find()) {
                int index = (textMatcher.group(1) == null ? 0 : Integer.parseInt(textMatcher.group(1))+1);
                String text = textMatcher.group(2);
                if(text == null)
                    continue;

                lineNode.insertChild(index+offset, new TextNode(text));
                offset++;
            }

            root.addChild(lineNode);
        }
        return root;
    }
}
