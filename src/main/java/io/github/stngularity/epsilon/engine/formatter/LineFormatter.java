package me.stngularity.epsilon.engine.formatter;

import me.stngularity.epsilon.engine.EpsilonEngine;
import me.stngularity.epsilon.engine.placeholders.Placeholder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Line formatter
 */
public class LineFormatter {
    /**
     * Formats line with specified settings
     *
     * @param original The original text that You want to format
     * @param length   The maximum length of text. Text that goes beyond these
     *                 limits will be wrapped to a new line. Specify -1 to remove
     *                 the limit
     * @param format   The format of output text
     * @param engine   The object of Epsilon Engine
     *
     * @return Formatted string
     */
    public static @NotNull String format(String original, int length, String format, @NotNull EpsilonEngine engine) {
        StringBuilder output = new StringBuilder();
        splitByLength(engine.process(original), length).forEach(line -> {
            engine.addPlaceholder(new Placeholder("original", line.strip()));
            output.append(engine.process(format));
            engine.removePlaceholder(engine.getPlaceholders().size()-1);
            output.append("\n");
        });

        return output.deleteCharAt(output.length()-1).toString();
    }

    public static @NotNull List<String> splitByLength(@NotNull String original, int length) {
        if(length == -1) return List.of(original);
        return Arrays.asList(original.split("(?<=\\G.{" + length + "})"));
    }
}
