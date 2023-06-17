package io.github.stngularity.epsilon.engine.placeholders;

/**
 * Basic placeholder
 */
public class Placeholder implements IPlaceholder {
    private final String name;
    private final String value;

    /**
     * Basic placeholder
     *
     * @param name  The name of the placeholder
     * @param value The value of the placeholder
     */
    public Placeholder(String name, Object value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    /**
     * Gets the name of placeholder
     * @return Name of placeholder
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the value for placeholder by specified data
     *
     * @param data The data that specified by user. If isn't specified, then provide `null`
     * @return The value of placeholder
     */
    @Override
    public String getValue(String data) {
        return value;
    }
}
