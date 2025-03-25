package uk.firedev.aliasmanager;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.jorel.commandapi.CommandTree;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.ParsingException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import uk.firedev.aliasmanager.local.AliasManager;

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
        new CommandTree(commandName)
            .withAliases(aliases.toArray(String[]::new))
            .withPermission(permission)
            .executes(info -> {
                CommandSender sender = info.sender();
                messages.forEach(message -> sendMessage(message, sender));
                commands.forEach(command -> executeCommand(command, info.sender()));
            }).register(AliasManager.INSTANCE);
        addToRegistered();
    }

    private void addToRegistered() {
        if (!REGISTERED.contains(commandName)) {
            REGISTERED.add(commandName);
        }

        for (String alias : aliases) {
            if (!REGISTERED.contains(alias)) {
                REGISTERED.add(alias);
            }
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public boolean isDisabled() {
        return disabled;
    }

    private void sendMessage(@NotNull String message, @NotNull Audience audience) {
        Component component;
        try {
           component = MiniMessage.miniMessage().deserialize(message);
        } catch (ParsingException exception) {
            AliasManager.INSTANCE.getLogger().warning("\"" + message + "\" is not a valid MiniMessage String.");
            return;
        }
        audience.sendMessage(component);
    }

    private void executeCommand(@NotNull String command, @NotNull CommandSender sender) {
        if (sender instanceof Player player) {
            command = command.replace("{player}", player.getName());
        }
        if (command.startsWith("console:")) {
            command = command.replace("console:", "");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        } else {
            Bukkit.dispatchCommand(sender, command);
        }
    }

}
