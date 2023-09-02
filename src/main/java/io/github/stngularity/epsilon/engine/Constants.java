package io.github.stngularity.epsilon.engine;

import java.util.regex.Pattern;

/**
 * Class for parser's constants
 */
public class Constants {
    /**
     * The default value of template pattern
     */
    public static final EPattern DEFAULT_TEMPLATE_PATTERN = EPattern.builder()
            .child("start", Pattern.compile("\\$"))
            .child("name", Pattern.compile("[a-zA-Z0-9]+"))
            .child("data",
                    EPattern.builder().name("start").regex(Pattern.compile("\\[")).build(),
                    EPattern.builder().name("value").regex(Pattern.compile("[^]]+"))
                            .child("escape", Pattern.compile("\\\\.*")).build(),
                    EPattern.builder().name("end").regex(Pattern.compile("]")).build())
            .child("end", Pattern.compile("\\$"))
            .build();

    /**
     * The default value of placeholder pattern
     */
    public static final EPattern DEFAULT_PLACEHOLDER_PATTERN = EPattern.builder()
            .child("start", Pattern.compile("\\{"))
            .child("name", Pattern.compile("[a-zA-Z0-9_\\-:.]+"))
            .child("data",
                    EPattern.builder().name("start").regex(Pattern.compile("\\[")).build(),
                    EPattern.builder().name("value").regex(Pattern.compile("[^]]+"))
                            .child("escape", Pattern.compile("\\\\.*")).build(),
                    EPattern.builder().name("end").regex(Pattern.compile("]")).build())
            .child("end", Pattern.compile("}"))
            .build();

    /**
     * The default value of action pattern
     */
    public static final EPattern DEFAULT_ACTION_PATTERN = EPattern.builder()
            .child("start", Pattern.compile("\\["))
            .child("name", Pattern.compile("[a-zA-Z0-9_\\-:.]+"))
            .child("data",
                    EPattern.builder().name("start").regex(Pattern.compile(": *")).build(),
                    EPattern.builder().name("value").regex(Pattern.compile("[^]]+"))
                            .child("escape", Pattern.compile("\\\\.*")).build(),
                    EPattern.builder().name("end").regex(Pattern.compile("")).build())
            .child("end", Pattern.compile("]"))
            .build();
}
