package de.trc.TRC_TeleportCompasses;

import org.bukkit.plugin.java.JavaPlugin;

public final class TRC_TeleportCompasses extends JavaPlugin {

    @Override
    public void onEnable() {
        Constants.LOGGER.info(Constants.PLUGIN_NAME + " aktiviert");

        saveDefaultConfig();
        Constants.init(this);

        getServer().getPluginManager().registerEvents(new Crafting(), this);
        getServer().getPluginManager().registerEvents(new RightClickListener(), this);
    }

    @Override
    public void onDisable() {
        Constants.LOGGER.info(Constants.PLUGIN_NAME + " deaktiviert");
    }
}
