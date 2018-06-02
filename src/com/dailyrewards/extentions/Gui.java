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

    protected final Inventory inventory;
    protected final Player player;
    private final String title;
    private BukkitTask task;
    private final int size;
    private final boolean timer;
    private final int interval;
    private final int delay;

    public Gui(Player player, String title, int size, boolean timer) {
        this(player, title, size, timer, 20, 20);
    }

    public Gui(Player player, String title, int size, boolean timer, int interval, int delay) {
        this.player = player;
        this.title = title;
        this.size = size;
        this.timer = timer;
        this.inventory = Bukkit.createInventory(this, size, Chat.toColor(title));
        this.interval = interval;
        this.delay = delay;
    }

    public void run() {
        if (this.player != null && (!this.player.isOnline() || player.getOpenInventory() == null)) destroy();
    }

    public void update() {
        this.inventory.clear();
        this.fill();
        this.updatePlayer();
    }

    public void updatePlayer() {
        this.player.updateInventory();
    }

    public void open() {
        if (timer) this.task = this.runTaskTimer(PluginClass.getPlugin(), delay, interval);
        this.fill();
        this.player.openInventory(this.inventory);
        PluginClass.getPlugin().getPluginLib().getMenuManager().register(this);
    }

    public BukkitTask getTask() {
        return task;
    }

    public Player getPlayer() {
        return player;
    }

    public int getInterval() {
        return interval;
    }

    public int getDelay() {
        return delay;
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

    public abstract void fill();


    public void destroy() {
        try {
            this.task.cancel();
        } catch (NullPointerException ignored) {}
        PluginClass.getPlugin().getPluginLib().getMenuManager().unregister(this);
        this.inventory.clear();
        Initializer.unload(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        this.destroy();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return "Gui{Type=" + this.getClass().getSimpleName() + ", Player=" + player.getName() + "}";
    }
}
