package uk.firedev.aliasmanager;

import dev.dejvokep.boostedyaml.block.implementation.Section;
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

import java.util.*;

public class CommandBuilder {

    public static final List<String> REGISTERED = new ArrayList<>();

    private final boolean disabled;
    private final String commandName;
    private final List<String> aliases;
    private final String permission;
    private final List<String> commands;
    private final List<String> messages;

    public CommandBuilder(@NotNull Section section) {
        this.disabled = section.getBoolean("disabled", false);
        this.commandName = Objects.requireNonNull(section.getNameAsString());
        this.aliases = section.getStringList("aliases");
        this.permission = section.getString("permission");
        this.commands = section.getStringList("commands");
        this.messages = section.getStringList("messages");
    }

    public void registerCommand() {
        if (commandName == null || disabled) {
            return;
        }
        CommandTree command = new CommandTree(commandName)
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
        REGISTERED.add(commandName);
    }

    public String getCommandName() {
        return commandName;
    }

    public List<String> getAliases() {
        return aliases;
    }

}
