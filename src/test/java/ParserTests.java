import io.github.stngularity.epsilon.engine.Constants;
import io.github.stngularity.epsilon.engine.EPattern;
import io.github.stngularity.epsilon.engine.parser.Parser;
import io.github.stngularity.epsilon.engine.parser.RawNode;
import io.github.stngularity.epsilon.engine.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParserTests {
    @Test
    public void testMultilineParse() {
        String input = "Hello! I'm {name} and I was born {birthday[LLL dd, yyyy year]}. I am {job}.\n$if[job == \"developer\"]$And I know {planguages}.$else$[ignoreLine]$endif$";

        List<EPattern> patterns = new ArrayList<>();
        patterns.add(Constants.DEFAULT_TEMPLATE_PATTERN);
        patterns.add(Constants.DEFAULT_PLACEHOLDER_PATTERN);
        patterns.add(Constants.DEFAULT_ACTION_PATTERN);

        Parser parser = new Parser();
        List<RawNode> nodes = parser.parse(Tokenizer.tokenize(input, patterns));
        nodes.forEach(node -> print(node, 0));
    }

    private void print(@NotNull RawNode node, int indent) {
        String value = (node.value == null ? "null" : "'" + node.value + "'").replace("\n", "\\n");
        System.out.printf("%s[token] name=%s; value=%s; original='%s'%n", " ".repeat(indent),
                node.name, value, node.original.replace("\n", "\\n"));

        node.children.forEach(t -> print(t, indent+2));
    }
}
