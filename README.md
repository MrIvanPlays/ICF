# ICF
Ivan's command framework.

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
    args.next(String.class /* ARGUMENT CLASS HERE */)
      .ifPresent(sender::sendMessage)
      .orElse(() -> sender.sendMessage("You need to have atleast 1 argument!"));
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
    args.next(String.class /* ARGUMENT CLASS HERE */)
      .ifPresent(player::sendMessage)
      .orElse(() -> player.sendMessage("You need to have atleast 1 argument!"));
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
    String message = args.getArgumentsJoined(0); // 0 is the starting argument
    player.sendMessage(message);
  }
}
```

Registering the command is simple
```java
commandManager.registerCommand(new CommandTestPlaceholders(), "mycommandname1", "mycommandname2");
```

Registering a argument resolver
```java
commandManager.registerArgumentResolver(MyCustomClass.class, input -> {
  return new MyCustomClass(input);
})
```