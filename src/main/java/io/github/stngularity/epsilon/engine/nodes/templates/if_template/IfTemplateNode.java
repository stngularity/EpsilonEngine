package io.github.stngularity.epsilon.engine.nodes.templates.if_template;

import io.github.stngularity.epsilon.engine.EpsilonEngine;
import io.github.stngularity.epsilon.engine.nodes.RootNode;
import io.github.stngularity.epsilon.engine.nodes.templates.BaseTemplateNode;
import io.github.stngularity.epsilon.engine.placeholders.IPlaceholder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IfTemplateNode extends BaseTemplateNode {
    private static final Pattern conditionPattern = Pattern.compile("([^!=]+)([!=]=?)?(.*)");
    private static final Pattern valuePattern = Pattern.compile("[\"']([^\"']+)[\"']|[0-9]+|true|false|null");

    public IfTemplateNode(String data, String content) {
        super("if", data, content);
    }

    @Override
    public RootNode processTemplate(EpsilonEngine engine) throws IllegalArgumentException {
        AtomicReference<RootNode> output = new AtomicReference<>(new RootNode());
        Matcher matcher = conditionPattern.matcher(getData());
        if(!matcher.matches())
            throw new IllegalArgumentException("Condition specified in the template data is invalid");

        processStatements(engine).forEach(statement -> {
            if(statement.getCondition() == null && !statement.getObject2().equals(""))
                throw new IllegalArgumentException("After the condition, there must be a second object");

            if(output.get().size() > 0)
                return;

            if(statement.getStatement().equals("else")) {
                output.set(engine.getParser().parse(statement.getContent()));
                return;
            }

            String object1 = parseObject(engine, statement.getObject1());
            if(statement.getCondition() == null && statement.getObject2().equals("") &&
                    !(object1.equals("false") || object1.equals("null"))) {
                output.set(engine.getParser().parse(statement.getContent()));
                return;
            }

            if(statement.getCondition() == null && statement.getObject2().equals("") &&
                    object1.equals("false") || object1.equals("null"))
                return;

            String condition = statement.getCondition();
            if(!condition.equals("==") && !condition.equals("!="))
                throw new IllegalArgumentException("Condition \"" + condition + "\" is unsupported");

            String object2 = parseObject(engine, statement.getObject2());
            if((condition.equals("==") && object1.equals(object2))
                || (condition.equals("!=") && !object1.equals(object2)))
                output.set(engine.getParser().parse(statement.getContent()));
        });

        return output.get();
    }

    private String parseObject(EpsilonEngine engine, String original) {
        Matcher matcher = valuePattern.matcher(original);
        if(matcher.matches())
            return matcher.group(1);

        for(IPlaceholder placeholder : engine.getPlaceholders()) {
            if(engine.placeholderNotEqual(placeholder, original)) continue;
            return engine.getPlaceholderValue(placeholder, original, "");
        }

        return original;
    }

    @Contract(pure = true)
    private @NotNull List<IfStatement> processStatements(@NotNull EpsilonEngine engine) throws IllegalArgumentException {
        List<IfStatement> output = new ArrayList<>();
        String oContent = getContent();
        Matcher matcher = Pattern.compile(engine.getParser().templateStartPattern.pattern()
                + "([^$]*)").matcher(oContent);

        while(matcher.find()) {
            String statement = matcher.group(1);
            if(!statement.equals("elif") && !statement.equals("else"))
                throw new IllegalArgumentException("Unsupported statement in if template");

            String content = matcher.group(3);

            String rawData = matcher.group(2);
            if(rawData != null) {
                Matcher data = conditionPattern.matcher(rawData);
                if(!data.matches())
                    continue;

                output.add(new IfStatement(statement, data.group(1).strip(), data.group(2),
                        data.group(3).strip(), content));

                oContent = oContent.replace(matcher.group(0), "");
                continue;
            }

            output.add(new IfStatement(statement, null, null, "", content));
            oContent = oContent.replace(matcher.group(0), "");
        }

        Matcher iMatcher = conditionPattern.matcher(getData());
        if(iMatcher.matches())
            output.add(0, new IfStatement("if", iMatcher.group(1).strip(), iMatcher.group(2),
                    iMatcher.group(3).strip(), oContent));

        return output;
    }
}
