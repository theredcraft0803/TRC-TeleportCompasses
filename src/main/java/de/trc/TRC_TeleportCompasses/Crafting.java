package de.trc.TRC_TeleportCompasses;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Crafting implements Listener {

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack[] matrix = inv.getMatrix();
        ItemStack compass = null;
        boolean hasEchoShard = false;
        boolean hasEnderPearl = false;
        int emptySlots = 0;

        for (ItemStack item : matrix) {
            if (item == null) {
                emptySlots++;
                continue;
            }
            if (item.getType() == Material.COMPASS && item.getAmount() == 1) {
                ItemMeta meta = item.getItemMeta();
                if ((meta instanceof CompassMeta compassMeta && compassMeta.hasLodestone()) && !(meta.getPersistentDataContainer().has(Constants.IS_TP_COMPASS))) {
                    compass = item;
                }
            }
            if (item.getType() == Material.ECHO_SHARD && item.getAmount() == 1) {
                hasEchoShard = true;
            }
            if (item.getType() == Material.ENDER_PEARL && item.getAmount() == 1) {
                hasEnderPearl = true;
            }
        }

        if (compass == null || !hasEnderPearl || !hasEchoShard || emptySlots != 6) {
            return;

        }

        ItemStack result = compass.clone();
        ItemMeta meta = result.getItemMeta();

        meta.displayName(Component.text("TP-").decoration(TextDecoration.ITALIC, false).append(Component.translatable("item.minecraft.compass").decoration(TextDecoration.ITALIC, false)));
        if (Constants.MAX_USES != -1) meta.lore(List.of(Component.text("Uses left: ").color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.ITALIC, false).append(Component.text(Constants.MAX_USES).color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false))));
        meta.setRarity(ItemRarity.RARE);
        meta.getPersistentDataContainer().set(Constants.IS_TP_COMPASS, PersistentDataType.BOOLEAN, true);
        meta.getPersistentDataContainer().set(Constants.COMPASS_USES_LEFT, PersistentDataType.INTEGER, Constants.MAX_USES);

        result.setItemMeta(meta);
        inv.setResult(result);
    }
}
