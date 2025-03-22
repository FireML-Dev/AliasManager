package uk.firedev.aliasmanager.local;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;

public class PluginCommand {

    public static CommandTree get() {
        return new CommandTree("aliasmanager")
            .then(
                new LiteralArgument("reload")
                    .executes(info -> {
                        AliasManager.INSTANCE.reload();
                        info.sender().sendPlainMessage("Reloaded AliasManager.");
                    })
            );
    }

}
