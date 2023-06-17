package me.stngularity.epsilon.engine.actions;

/**
 * Action to ignore the line it is on
 */
public class IgnoreLineAction extends Action {
    protected IgnoreLineAction() {
        super("ignoreLine");
    }

    /**
     * Processes specified line and returns new line
     *
     * @param line The original content of the string
     * @param data The data of action, if any
     * @return New line formed from the specified line
     */
    @Override
    public String processLine(String line, String data) {
        return null;
    }
}
