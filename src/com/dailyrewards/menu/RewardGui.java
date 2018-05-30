package com.dailyrewards.menu;

import com.dailyrewards.extentions.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
public class RewardGui extends Gui {


    public RewardGui(Player player) {
        super(player, "hey", 54, true);
    }

    @Override
    public void fill(Inventory inventory) {

    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onInventoryDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }
}
