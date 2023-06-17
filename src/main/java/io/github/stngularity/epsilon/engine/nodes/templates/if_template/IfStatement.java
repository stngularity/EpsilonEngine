package io.github.stngularity.epsilon.engine.nodes.templates.if_template;

public class IfStatement {
    private final String statement;
    private final String object1;
    private final String condition;
    private final String object2;
    private final String content;

    public IfStatement(String statement, String object1, String condition, String object2, String content) {
        this.statement = statement;
        this.object1 = object1;
        this.condition = condition;
        this.object2 = object2;
        this.content = content;
    }

    public String getStatement() {
        return statement;
    }

    public String getObject1() {
        return object1;
    }

    public String getCondition() {
        return condition;
    }

    public String getObject2() {
        return object2;
    }

    public String getContent() {
        return content;
    }
}
