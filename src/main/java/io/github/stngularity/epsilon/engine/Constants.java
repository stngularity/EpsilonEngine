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
            .name("template")
            .child("start", Pattern.compile("\\$"))
            .child("name", Pattern.compile("[a-zA-Z0-9]+"))
            .child("data", Pattern.compile("\\[([^]]+)]"))
            .child("end", Pattern.compile("\\$"))
            .build();

    /**
     * The default value of placeholder pattern
     */
    public static final EPattern DEFAULT_PLACEHOLDER_PATTERN = EPattern.builder()
            .name("placeholder")
            .child("start", Pattern.compile("\\{"))
            .child("name", Pattern.compile("[a-zA-Z0-9_\\-:.]+"))
            .child("data", Pattern.compile("\\[([^]]+)]"))
            .child("end", Pattern.compile("}"))
            .build();

    /**
     * The default value of action pattern
     */
    public static final EPattern DEFAULT_ACTION_PATTERN = EPattern.builder()
            .name("action")
            .child("start", Pattern.compile("\\["))
            .child("name", Pattern.compile("[a-zA-Z0-9_\\-:.]+"))
            .child("data", Pattern.compile(": *([^]]+)"))
            .child("end", Pattern.compile("]"))
            .build();
}
