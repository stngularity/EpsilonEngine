import io.github.stngularity.epsilon.engine.Constants;
import io.github.stngularity.epsilon.engine.EPattern;
import io.github.stngularity.epsilon.engine.ast.*;
import io.github.stngularity.epsilon.engine.parser.Parser;
import io.github.stngularity.epsilon.engine.tokenizer.Tokenizer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ASTTests {
    @Test
    public void testProcess() {
        String input = "Hello! I'm {name} and I was born {birthday[LLL dd, yyyy year]}. I am {job}.\n$if[job == \"developer\"]$And I know {planguages}.$else$[ignoreLine]$endif$";

        List<EPattern> patterns = new ArrayList<>();
        patterns.add(Constants.DEFAULT_TEMPLATE_PATTERN);
        patterns.add(Constants.DEFAULT_PLACEHOLDER_PATTERN);
        patterns.add(Constants.DEFAULT_ACTION_PATTERN);

        Parser parser = new Parser();
        RootNode node = AbstractSyntaxTree.process(parser.parse(Tokenizer.tokenize(input, patterns)));
        printNode(node, 0);
    }

    public void printNode(BaseNode node, int indent) {
        if(node instanceof RootNode)
            System.out.printf("%s[root]%n", " ".repeat(indent));

        if(node instanceof TemplateNode) {
            System.out.printf("%s[template] %s; data='%s'%n", " ".repeat(indent), node.name,
                    ((TemplateNode) node).data);

            if(!node.name.equals("null"))
                System.out.printf("%s[return]%n", " ".repeat(indent+2));

            ((TemplateNode) node).templateReturn.forEach(ret -> printNode(ret, indent
                    +(node.name.equals("null") ? 2 : 4)));
        }

        if(node instanceof PlaceholderNode && !(node instanceof ActionNode)) {
            System.out.printf("%s[placeholder] %s; data='%s'%n", " ".repeat(indent), node.name,
                    ((PlaceholderNode) node).data);
        }

        if(node instanceof ActionNode) {
            System.out.printf("%s[action] %s; data='%s'%n", " ".repeat(indent), node.name,
                    ((ActionNode) node).data);
        }

        if(node instanceof TextNode)
            System.out.printf("%s[text] '%s'%n", " ".repeat(indent), ((TextNode) node).value.replace("\n", "\\n"));

        node.children.forEach(child -> printNode(child, indent+2));
    }
}
