package uk.firedev.aliasmanager.local;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.plugin.java.JavaPlugin;
import uk.firedev.aliasmanager.AliasConfig;
import uk.firedev.aliasmanager.CustomAliases;

public final class AliasManager extends JavaPlugin {

    public static AliasManager INSTANCE;

    public AliasManager() {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException(getClass().getName() + " has already been assigned!");
        }
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
        CommandAPIBukkitConfig config = new CommandAPIBukkitConfig(this)
            .shouldHookPaperReload(true)
            .usePluginNamespace()
            .missingExecutorImplementationMessage("You are not able to use this command!");
        CommandAPI.onLoad(config);
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        PluginCommand.get().register(this);
        CustomAliases.INSTANCE.load();
    }

    public void reload() {
        AliasConfig.INSTANCE.reload();
        CustomAliases.INSTANCE.reload();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }

}
