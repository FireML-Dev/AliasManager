package uk.firedev.aliasmanager;

import dev.jorel.commandapi.CommandTree;
import org.jetbrains.annotations.NotNull;
import uk.firedev.aliasmanager.local.AliasManager;

import java.util.ArrayList;
import java.util.List;

public class CustomAliases {

    public static final CustomAliases INSTANCE = new CustomAliases();

    public static final List<CommandBuilder> COMMAND_BUILDERS = new ArrayList<>();

    private CustomAliases() {}

    public void load() {
        loadAllCommands();
    }

    public void reload() {
        loadAllCommands();
    }

    private void loadAllCommands() {
        List<String> oldCommands = List.copyOf(CommandBuilder.REGISTERED);
        CommandBuilder.REGISTERED.clear();

        List<CommandBuilder> builders = AliasConfig.INSTANCE.getCommandBuilders();

        // Check every configured builder
        for (CommandBuilder builder : builders) {
            String name = builder.getCommandName();
            if (name == null || name.isEmpty()) {
                return;
            }
            // If the builder is disabled, overwrite any registered commands with an "alias disabled" message
            if (builder.isDisabled()) {
                checkAlias(name, oldCommands);
                builder.getAliases().forEach(alias -> checkAlias(alias, oldCommands));
                return;
            }
            // Register the command
            builder.registerCommand();
        }
    }

    private void checkAlias(@NotNull String alias, @NotNull List<String> oldCommands) {
        if (alias.isEmpty()) {
            return;
        }
        if (oldCommands.contains(alias)) {
            registerAliasDisabled(alias);
        }
    }

    private void registerAliasDisabled(@NotNull String alias) {
        new CommandTree(alias)
            .executes(info -> {
                info.sender().sendRichMessage("<red>This alias is disabled.");
            })
            .register(AliasManager.INSTANCE);
    }

}
