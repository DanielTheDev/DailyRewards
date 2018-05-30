package com.dailyrewards.extentions;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CraftItem {

    private ItemStack item;
    private ItemMeta meta;
    private boolean glow;

    public CraftItem(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public CraftItem(Material material) {
        this(material, 1);
    }

    public CraftItem(Material material, int amount) {
        this(material, amount, (short) 0);
    }

    public CraftItem(Material material, int amount, short durability) {
        this.item = new ItemStack(material, amount, durability);
        this.meta = item.getItemMeta();
    }

    public short getDurability() {
        return this.item.getDurability();
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.item.getEnchantments();
    }

    public int getAmount() {
        return this.item.getAmount();
    }

    public Material getType() {
        return this.item.getType();
    }

    public int getMaxStackSize() {
        return this.item.getMaxStackSize();
    }

    public int getEnchantmentLevel(Enchantment enchantment) {
        return this.item.getEnchantmentLevel(enchantment);
    }

    public String getDisplayName() {
        return this.meta.getDisplayName();
    }

    public List<String> getLore() {
        return this.meta.getLore();
    }

    public Set<ItemFlag> getFlags() {
        return this.meta.getItemFlags();
    }

    public MaterialData getData() {
        return this.item.getData();
    }

    public ItemMeta getRawMeta(){
        return this.meta;
    }

    public CraftItem setRawMeta(ItemMeta meta){
        this.item.setItemMeta(meta);
        return this;
    }

    public boolean hasEnchantment(Enchantment enchantment) {
        return this.item.containsEnchantment(enchantment);
    }

    public boolean hasFlag(ItemFlag flag) {
        return this.meta.hasItemFlag(flag);
    }

    public CraftItem setData(MaterialData data) {
        this.item.setData(data);
        return this;
    }

    public CraftItem setGlow(boolean glow) {
        if(glow) {
            this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            this.addFlag(ItemFlag.HIDE_ENCHANTS);
            this.glow = true;
        } else {
            if(glow) {
                this.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
                this.removeFlag(ItemFlag.HIDE_ENCHANTS);
            }
        }
        return this.buildMeta();
    }

    public CraftItem setType(Material type) {
        this.item.setType(type);
        return this;
    }

    public CraftItem setDurability(short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public CraftItem setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public CraftItem addEnchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this.buildMeta();
    }


    public CraftItem addEnchantments(Map<Enchantment, Integer> enchantments) {
        for(Enchantment enchantment : enchantments.keySet()) {
            this.meta.addEnchant(enchantment, enchantments.get(enchantment), true);
        }
        return this.buildMeta();
    }

    public CraftItem removeFlag(ItemFlag... flags) {
        this.meta.removeItemFlags(flags);
        return this.buildMeta();
    }


    public CraftItem removeEnchantment(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this.buildMeta();
    }

    public CraftItem addFlag(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        this.buildMeta();
        return this;
    }


    public ItemStack build() {
        return this.buildMeta().item;
    }

    public CraftItem setDisplayName(String name) {
        this.meta.setDisplayName(Chat.toColor(name));
        return this.buildMeta();
    }

    public CraftItem setLocalizedName(String name) {
        this.meta.setLocalizedName(Chat.toColor(name));
        return this.buildMeta();
    }


    public CraftItem setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this.buildMeta();
    }

    public CraftItem setLore(List<String> text) {
        this.meta.setLore(Chat.toColor(text, true));
        return this.buildMeta();
    }

    public CraftItem setLore(String... text) {
        this.meta.setLore(Chat.toColor(true, text));
        return this.buildMeta();
    }

    private CraftItem buildMeta() {
        item.setItemMeta(meta);
        return this;
    }

    public static CraftItem getPlayerSkull(Player player) {
        CraftItem item = new CraftItem(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) item.getRawMeta();
        meta.setOwner(player.getName());
        item.setRawMeta(meta);
        return item;
    }

}
