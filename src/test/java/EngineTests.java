import me.stngularity.epsilon.engine.EpsilonEngine;
import me.stngularity.epsilon.engine.placeholders.MapPlaceholder;
import me.stngularity.epsilon.engine.placeholders.Placeholder;
import me.stngularity.epsilon.engine.placeholders.TimePlaceholder;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

public class EngineTests {
    @Test
    public void testEngine() {
        EpsilonEngine engine = new EpsilonEngine();
        engine.addPlaceholder(new Placeholder("greetings", "hello"));
        engine.addPlaceholder(new Placeholder("user", "stngularity"));
        engine.addPlaceholder(new TimePlaceholder(LocalDateTime.now()));

        String text = "This is just example\n{greetings}, {user}\nNow {time[dd.MM.yyyy]}\n$if[user.isAdmin]$You're admin!$endif$";
        System.out.println(engine.process(text));
    }

    @Test
    public void testMapPlaceholder() {
        MapPlaceholder placeholder = new MapPlaceholder("user");
        placeholder.put("$default", "$:name");
        placeholder.put("name", "stngularity");
        placeholder.put("id", 1);
        placeholder.put("registered", new TimePlaceholder(LocalDateTime.now()));

        EpsilonEngine engine = new EpsilonEngine();
        engine.addPlaceholder(placeholder);

        String text = "Hi, {user}!\nName: {user.name}\nID: {user.id}\nRegistered at {user.registered[dd.MM.yyyy HH:mm]}\n{user.invalidKey}";
        System.out.println(engine.process(text));
    }

    @Test
    public void testActions() {
        EpsilonEngine engine = new EpsilonEngine();
        System.out.println(engine.process("I'm simple line\n[ignoreLine] I'm ignored!\nI'm 3 line"));
    }

    @Test
    public void testTemplates() {
        List.of("owner", "admin", "moder", "unknown").forEach(rank -> {
            EpsilonEngine engine = new EpsilonEngine();
            engine.addPlaceholder(new Placeholder("user.rank", rank));
            System.out.println(engine.process("You're $if[user.rank == 'owner']$owner$elif[user.rank == 'admin']$admin$elif[user.rank == 'moder']$moder$else$user$endif$!"));
        });

        EpsilonEngine engine = new EpsilonEngine();
        engine.addPlaceholder(new Placeholder("user.rank", "admin"));
        System.out.println(engine.process("\nHello!\nYou're admin$if[user.rank != 'admin']$[ignoreLine]$endif$"));
    }

    @Test
    public void testFormatTemplate() {
        EpsilonEngine engine = new EpsilonEngine();
        System.out.println(engine.process("Font:\n$format[font=smallcaps]$I'm smallcaps font$endFormat$"));
        System.out.println();
        System.out.println(engine.process("Font & format:\n$format[font=smallcaps,format='  {original}  ']$I'm smallcaps font$endFormat$"));
        System.out.println();
        System.out.println(engine.process("Max length (14):\n$format[maxLength=14]$Hello, hello, hello, hello, hello, hello, hello$endFormat$"));
        System.out.println();
        System.out.println(engine.process("Max length (14) & format:\n$format[maxLength=14,format='  {original}  ']$Hello, hello, hello, hello, hello, hello, hello$endFormat$"));
        engine.addPlaceholder(new Placeholder("text", "Hello, hello, hello, hello, hello, hello, hello"));
        System.out.println();
        System.out.println(engine.process("Max length (14) & format & placeholder:\n$format[maxLength=14,format=''  {original}  '']${text}$endFormat$"));
    }
}
