package de.trc.TRC_TeleportCompasses;

import org.bukkit.NamespacedKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    public static final String PLUGIN_NAME = "TRC Teleport Compasses";
    public static final Logger LOGGER = LoggerFactory.getLogger(PLUGIN_NAME);
    public static int MAX_USES;
    public static boolean BREAK_ON_CONSUMED;
    public static long TP_COOLDOWN_MS;
    public static NamespacedKey IS_TP_COMPASS;
    public static NamespacedKey COMPASS_USES_LEFT;

    public static void init(TRC_TeleportCompasses plugin) {
        MAX_USES = plugin.getConfig().getInt("max_uses", 10);
        BREAK_ON_CONSUMED = plugin.getConfig().getBoolean("break_at_zero_uses", true);
        TP_COOLDOWN_MS = plugin.getConfig().getLong("tp_cooldown", 3000);

        IS_TP_COMPASS = new NamespacedKey(plugin, "is_tp_compass");
        COMPASS_USES_LEFT = new NamespacedKey(plugin, "compass_uses_left");
    }
}
