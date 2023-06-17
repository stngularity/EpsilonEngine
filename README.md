## What it is?
`EpsilonEngine` is a text engine built for the `Epsilon` plugin (still in
development) and `PrimPlugin` (plugin for the server where I'm deputy). It
is needed for processing placeholders and convenient use `templates` (`if`
and `format`). Now it is only in development. But he already knows something

## Features
1. Miscellaneous placeholders (static, by time, and a map that can use 
   any type and even placeholders)
2. Line actions, so far only 1 (`ignoreLine`). But you can add own actions
3. Templates. Templates are checks (`if`/`elif`/`else`) and formatting
   (`format`). I will add more templates in the future.
4. ~~Parser for color and formatting codes. Will return a native implementation 
   of components that can be converted to Spigot or BungeeCord components~~
   (`Coming Soon`)

## How to use?
First, add Sonatype repository to your Maven project:
```xml
<repositories>
   <repository>
      <id>sonatype</id>
      <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
   </repository>
</repositories>
```

Second, add an engine to dependencies of your Maven project:
```xml
<dependencies>
   <dependency>
      <groupId>io.github.stngularity.epsilon</groupId>
      <artifactId>engine</artifactId>
      <version>0.1.0-SNAPSHOT</version>
      <scope>compile</scope>
   </dependency>
</dependencies>
```
> The version is marked `SNAPSHOT` because the engine is still under development

Third, use `EpsilonEngine` class for work with an engine (in example uses Bukkit API):

```java
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import io.github.stngularity.epsilon.engine.EpsilonEngine;
import io.github.stngularity.epsilon.engine.placeholders.Placeholder;

public class BeautifulPlugin extends JavaPlugin {
   @Override
   public void onEnable() {
      EpsilonEngine engine = new EpsilonEngine();  // You can specify custom patterns for any parts of engine in arguments
      engine.addPlaceholder(new Placeholder("plugin.name", getName()));
      Bukkit.getConsoleSender().sendMessage("Enabled {plugin.name}!");
   }
}
```

## Comment from me
> "Reinventing the wheel is fun and educational."
> — stngularity, 2023 year

## License
This project is completely open-source and distributed under the `MIT`
license. Read the [`LICENSE`](LICENSE) file for more information.

```
Made with ❤ and 🍵 by stngularity for everyone
```