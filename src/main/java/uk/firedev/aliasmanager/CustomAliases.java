package uk.firedev.aliasmanager;

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
