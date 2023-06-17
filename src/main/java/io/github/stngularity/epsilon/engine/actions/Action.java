package me.stngularity.epsilon.engine.actions;

/**
 * The base class for actions
 */
public abstract class Action {
    private final String name;

    protected Action(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the action
     * @return Name of the action
     */
    public String getName() {
        return name;
    }

    /**
     * Processes specified line and returns new line
     *
     * @param line The original content of the string
     * @param data The data of action, if any
     * @return New line formed from the specified line
     */
    public abstract String processLine(String line, String data);
}
