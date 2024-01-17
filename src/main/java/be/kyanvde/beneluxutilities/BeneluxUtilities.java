package be.kyanvde.beneluxutilities;

import be.kyanvde.beneluxutilities.commands.GoToCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class BeneluxUtilities extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("goto").setExecutor(new GoToCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
