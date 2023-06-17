package io.github.stngularity.epsilon.engine.placeholders;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Placeholder for work with maps.
 */
public class MapPlaceholder implements IPlaceholder {
    private final String name;
    private final Map<String, Object> map;

    /**
     * Placeholder for work with maps.
     * @param baseName The base name of the placeholder. For example, baseName=user;
     *                 placeholder=user.name; it is references to this placeholder.
     */
    public MapPlaceholder(String baseName) {
        this.name = baseName;
        this.map = new HashMap<>();
    }

    /**
     * Placeholder for work with maps.
     *
     * @param baseName The base name of the placeholder. For example, baseName=user;
     *                 placeholder=user.name; it is references to this placeholder.
     * @param map      The map with data of this placeholder
     */
    public MapPlaceholder(String baseName, Map<String, Object> map) {
        this.name = baseName;
        this.map = map;
    }

    /**
     * Puts specified value by specified key
     *
     * @param key   The key by which the value specified in the `value` parameter will be added.
     * @param value The value that You want to add
     */
    public void put(String key, Object value) {
        map.put(key, value);
    }

    /**
     * Removes from a map specified key and its value
     * @param key The key that You want to remove
     */
    public void remove(String key) {
        map.remove(key);
    }

    /**
     * Clears (removes all keys from) map
     */
    public void clear() {
        map.clear();
    }

    /**
     * Gets value of specified key
     *
     * @param key The key by which the value You want to get is stored.
     * @return The value to which the specified key is mapped, or {@code null} if the map contains
     *         no mapping for the key.
     */
    public Object get(String key) {
        return map.get(key);
    }

    /**
     * The size of the map in this placeholder
     * @return The amount of keys that this placeholder's map contains
     */
    public int size() {
        return map.size();
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
     * @param rawData The data that specified by user. If isn't specified, then provide `null`
     * @return The value of placeholder
     *
     * @throws IllegalArgumentException If there are any errors with the processing the default value.
     */
    @Override
    public String getValue(@NotNull String rawData) throws IllegalArgumentException {
        String name = rawData.split("\u007F")[0];

        String data = rawData.split("\u007F")[1];
        if(data.equals("null"))
            data = null;

        if(!name.equals(this.name)) {
            Object output = map;
            for(String key : name.replace(this.name, "").split("\\.")) {
                if(key.equals("")) continue;
                if(output == null) return null;

                if(output instanceof Map)
                    output = ((Map<?, ?>) output).get(key);
            }

            if(output instanceof IPlaceholder)
                return ((IPlaceholder) output).getValue(data);

            return String.valueOf(output);
        }

        Object rawValue = get("$default");
        if(rawValue == null)
            throw new IllegalArgumentException("The base key (name) is specified as full name and the placeholder doesn't have a default value in it");

        String value = String.valueOf(rawValue);
        if(!value.startsWith("$:"))
            return value;

        Object linkedValue = getValue(value.replace("$:", "") + "\u007Fnull");
        if(linkedValue == null)
            throw new IllegalArgumentException("The default value is a reference to a non-existent key");

        return String.valueOf(linkedValue);
    }
}
