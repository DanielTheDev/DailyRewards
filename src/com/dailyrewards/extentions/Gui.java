package com.dailyrewards.extentions;

import com.dailyrewards.PluginClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Gui extends BukkitRunnable implements InventoryHolder {

    private Inventory inventory;
    protected Player player;
    private String title;
    private BukkitTask task;
    private int size;
    private boolean timer;

    public Gui(Player player, String title, int size, boolean timer) {
        this.player = player;
        this.title = title;
        this.size = size;
        this.timer = timer;
        this.inventory = Bukkit.createInventory(this, size, Chat.toColor(title));
    }

    public void run() {
        if(this.player != null && !this.player.isOnline()) destroy();
    }

    public void update() {
        this.inventory.clear();
        this.fill(inventory);
    }

    public void open() {
        if(timer) this.task = this.runTaskTimer(PluginClass.getPlugin(), 20, 20);
        this.fill(this.inventory);
        this.player.openInventory(this.inventory);
    }

    public BukkitTask getTask() {
        return task;
    }


    public boolean isTimer() {
        return timer;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public void close() {
        this.player.closeInventory();
    }

    public abstract void fill(Inventory inventory);

    private void destroy() {
        this.task.cancel();
        PluginClass.getPlugin().getPluginLib().getMenuManager().unregister(this);
        this.inventory.clear();
        Initializer.unload(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){}

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        this.destroy();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e){}

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e){}

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
