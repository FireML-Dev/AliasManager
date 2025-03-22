package uk.firedev.aliasmanager;

import dev.jorel.commandapi.CommandAPI;

import java.util.List;

public class CustomAliases {

    public static final CustomAliases INSTANCE = new CustomAliases();

    private CustomAliases() {}

    public void load() {
        loadAllCommands();
    }

    public void reload() {
        loadAllCommands();
    }

    private void loadAllCommands() {
        List<String> configCommands = AliasConfig.INSTANCE.getCommandBuilderNames();

        // Unregister any removed aliases
        for (String command : configCommands) {
            if (!CommandBuilder.REGISTERED.contains(command)) {
                CommandAPI.unregister(command);
                CommandAPI.unregister("aliasmanager:" + command);
            }
        }

        // Re-register all aliases
        AliasConfig.INSTANCE.getCommandBuilders().forEach(builder -> {
            String name = builder.getCommandName();
            if (name == null || name.isEmpty()) {
                return;
            }
            // Command registration
            builder.registerCommand();
        });
    }

}
