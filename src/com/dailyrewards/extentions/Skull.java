package com.dailyrewards.extentions;

import com.dailyrewards.PluginClass;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Skull {

    public static void getPlayerSkull(Player player, SkullResult result) {
        new BukkitRunnable() {

            @Override
            public void run() {

                CraftItem item = new CraftItem(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                SkullMeta meta = (SkullMeta) item.getRawMeta();
                meta.setOwner(player.getName());
                item.setRawMeta(meta);

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        result.call(item);
                    }
                }.runTask(PluginClass.getPlugin());
            }
        }.runTaskAsynchronously(PluginClass.getPlugin());
    }


    public interface SkullResult {
        void call(CraftItem item);
    }
}
