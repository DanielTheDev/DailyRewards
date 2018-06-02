package com.dailyrewards.menu;


import com.dailyrewards.extentions.Gui;
import com.dailyrewards.extentions.Initializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MenuManager implements Initializer<MenuManager>, Listener {
    private final List<Gui> open_guis;
    private final int[][] map = {
            {-4, 2}, {-3, 2}, {-2, 2}, {-1, 2}, {0, 2}, {1, 2}, {2, 2}, {3, 2}, {4, 2},
            {-4, 1}, {-3, 1}, {-2, 1}, {-1, 1}, {0, 1}, {1, 1}, {2, 1}, {3, 1}, {4, 1},
            {-4, 0}, {-3, 0}, {-2, 0}, {-1, 0}, {0, 0}, {1, 0}, {2, 0}, {3, 0}, {4, 0},
            {-4, -1}, {-3, -1}, {-2, -1}, {-1, -1}, {0, -1}, {1, -1}, {2, -1}, {3, -1}, {4, -1},
            {-4, -2}, {-3, -2}, {-2, -2}, {-1, -2}, {0, -2}, {1, -2}, {2, -2}, {3, -2}, {4, -2}};


    public MenuManager() {
        this.open_guis = new ArrayList<>();
    }

    public List<Gui> getOpen_guis() {
        return this.open_guis;
    }

    public int getSlot(int x, int y) {
        for (int slot = 0; slot < map.length; slot++) {
            if (map[slot][0] == x && map[slot][1] == y) return slot;
        }
        return -1;
    }

    public boolean hasGuiOpen(Player player) {
        return getOpenGui(player) != null;
    }

    public Gui getOpenGui(Player player) {
        for (int x = 0; x < open_guis.size(); x++) {
            if (open_guis.get(x).getPlayer().equals(player)) return open_guis.get(x);
        }
        return null;
    }

    public int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public void register(Gui gui) {
        if (this.open_guis.contains(gui)) {
            try {
                throw new IOException("Gui already exists in list");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.open_guis.add(gui);
        }
    }

    public void open(Gui gui) {
        if (!this.hasGuiOpen(gui.getPlayer())) gui.open();
    }

    public void switchTo(Gui gui) {
        Gui oldGui = this.getOpenGui(gui.getPlayer());
        if (oldGui != null && oldGui.getClass() != gui.getClass()) {
            gui.getPlayer().closeInventory();
            gui.open();
        }
    }

    public void unregister(Gui gui) {
        if (!this.open_guis.contains(gui)) {
            try {
                throw new IOException("Gui not exists in list");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.open_guis.remove(gui);
        }
    }

    public boolean exists(Inventory inv) {
        Gui gui;
        for (int x = 0; x < this.open_guis.size(); x++) {
            gui = this.open_guis.get(x);
            if (gui.equals(inv.getHolder())) return true;
        }
        return false;
    }

    public Gui getGui(Inventory inv) {
        Gui gui;
        for (int x = 0; x < this.open_guis.size(); x++) {
            gui = this.open_guis.get(x);
            if (gui.equals(inv.getHolder())) return gui;
        }
        return null;
    }


    public MenuManager onEnable() {
        this.open_guis.clear();
        return this;
    }

    public void onDisable() {
        for (int x = 0; x < open_guis.size(); x++) {
            open_guis.get(x).close();
        }
        open_guis.clear();
    }

    public void onReload() {
        this.onDisable();
        this.onEnable();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (exists(e.getInventory())) this.getGui(e.getInventory()).onInventoryClick(e);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (exists(e.getInventory())) this.getGui(e.getInventory()).onInventoryClose(e);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (exists(e.getInventory())) this.getGui(e.getInventory()).onInventoryOpen(e);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (exists(e.getInventory())) this.getGui(e.getInventory()).onInventoryDrag(e);
    }
}
