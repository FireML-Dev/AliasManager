package uk.firedev.aliasmanager;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uk.firedev.aliasmanager.local.AliasManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class AliasConfig {

    public static final AliasConfig INSTANCE = new AliasConfig();

    private final YamlDocument document;

    private AliasConfig() {
        File file = new File(AliasManager.INSTANCE.getDataFolder(), "aliases.yml");
        try {
            YamlDocument document;
            InputStream resource = AliasManager.INSTANCE.getResource("aliases.yml");
            if (resource == null) {
                document = YamlDocument.create(file);
            } else {
                document = YamlDocument.create(file, resource);
            }
            this.document = document;
        } catch (IOException exception) {
            AliasManager.INSTANCE.getLogger().severe("Could not load aliases.yml.");
            throw new RuntimeException(exception);
        }
    }

    public YamlDocument getConfig() {
        return document;
    }

    public void reload() {
        try {
            document.reload();
        } catch (IOException exception) {
            AliasManager.INSTANCE.getLogger().warning("Could not reload aliases.yml.");
        }
    }

    public void save() {
        try {
            document.save();
        } catch (IOException exception) {
            AliasManager.INSTANCE.getLogger().warning("Could not save aliases.yml.");
        }
    }

    public List<CommandBuilder> getCommandBuilders() {
        List<CommandBuilder> list = new ArrayList<>();
        getConfig().getRoutesAsStrings(false).forEach(key -> {
            Section section = getConfig().getSection(key);
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
