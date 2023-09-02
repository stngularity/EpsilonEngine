package io.github.stngularity.epsilon.engine;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The class for Engine patterns
 */
public class EPattern {
    private final @NotNull String name;
    private final @Nullable Pattern regex;
    private final @NotNull List<EPattern> children;
    private final boolean optional;

    /**
     * Initializes the pattern
     *
     * @param name     Pattern name
     * @param regex    Pattern regex
     * @param children Pattern children list
     * @param optional Whether pattern is optional
     */
    private EPattern(@NotNull String name, @Nullable Pattern regex, @NotNull List<EPattern> children, boolean optional) {
        this.name = name;
        this.regex = regex;
        this.children = children;
        this.optional = optional;
    }

    /**
     * Returns the name of pattern (e.g., placeholder)
     * @return Pattern name
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Returns regex of pattern if that exists, else throws {@link IllegalArgumentException}
     *
     * @return Pattern regex
     * @throws IllegalArgumentException If pattern's regex is {@code null}
     */
    public @NotNull Pattern getRegex() throws IllegalArgumentException {
        if(regex == null)
            throw new IllegalArgumentException("Pattern " + name + " requires a regex");

        return regex;
    }

    /**
     * Returns list of pattern's children
     * @return List of {@link EPattern} instances
     */
    public @NotNull List<EPattern> getChildren() {
        return children;
    }

    /**
     * Returns {@link EPattern} with specified name
     *
     * @param name The name of pattern that you like to get
     * @return {@link EPattern}
     * @throws IllegalArgumentException If pattern with specified name doesn't exist
     */
    public @NotNull EPattern get(String name) throws IllegalArgumentException {
        for(EPattern child : children) {
            if(!child.name.equals(name)) continue;
            return child;
        }

        throw new IllegalArgumentException("Pattern with \"" + name + "\" name not found");
    }

    /**
     * Whether pattern is optional
     * @return {@code true} or {@code false}
     */
    public boolean isOptional() {
        return optional;
    }

    /**
     * Creates the builder instance
     * @return {@link EnginePatternBuilder}
     */
    @Contract(" -> new")
    public static @NotNull EnginePatternBuilder builder() {
        return new EnginePatternBuilder();
    }

    /**
     * The class of Engine patterns builder
     */
    public static class EnginePatternBuilder {
        private String name;
        private @Nullable Pattern regex = null;
        private final @NotNull List<EPattern> children = new ArrayList<>();
        private boolean optional = false;

        /**
         * Sets the name of pattern
         *
         * @param name The new name for pattern
         * @return {@link EnginePatternBuilder}
         */
        public EnginePatternBuilder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the regex of pattern
         *
         * @param regex The new regex for pattern
         * @return {@link EnginePatternBuilder}
         */
        public EnginePatternBuilder regex(@Nullable Pattern regex) {
            this.regex = regex;
            return this;
        }

        /**
         * Adds child to pattern
         *
         * @param child The pattern instance
         * @return {@link EnginePatternBuilder}
         */
        public EnginePatternBuilder child(EPattern child) {
            children.add(child);
            return this;
        }

        /**
         * Adds child builder to pattern
         *
         * @param child The pattern builder instance
         * @return {@link EnginePatternBuilder}
         */
        public EnginePatternBuilder child(@NotNull EnginePatternBuilder child) {
            children.add(child.build());
            return this;
        }

        /**
         * Creates new child using specified parameters and adds it to pattern
         *
         * @param name     The name of child pattern
         * @param regex    The regex of child pattern
         * @param optional Whether pattern is optional
         * @param children List of pattern children
         * @return @link EnginePatternBuilder}
         */
        public EnginePatternBuilder child(String name, Pattern regex, boolean optional, EPattern @NotNull ... children) {
            this.children.add(new EPattern(name, regex, List.of(children), optional));
            return this;
        }

        /**
         * Creates new child using specified parameters and adds it to pattern
         *
         * @param name     The name of child pattern
         * @param optional Whether pattern is optional
         * @param children List of pattern children
         * @return @link EnginePatternBuilder}
         */
        public EnginePatternBuilder child(String name, boolean optional, EPattern @NotNull ... children) {
            this.children.add(new EPattern(name, null, List.of(children), optional));
            return this;
        }

        /**
         * Creates new child using specified parameters and adds it to pattern
         *
         * @param name     The name of child pattern
         * @param children List of pattern children
         * @return @link EnginePatternBuilder}
         */
        public EnginePatternBuilder child(String name, EPattern @NotNull ... children) {
            this.children.add(new EPattern(name, null, List.of(children), false));
            return this;
        }

        /**
         * Creates new child using specified parameters and adds it to pattern
         *
         * @param name     The name of child pattern
         * @param regex    The regex of child pattern
         * @param children List of pattern children
         * @return @link EnginePatternBuilder}
         */
        public EnginePatternBuilder child(String name, Pattern regex, EPattern @NotNull ... children) {
            this.children.add(new EPattern(name, regex, List.of(children), false));
            return this;
        }

        /**
         * Sets the optionality of pattern
         *
         * @param optional The new optionality value
         * @return {@link EnginePatternBuilder}
         */
        public EnginePatternBuilder optional(boolean optional) {
            this.optional = optional;
            return this;
        }

        /**
         * Builds pattern
         * @return {@link EPattern}
         */
        public EPattern build() {
            if(name == null)
                throw new IllegalArgumentException("Specify the name of pattern");

            return new EPattern(name, regex, children, optional);
        }
    }
}
