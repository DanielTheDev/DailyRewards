package com.dailyrewards.menu;

import com.dailyrewards.PluginClass;
import com.dailyrewards.config.PlayerData;
import com.dailyrewards.config.PlayerDataManager;
import com.dailyrewards.extentions.Chat;
import com.dailyrewards.extentions.CraftItem;
import com.dailyrewards.extentions.Gui;
import com.dailyrewards.extentions.Skull;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ThreadLocalRandom;


public class MainGui extends Gui {

    private final PlayerDataManager manager;
    private ItemStack skull;
    private int loading = 0;
    private final PlayerData data;
    private final CraftItem item = new CraftItem(Material.EYE_OF_ENDER);
    private BukkitTask animation;
    private boolean opening;

    public MainGui(Player player) {
        super(player, "&f&l--&e&l=&f&l-- &5&lDaily &d&lRewards &f&l--&e&l=&f&l--", 27, true);
        this.manager = PluginClass.getPlugin().getPluginLib().getPlayerDataManager();
        this.data = manager.getPlayer(player);
        this.skull = new CraftItem(Material.SKULL_ITEM, 1, (short) 3).setDisplayName("&7Loading stats...").build();
        this.setPlayerStats();
    }

    public void fill() {
        inventory.setItem(22, skull);
        if(!this.opening) inventory.setItem(13, getChest());
        else {
            if(loading != 27) loading+=1;

            inventory.setItem(13, item.setDisplayName("Loading "+Chat.percentText("▌", 10, (int) (loading/2.7), '5','d')+"&f "+((27-loading)/10.0)+" seconds").build());
        }

        for(int place = 0; place < this.getSize(); place++) {
            if(!(place == 13 || place == 22))
                inventory.setItem(place, new CraftItem(Material.STAINED_GLASS_PANE, 1, (short) (place > 9 && place < 17 ? 6 : 2)).setDisplayName("&a").build());
        }
        for(int place : new int[] {0,8,18,26}) {
            inventory.setItem(place, new CraftItem(Material.STAINED_GLASS_PANE, 1, (short) 10).setGlow(true).setDisplayName("&a").build());
        }
        this.player.updateInventory();
    }

    public void setPlayerStats() {
        Skull.getPlayerSkull(player, item -> {

                if(this.skull == null) return;

                this.skull = item.
                        setDisplayName("&6&l" + player.getName()+"'s Stats")
                        .setLore(
                            "&7Claim Streak&f: "+ data.getClaimStreak(),
                            "&7Streak Remaining Time&f: "+ data.getRemainingTimeFormat(),
                            "&7Total Claimed&f: "+ data.getTotalClaimed(),
                            "&7First Claimed&f: "+ data.getFirstClaimedRelative(),
                            "&7Last Claimed&f: "+ data.getLastClaimedRelative()).build();
                this.update();
        });
    }

    public ItemStack getChest() {
        if(this.data.canClaim()) {
            return new CraftItem(Material.CHEST)
                    .setDisplayName("&aDaily Bonus Reward")
                    .setLore(
                            "&c&m------&6&m------&e&m------&a&m------&b&m------&d&m------",
                            "&7Your daily bonus reward is available",
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
        loading = 0;
        animation = new BukkitRunnable() {

            double amount = 0.5;

            @Override
            public void run() {
                if(!player.isOnline()) {
                    this.cancel();
                } else {
                    if (amount < 2) {
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, (float) 0.75, (float) amount);
                    } else if (amount > 2 && amount < 2.1) {
                        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 1, 1);
                    } else if (amount > 2.3 && amount < 2.4) {
                        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 1, 1);
                    } else if (amount > 2.5 && amount < 2.6) {
                        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 1, 1);
                    } else if (amount > 3) {
                        openPresents();
                        this.cancel();
                        return;
                    }
                    update();
                    amount += 0.1;
                }
            }
        }.runTaskTimer(PluginClass.getPlugin(), 0, 2);
    }

    private void openPresents() {
        if(this.player.isOnline() && PluginClass.getPlugin().getPluginLib().getMenuManager().hasGuiOpen(player))
        PluginClass.getPlugin().getPluginLib().getMenuManager().switchTo(new RewardGui(player));
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent e) {
        if(this.animation != null) this.animation.cancel();
        this.skull = null;
        this.animation = null;
        super.onInventoryClose(e);
    }

    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getRawSlot() == 13) {
            if(this.data.canClaim()) {
                this.opening = true;
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
