package uk.firedev.aliasmanager;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.ParsingException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import uk.firedev.aliasmanager.local.AliasManager;
import uk.firedev.daisylib.api.message.component.ComponentMessage;
import uk.firedev.daisylib.api.message.string.StringReplacer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO add namespace option
public class CommandBuilder {

    public static final List<String> REGISTERED = new ArrayList<>();

    private final boolean disabled;
    private final String commandName;
    private final List<String> aliases;
    private final String permission;
    private final List<String> commands;
    private final List<String> messages;

    public CommandBuilder(@NotNull ConfigurationSection section) {
        this.disabled = section.getBoolean("disabled", false);
        this.commandName = Objects.requireNonNull(section.getName());
        this.aliases = section.getStringList("aliases");
        this.permission = section.getString("permission");
        this.commands = section.getStringList("commands");
        this.messages = section.getStringList("messages");
    }

    public void registerCommand() {
        if (commandName == null || disabled) {
            return;
        }
        new CommandTree(commandName)
            .withAliases(aliases.toArray(String[]::new))
            .withPermission(permission)
            .executes(info -> {
                CommandSender sender = info.sender();
                messages.forEach(message -> ComponentMessage.fromString(message).sendMessage(sender));
                commands.forEach(executeCommand -> {
                    CommandSender thisSender;
                    StringReplacer replacer = StringReplacer.create();
                    if (sender instanceof Player player) {
                        replacer.addReplacement("player", player.getName());
                    }
                    if (executeCommand.startsWith("console:")) {
                        executeCommand = executeCommand.replace("console:", "");
                        thisSender = Bukkit.getConsoleSender();
                    } else {
                        thisSender = sender;
                    }
                    Bukkit.dispatchCommand(thisSender, replacer.replace(executeCommand));
                });
            })
            .register(AliasManager.INSTANCE);
        REGISTERED.add(commandName);
    }

    public String getCommandName() {
        return commandName;
    }

    public List<String> getAliases() {
        return aliases;
    }

}
