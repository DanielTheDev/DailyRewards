package com.dailyrewards.menu;

import com.dailyrewards.PluginClass;
import com.dailyrewards.config.PlayerData;
import com.dailyrewards.config.PlayerDataManager;
import com.dailyrewards.extentions.Chat;
import com.dailyrewards.extentions.CraftItem;
import com.dailyrewards.extentions.Gui;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;


public class MainGui extends Gui {

    private final PlayerDataManager manager;
    private final PlayerData data;

    public MainGui(Player player) {
        super(player, "&f&l--&e&l=&f&l-- &5&lDaily &d&lRewards &f&l--&e&l=&f&l--", 27, true);
        this.manager = PluginClass.getPlugin().getPluginLib().getPlayerDataManager();
        this.data = manager.getPlayer(player);
    }

    public void fill(Inventory inventory) {
        inventory.setItem(13, getChest());
        inventory.setItem(22, getPlayerStats());

        for(int place = 0; place < this.getSize(); place++) {
            if(inventory.getItem(place) == null) inventory.setItem(place, new CraftItem(Material.STAINED_GLASS_PANE, 1, (short) (place > 9 && place < 17 ? 6 : 2)).setDisplayName("&a").build());
        }
        for(int place : new int[] {0,8,18,26}) {
            inventory.setItem(place, new CraftItem(Material.STAINED_GLASS_PANE, 1, (short) 10).setGlow(true).setDisplayName("&a").build());
        }
        this.player.updateInventory();
    }

    public ItemStack getPlayerStats() {
        int remainingStreakTime = this.data.streakTimeRemaining();
        Date lastClaimed = this.data.getLastClaimed();
        Date firstClaimed = this.data.getFirstClaimed();

        return CraftItem.getPlayerSkull(player)
                .setDisplayName("&6&l" + player.getName()+"'s Stats")
                .setLore(
                        "&7Claim Streak&f: "+ this.data.getClaimStreak(),
                        "&7Streak Remaining Time&f: "+ (remainingStreakTime > 0 ? Chat.timeTranslate(remainingStreakTime) : "none"),
                        "&7Total Claimed&f: "+ this.data.getTotalClaimed(),
                        "&7First Claimed&f: "+ (firstClaimed != null ? Chat.timeTranslate((System.currentTimeMillis()-firstClaimed.getTime())/1000) + " ago" : "none"),
                        "&7Last Claimed&f: "+ (lastClaimed != null ? Chat.timeTranslate((System.currentTimeMillis()-lastClaimed.getTime())/1000) + " ago" : "none")
                )
                .build();
    }

    public ItemStack getChest() {
        if(this.data.canClaim()) {
            return new CraftItem(Material.CHEST)
                    .setDisplayName("&aDaily Bonus Reward")
                    .setLore(
                            "&c&m------&6&m------&e&m------&a&m------&b&m------&d&m------",
                            "&7You daily bonus reward is available",
                            "&7It contains some magical presents",
                            "&7Open the reward and unwrap the presents.",
                            "",
                            "&6&lReward:",
                            "&f- &5Something Magical",
                            "",
                            "&eClick to claim",
                            "&c&m------&6&m------&e&m------&a&m------&b&m------&d&m------"
                            )
                    .build();
        } else {
            return new CraftItem(Material.REDSTONE_BLOCK)
                    .setDisplayName("&cDaily Bonus Reward")
                    .setLore(
                            "&c&m------&6&m------&e&m------&a&m------&b&m------&d&m------",
                            "&7You already claimed your daily bonus.",
                            "&7unfortunately you must wait a moment.",
                            "&7Maybe I will reward you with something better",
                            "&7That depends on your mood ^_^.",
                            "",
                            "&7Come back in: &6" + Chat.timeTranslate(this.data.timeRemaining()),
                            "&c&m------&6&m------&e&m------&a&m------&b&m------&d&m------")
                    .build();
        }

    }


    public void run() {
        this.data.update();
        this.update();
    }

    public void claimAnimation() {
        new BukkitRunnable() {

            double amount = 0.5;

            @Override
            public void run() {
                if(!player.isOnline()) {
                    this.cancel();
                    return;
                } else {
                    if (amount < 2) {
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, (float) 0.75, (float) amount);
                    } else if (amount > 2 && amount < 2.1) {
                        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 1, 1);
                    } else if (amount > 2.7 && amount < 2.8) {
                        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 1, 1);
                    } else if (amount > 2.9 && amount < 3) {
                        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 1, 1);
                    } else if (amount > 3.2) {
                        this.cancel();
                        return;
                    }
                    amount += 0.1;
                }
            }
        }.runTaskTimer(PluginClass.getPlugin(), 0, 2);
    }

    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getRawSlot() == 13) {
            if(this.data.canClaim()) {
                this.data.claim();
                this.update();
                this.claimAnimation();
            } else {
                this.player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, (float) 0.7, (float) 0.6);
            }
        } else if(e.getRawSlot() == 22) {
            this.player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 2, (float) ThreadLocalRandom.current().nextDouble(0.5, 2));
        } else if(e.getRawSlot() < 27) {
            int random = ThreadLocalRandom.current().nextInt(4);
            if(random == 0) {
                this.player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 2, (float) ThreadLocalRandom.current().nextDouble(0, 2));
            } else if(random == 1) {
                this.player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HAT, 2, (float) ThreadLocalRandom.current().nextDouble(0, 2));
            } else if(random == 2) {
                this.player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 2, (float) ThreadLocalRandom.current().nextDouble(0, 2));
            } else if(random == 3) {
                this.player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 2, (float) ThreadLocalRandom.current().nextDouble(0, 2));
            }
        }
        e.setCancelled(true);
    }


    public void onInventoryDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }
}
