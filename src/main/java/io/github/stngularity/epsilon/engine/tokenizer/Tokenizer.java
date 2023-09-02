package io.github.stngularity.epsilon.engine.tokenizer;

import io.github.stngularity.epsilon.engine.EPattern;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    public static @NotNull List<Token> tokenize(@NotNull String input, List<EPattern> patterns) {
        List<Token> tokens = new ArrayList<>();

        StringBuilder buffer = new StringBuilder();
        Token current = null;
        for(int i = 0; i < input.length(); i++) {
            buffer.append(input.charAt(i));

            for(EPattern pattern : patterns) {
                if(current != null && !current.type.equals(pattern.getName()))
                    continue;

                if(current == null && match(buffer.toString(), pattern, "start")) {
                    current = new Token(pattern.getName(), String.valueOf(input.charAt(i)));

                    buffer.deleteCharAt(buffer.length()-1);
                    if(buffer.length() > 0)
                        tokens.add(new Token("text", buffer.toString()));

                    buffer = new StringBuilder();
                    break;
                }

                if(current != null && match(buffer.toString(), pattern, "end")) {
                    current.data += buffer.toString();
                    tokens.add(current);

                    current = null;
                    buffer = new StringBuilder();
                }
            }
        }

        return tokens;
    }

    /**
     * Checks if content is matches with specified via parent and child path pattern
     *
     * @param content String content to check
     * @param parent  Parent pattern
     * @param child   Path to pattern child
     * @return {@code true}, if matches, else {@code false}
     */
    private static boolean match(String content, @NotNull EPattern parent, String @NotNull ... child) {
        EPattern pattern = parent;
        for(String element : child)
            pattern = pattern.get(element);

        return pattern.getRegex().matcher(content).find();
    }
}
