package com.dailyrewards.menu;


import com.dailyrewards.extentions.Gui;
import com.dailyrewards.extentions.Initializer;
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

public class MenuManager implements Initializer<MenuManager>, Listener {

    private List<Gui> open_guis;

    public void register(Gui gui) {
        if(this.open_guis.contains(gui)) {
            try {
                throw new IOException("Gui already exists in list");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.open_guis.add(gui);
        }
    }

    public void unregister(Gui gui) {
        if(!this.open_guis.contains(gui)) {
            try {
                throw new IOException("Gui not exists in list");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.open_guis.remove(gui);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(exists(e.getInventory())) this.getGui(e.getInventory()).onInventoryClick(e);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if(exists(e.getInventory())) this.getGui(e.getInventory()).onInventoryClose(e);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e){
        if(exists(e.getInventory())) this.getGui(e.getInventory()).onInventoryOpen(e);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e){
        if(exists(e.getInventory())) this.getGui(e.getInventory()).onInventoryDrag(e);
    }

    public boolean exists(Inventory inv) {
        Gui gui;
        for (int x = 0; x < this.open_guis.size(); x++) {
            gui = this.open_guis.get(x);
            if(gui.equals(inv.getHolder())) return true;
        }
        return false;
    }

    public Gui getGui(Inventory inv) {
        Gui gui;
        for (int x = 0; x < this.open_guis.size(); x++) {
            gui = this.open_guis.get(x);;
            if(gui.equals(inv.getHolder())) return gui;
        }
        return null;
    }


    public MenuManager onEnable() {
        this.open_guis = new ArrayList<>();
        return this;
    }

    public void onDisable() {
        if(open_guis != null) {
            for (int x = 0; x < open_guis.size(); x++) {
                open_guis.get(x).close();
            }
            open_guis.clear();
        }
        this.open_guis = null;
    }

    public void onReload() {
        this.onDisable();
        this.onEnable();
    }

}
