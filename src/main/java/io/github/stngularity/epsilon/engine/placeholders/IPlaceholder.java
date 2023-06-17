package io.github.stngularity.epsilon.engine.placeholders;

/**
 * The base class for placeholders
 */
public interface IPlaceholder {
    /**
     * Gets the name of placeholder
     * @return Name of placeholder
     */
    String getName();

    /**
     * Gets the value for placeholder by specified data
     *
     * @param data The data that specified by user. If isn't specified, then provide `null`
     * @return The value of placeholder
     */
    String getValue(String data);
}
