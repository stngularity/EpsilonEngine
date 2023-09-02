import io.github.stngularity.epsilon.engine.parser.EPattern;
import io.github.stngularity.epsilon.engine.parser.ParserConstants;
import io.github.stngularity.epsilon.engine.tokenizer.Token;
import io.github.stngularity.epsilon.engine.tokenizer.Tokenizer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokenizerTests {
    @Test
    public void testTokenizer() {
        List<EPattern> patterns = new ArrayList<>();
        patterns.add(ParserConstants.DEFAULT_TEMPLATE_PATTERN);
        patterns.add(ParserConstants.DEFAULT_PLACEHOLDER_PATTERN);
        patterns.add(ParserConstants.DEFAULT_ACTION_PATTERN);

        String input = "Hello! I'm {name} and I was born {birthday[LLL dd, yyyy]}. I am {job}.\n$if[job == \"developer\"]$And I know {planguages}.$else$[ignoreLine]$endif$";
        List<Token> tokens = Tokenizer.tokenize(input, patterns);
        tokens.forEach(token -> System.out.printf("[%s: \"%s\"]%n", token.type, token.data.replace("\n", "\\n")));
    }
}
