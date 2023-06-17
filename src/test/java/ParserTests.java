import me.stngularity.epsilon.engine.Parser;
import me.stngularity.epsilon.engine.nodes.*;
import me.stngularity.epsilon.engine.nodes.templates.BaseTemplateNode;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class ParserTests {
    @Test
    public void testMultilineParse() {
        Parser parser = new Parser();
        RootNode root = parser.parse("This is just example\n{greetings}, {user}\nNow {time[dd.MM.yyyy]}\n$if[user.isAdmin]$You're admin!$endif$");
        testMultilineParse_printNode(root, 0);
    }

    private void testMultilineParse_printNode(@NotNull BaseNode node, int indent) {
        String information = " ".repeat(indent) + "NODE " + node.getName();
        if(node instanceof TextNode) {
            String content = ((TextNode) node).getContent();
            information += " [" + (content == null ? "null" : content.replace("\n", "\\n")) + "]";
        }

        if(node instanceof PlaceholderNode)
            information += " [name=" + ((PlaceholderNode) node).getPlaceholderName() + ", data=" + ((PlaceholderNode) node).getPlaceholderData() + "]";

        if(node instanceof ActionNode)
            information += " [name=" + ((ActionNode) node).getActionName() + ", data=" + ((ActionNode) node).getActionData() + "]";

        if(node instanceof BaseTemplateNode)
            information += " [name=" + ((BaseTemplateNode) node).getTemplateName() + ", content=" + ((BaseTemplateNode) node).getContent() + ", data=" + ((BaseTemplateNode) node).getData() + "]";

        System.out.println(information);
        node.getChildren().forEach(child -> testMultilineParse_printNode(child, indent+2));
    }
}
