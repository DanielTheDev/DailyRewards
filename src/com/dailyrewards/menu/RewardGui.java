package com.dailyrewards.menu;

import com.dailyrewards.PluginClass;
import com.dailyrewards.config.RewardConfig;
import com.dailyrewards.extentions.Chat;
import com.dailyrewards.extentions.Gui;
import com.dailyrewards.extentions.Initializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RewardGui extends Gui {

    private final List<RewardItem> items;
    private boolean isReady;
    private BukkitTask animation;
    private final ItemStack trail;
    private final ItemStack wrapped;
    private final MenuManager manager = PluginClass.getPlugin().getPluginLib().getMenuManager();

    RewardGui(Player player) {
        super(player, "&f&l--&e&l=&f&l-- &5&lDaily &d&lRewards &f&l--&e&l=&f&l--", 45, true, 5, 0);
        this.items = new ArrayList<>();
        this.trail = PluginClass.getPluginConfig().getPresentTrail().build();
        this.wrapped = PluginClass.getPluginConfig().getPresentWrapped().build();
        this.init();
    }

    private void init() {
        for (RewardConfig.Present present : PluginClass.getPlugin().getPluginLib().getRewardConfig().getPresents()) {
            if (this.player.hasPermission(present.getPermissions())) {
                try {
                    this.addPresent(present);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void fill() {
        for (RewardItem item : items) {
            this.setItem(item);
        }
    }

    @Override
    public void run() {
        boolean stop = true;

        for (RewardItem item : items) {

            if (!item.hasArrived()) stop = false;

            if (item.getPaths().size() != 0) {

                item.updateTrails(inventory);
                item.addTrail(inventory, manager.getSlot(item.getX(), item.getY()), trail);

                int[] loc = item.getPaths().get(0);

                if (loc[0] == 0) {
                    item.moveX(loc[1]);
                } else {
                    item.moveY(loc[1]);
                }

                item.getPaths().remove(0);
            }
            this.inventory.setItem(manager.getSlot(item.getX(), item.getY()), wrapped);
        }

        if (stop) {

            this.cancel();

            reveal();

            ItemStack item;

            for (int slot = 0; slot < this.getSize(); slot++) {
                item = this.inventory.getItem(slot);
                if (item != null && item.getType() == trail.getType()) this.inventory.setItem(slot, null);
            }
        }
        player.playSound(player.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, (float) 1);
    }

    private void reveal() {

        animation = new BukkitRunnable() {

            int x;
            final int max = items.size();
            RewardItem item;

            @Override
            public void run() {
                if (x < max) {
                    item = items.get(x);
                    setItem(item);
                    item.destroy();
                    item = null;
                    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, (float) 1);
                } else {
                    this.cancel();
                    isReady = true;
                }
                x++;
            }
        }.runTaskTimer(PluginClass.getPlugin(), 20, 15);
    }

    private void setItemPaths() {
        int type = manager.random(0, 1);

        int[] oldLocation;
        for (RewardItem item : items) {
            oldLocation = new int[]{item.getX(), item.getY()};
            while (!item.hasArrived()) {

                int xStep = manager.random(1, 2);
                int yStep = manager.random(1, 3);

                int xRemaining = item.getRemainingX();
                int yRemaining = item.getRemainingY();

                int xRemainingType = item.getRemainingTypeX();
                int yRemainingType = item.getRemainingTypeY();


                if (type % 2 == 0 && !item.hasArrived_Y()) {
                    if (yStep >= item.getRemainingY()) {
                        item.getPaths().addAll(createPaths(1, yRemaining * yRemainingType));
                        item.moveY(yRemaining * yRemainingType);
                    } else {
                        item.getPaths().addAll(createPaths(1, yStep * yRemainingType));
                        item.moveY(yStep * yRemainingType);
                    }
                } else if (!item.hasArrived_X()) {
                    if (xStep >= item.getRemainingX()) {
                        item.getPaths().addAll(createPaths(0, xRemaining * xRemainingType));
                        item.moveX(xRemaining * xRemainingType);
                    } else {
                        item.getPaths().addAll(createPaths(0, xStep * xRemainingType));
                        item.moveX(xStep * xRemainingType);
                    }
                }
                type++;
            }
            item.teleport(oldLocation[0], oldLocation[1]);
        }
    }

    private List<int[]> createPaths(int type, int amount) {
        List<int[]> list = new ArrayList<>();
        int adjust = amount < 0 ? -1 : 1;
        if (amount == 0) return list;

        for (int place = 0; place < Math.abs(amount); place++) {
            list.add(new int[]{type, adjust});
        }
        return list;
    }

    private void setItem(RewardItem item) {
        int slot = manager.getSlot(item.getX(), item.getY());
        if (slot < 0 || slot > this.getSize()) try {
            throw new IOException("Item out of range");
        } catch (IOException e) {
            e.printStackTrace();
        }
        else {
            this.inventory.setItem(slot, item.getPresent().getItem().build());
        }
    }

    private boolean destinationExits(int x, int y) {
        for (RewardItem item : items) {
            if (item.getDestination_x() == x && item.getDestination_y() == y) {
                return true;
            }
        }
        return false;
    }

    private void addPresent(RewardConfig.Present present) throws IOException {
        if (this.items.size() > 9) throw new IOException("Cannot add more presents, max: 10");
        int[] destination = createDestination();
        int destX = destination[0];
        int destY = destination[1];
        this.items.add(new RewardItem(present, 0, 0, destX, destY));
    }

    private int[] createDestination() {
        int destX = this.manager.random(-3, 3);
        int destY = this.manager.random(-2, 2);
        destX = destX < 0 ? destX - 1 : destX + 1;
        if (this.destinationExits(destX, destY)) {
            return createDestination();
        } else {
            return new int[]{destX, destY};
        }
    }

    private boolean hasOpenedAllPresents() {
        boolean opened_all = true;
        for (RewardItem item : items) {
            if (!item.isOpened()) opened_all = false;
        }
        return opened_all;
    }

    private void openItem(RewardItem item) {
        item.setOpened(true);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 1, (float) 1);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, (float) ThreadLocalRandom.current().nextDouble(0.5, 1.5));
        this.inventory.setItem(manager.getSlot(item.getX(), item.getY()), item.getPresent().getOpened_item().build());

        for (String command : item.getPresent().getRewards()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Chat.placeholder(player, command));
        }
        item.getPresent().destroy();

        if (hasOpenedAllPresents()) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    Gui openGui = PluginClass.getPlugin().getPluginLib().getMenuManager().getOpenGui(player);
                    if (openGui != null && openGui.getClass().getSimpleName().equals("RewardGui"))
                        player.closeInventory();
                    player.sendMessage(Chat.toColor(Chat.placeholder(player, PluginClass.getPluginConfig().getRewardFinished())));
                }
            }.runTaskLater(PluginClass.getPlugin(), 50);
        }
    }

    @Override
    public void destroy() {
        this.items.clear();
        Initializer.unload(this);
        super.destroy();
    }

    public void open() {
        this.setItemPaths();
        super.open();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        player.updateInventory();
        e.setCancelled(true);
        if (isReady) {
            for (RewardItem item : items) {
                if (e.getRawSlot() == manager.getSlot(item.getX(), item.getY()) && !item.isOpened()) {
                    openItem(item);
                }
            }
        }
    }

    @Override
    public void onInventoryDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent e) {
        if (this.animation != null) this.animation.cancel();
        if (!this.hasOpenedAllPresents()) {
            e.getPlayer().sendMessage(Chat.toColor("&7Oops, you forgot to claim all your presents."));
        }
        super.onInventoryClose(e);
    }


}
