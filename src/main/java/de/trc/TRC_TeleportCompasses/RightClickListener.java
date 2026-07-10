package de.trc.TRC_TeleportCompasses;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class RightClickListener implements Listener {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        Action action = event.getAction();
        Player player = event.getPlayer();

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {


            ItemStack item = event.getItem();

            if (item == null) return;
            if (item.getType() != Material.COMPASS) return;
            ItemMeta itemMeta = item.getItemMeta();
            if (!itemMeta.getPersistentDataContainer().has(Constants.IS_TP_COMPASS)) return;

            UUID uuid = player.getUniqueId();
            long now = System.currentTimeMillis();

            if (cooldowns.containsKey(uuid)) {
                long lastUse = cooldowns.get(uuid);

                if (now - lastUse < Constants.TP_COOLDOWN_MS) {
                    double remaining = (Constants.TP_COOLDOWN_MS - (now - lastUse)) / 1000.0;
                    player.sendActionBar(Component.text("On Cooldown! - ").color(NamedTextColor.RED).append(Component.text(String.format("%.1f", remaining)).color(NamedTextColor.RED)).append(Component.text("s").color(NamedTextColor.RED)));
                    event.setCancelled(true);
                    return;
                }
            }

            cooldowns.put(uuid, now);
            CompassMeta compassMeta = (CompassMeta) item.getItemMeta();

            if (compassMeta != null && compassMeta.isLodestoneTracked()) {

                Location location = compassMeta.getLodestone();
                if (location == null) return;

                location.add(0.5, 1, 0.5);
                player.teleport(location);

                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.UI, 100, 1);

                if (Constants.MAX_USES == -1) return;

                int uses_left = Objects.requireNonNullElse(itemMeta.getPersistentDataContainer().get(Constants.COMPASS_USES_LEFT, PersistentDataType.INTEGER), 0);
                uses_left--;
                itemMeta.getPersistentDataContainer().set(Constants.COMPASS_USES_LEFT, PersistentDataType.INTEGER, uses_left);

                if (uses_left < 1 && !Constants.BREAK_ON_CONSUMED) {
                    player.playSound(player, Sound.BLOCK_ANVIL_BREAK, SoundCategory.UI, 100, 1);
                    itemMeta.getPersistentDataContainer().remove(Constants.COMPASS_USES_LEFT);
                    itemMeta.getPersistentDataContainer().remove(Constants.IS_TP_COMPASS);
                    itemMeta.setRarity(ItemRarity.COMMON);
                    itemMeta.lore(null);
                    itemMeta.displayName(Component.translatable("item.minecraft.lodestone_compass").decoration(TextDecoration.ITALIC, false));
                    item.setItemMeta(itemMeta);
                } else if (uses_left < 1) {
                    player.getInventory().remove(item);
                    player.playSound(player, Sound.ENTITY_ITEM_BREAK, SoundCategory.UI, 100, 1);
                } else {
                    itemMeta.lore(List.of(Component.text("Uses left: ").color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.ITALIC, false).append(Component.text(uses_left).color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false))));
                    item.setItemMeta(itemMeta);
                }
            }
        }
    }
}
