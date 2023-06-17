package me.stngularity.epsilon.engine.nodes.templates;

import me.stngularity.epsilon.engine.EpsilonEngine;
import me.stngularity.epsilon.engine.formatter.Fonts;
import me.stngularity.epsilon.engine.formatter.LineFormatter;
import me.stngularity.epsilon.engine.nodes.RootNode;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatTemplateNode extends BaseTemplateNode {
    private static final Pattern keyValuePattern = Pattern.compile("([a-zA-Z0-9_\\-.]+)=[\"']?([^,\"'\\]]+)[\"']?");
    public FormatTemplateNode(String data, String content) {
        super("format", data, content);
    }

    @Override
    public RootNode processTemplate(EpsilonEngine engine) {
        Matcher matcher = keyValuePattern.matcher(getData());
        Map<String, String> keyValue = new HashMap<>();
        while(matcher.find())
            keyValue.put(matcher.group(1), matcher.group(2));

        int maxLength = Integer.parseInt(keyValue.getOrDefault("maxLength", "-1"));
        String format = keyValue.getOrDefault("format", "{original}");
        String font = keyValue.getOrDefault("font", "default");
        return engine.getParser().parse(LineFormatter.format(Fonts.fromFontToFont(getContent(),
                Fonts._default, Fonts.getFont(font)), maxLength, format, engine));
    }
}
