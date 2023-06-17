package me.stngularity.epsilon.engine.placeholders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Time placeholder.
 * If there is no data, the following format will be used: `dd.MM.yyyy HH:mm:ss`
 *
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html">Oracle docs for time formatter</a>
 */
public class TimePlaceholder implements IPlaceholder {
    private final String name;
    private final LocalDateTime time;

    /**
     * Time placeholder
     * @param time The time to use as a placeholder value
     */
    public TimePlaceholder(LocalDateTime time) {
        this.name = "time";
        this.time = time;
    }

    /**
     * Time placeholder
     * @param name The name of the placeholder. By default, `time`
     * @param time The time to use as a placeholder value
     */
    public TimePlaceholder(String name, LocalDateTime time) {
        this.name = name;
        this.time = time;
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
        return time.format(DateTimeFormatter.ofPattern(data == null ? "dd.MM.yyyy HH:mm:ss" : data));
    }
}
