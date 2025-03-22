package uk.firedev.aliasmanager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import uk.firedev.aliasmanager.local.AliasManager;

import java.util.ArrayList;
import java.util.List;

public class AliasConfig {

    public static final AliasConfig INSTANCE = new AliasConfig();

    private AliasConfig() {}

    public FileConfiguration getConfig() {
        AliasManager.INSTANCE.saveDefaultConfig();
        return AliasManager.INSTANCE.getConfig();
    }

    public void reload() {
        AliasManager.INSTANCE.reloadConfig();
    }

    public void save() {
        AliasManager.INSTANCE.saveConfig();
    }

    public List<CommandBuilder> getCommandBuilders() {
        List<CommandBuilder> list = new ArrayList<>();
        getConfig().getKeys(false).forEach(key -> {
            ConfigurationSection section = getConfig().getConfigurationSection(key);
            if (section == null) {
                return;
            }
            list.add(new CommandBuilder(section));
        });
        return list;
    }

    public List<String> getCommandBuilderNames() {
        List<String> names = new ArrayList<>();
        for (CommandBuilder builder : getCommandBuilders()) {
            names.add(builder.getCommandName());
            names.addAll(builder.getAliases());
        }
        return names;
    }

}
