[ ![Download](https://api.bintray.com/packages/mrivanplaysbg/mrivanplays/ICF/images/download.svg) ](https://bintray.com/mrivanplaysbg/mrivanplays/ICF/_latestVersion)
# ICF 
Ivan's command framework, providing custom argument handling. 
Requires Java 8.

# I'm using java 7
Get off your dinosaur and get on this rocket ship!

On a serious note, Java 7 is END OF LIFE and it is unsafe to use. Go download java 8.

# Set me up
I will show you how to make it with maven.

Install the dependency and the repository:
```html
  <repositories>
    <repository>
      <id>spigotmc-repo</id>
      <url>https://hub.spigotmc.org/nexus/content/groups/public/</url>
    </repository>
    <repository>
      <id>sonatype</id>
      <url>https://oss.sonatype.org/content/groups/public/</url>
    </repository>
    <repository>
      <id>mrivanplays</id>
      <url>https://dl.bintray.com/mrivanplaysbg/mrivanplays/</url>
    </repository>
  </repositories>

  <dependencies>
    <dependency>
      <groupId>org.spigotmc</groupId>
      <artifactId>spigot-api</artifactId>
      <version>1.14.4-R0.1-SNAPSHOT</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>com.mrivanplays</groupId>
      <artifactId>icf</artifactId>
      <version>VERSION</version> <!-- Replace with latest -->
      <scope>compile</scope>
    </dependency>
  </dependencies>
```
If it says to you that it can't find the version, don't believe it. It is working perfectly

Then you should relocate the dependency
```html
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.1.0</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
            <configuration>
              <createDependencyReducedPom>false</createDependencyReducedPom>
              <relocations>
                <relocation>
                  <pattern>com.mrivanplays.icf</pattern>
                  <shadedPattern>[YOUR_PACKAGE].icf</shadedPattern>
                </relocation>
              </relocations>
            </configuration>
          </execution>
        </executions>
      </plugin>
```

# Usage
First you will need a command manager
```java
public void setupCommands() {
  CommandManager commandManager = new CommandManager(this /* PLUGIN */ );
}
```

Then to create a command you need to make a class and extend that class with `ICFCommand`
```java
public class CommandTestPlaceholders extends ICFCommand {

  @Override
  public void execute(CommandSender sender, String label, CommandArguments args) {
    
  }
}
```

We want to get the 1st argument? Or any other argument? No problem!
```java
public class CommandTestPlaceholders extends ICFCommand {

  @Override
  public void execute(CommandSender sender, String label, CommandArguments args) {
    args.next(ArgumentResolvers.STRING /* ARGUMENT RESOLVER HERE */)
      .ifPresent(sender::sendMessage)
      .orElse(failReason -> sender.sendMessage("You need to have atleast 1 argument!"));
  }
}
```

We want a permission or to force the command to be player only? NO PROBLEM
```java
public class CommandTestPlaceholders extends ICFCommand {

  public CommandTestPlaceholders() {
    super(true, "my.permission");
  }

  @Override
  public void execute(CommandSender sender, String label, CommandArguments args) {
    Player player = (Player) sender;
    args.next(ArgumentResolvers.STRING /* ARGUMENT RESOLVER HERE */)
      .ifPresent(player::sendMessage)
      .orElse(failReason -> player.sendMessage("You need to have atleast 1 argument!"));
  }
}
```

We want a message from argument? no problem
```java
public class CommandTestPlaceholders extends ICFCommand {

  public CommandTestPlaceholders() {
    super(true, "my.permission");
  }

  @Override
  public void execute(CommandSender sender, String label, CommandArguments args) {
    Player player = (Player) sender;
    String message = args.joinArgumentsSpace(0); // 0 is the starting argument
    player.sendMessage(message);
  }
}
```

We want to tab complete the command? No problem. You just have to extend the class with our TabCompleter
```java
public class CommandTestPlaceholders extends ICFCommand implements TabCompleter {

  public CommandTestPlaceholders() {
    super(true, "my.permission");
  }

  @Override
  public void execute(CommandSender sender, String label, CommandArguments args) {
    Player player = (Player) sender;
    String message = args.joinArgumentsSpace(0); // 0 is the starting argument
    player.sendMessage(message);
  }
  
  @Override
  public Iterable<String> tabComplete(CommandSender sender, String label, CommandArguments args) {
    return null; // hadle tab completion
  }
}
```

Registering the command is simple
```java
commandManager.registerCommand(new CommandTestPlaceholders(), "mycommandname1", "mycommandname2");
```

Making a argument resolver. If you have multiple ones, make sure to make a util class or something
with them so they're easily accessable. Also keep in mind that this is a example.
```java
// creation
public static Function<String, Weapon> WEAPON = Weapon::new;


// usage
args.next(WEAPON).ifPresent(...).orElse(...);
```