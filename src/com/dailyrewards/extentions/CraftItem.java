package com.dailyrewards.extentions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CraftItem {

    private final ItemStack item;
    private final ItemMeta meta;
    private boolean glow;

    public CraftItem(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public CraftItem() {
        this(Material.STONE);
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

    public int getEnchantmentLevel(final Enchantment enchantment) {
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

    public ItemMeta getRawMeta() {
        return this.meta;
    }

    public CraftItem setRawMeta(final ItemMeta meta) {
        this.item.setItemMeta(meta);
        return this;
    }

    public boolean hasEnchantment(final Enchantment enchantment) {
        return this.item.containsEnchantment(enchantment);
    }

    public boolean hasFlag(final ItemFlag flag) {
        return this.meta.hasItemFlag(flag);
    }

    public CraftItem setData(final MaterialData data) {
        this.item.setData(data);
        return this;
    }

    public CraftItem setGlow(final boolean glow) {
        if (glow) {
            this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            this.addFlag(ItemFlag.HIDE_ENCHANTS);
        } else {
            this.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
            this.removeFlag(ItemFlag.HIDE_ENCHANTS);
        }
        this.glow = glow;
        return this;
    }

    public CraftItem setType(final Material type) {
        this.item.setType(type);
        return this;
    }

    public CraftItem setDurability(final short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public CraftItem setAmount(final int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public CraftItem addEnchantment(final Enchantment enchantment, final int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }


    public CraftItem addEnchantments(final Map<Enchantment, Integer> enchantments) {
        for (Enchantment enchantment : enchantments.keySet()) {
            this.meta.addEnchant(enchantment, enchantments.get(enchantment), true);
        }
        return this;
    }

    public CraftItem removeFlag(final ItemFlag... flags) {
        this.meta.removeItemFlags(flags);
        return this;
    }


    public CraftItem removeEnchantment(final Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    public CraftItem addFlag(final ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    public CraftItem setDisplayName(final String name) {
        this.meta.setDisplayName(Chat.toColor("&r" + name));
        return this;
    }

    public CraftItem setLocalizedName(final String name) {
        this.meta.setLocalizedName(Chat.toColor(name));
        return this;
    }


    public CraftItem setUnbreakable(final boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    public CraftItem setLore(final List<String> text) {
        this.meta.setLore(Chat.toColor(text, true));
        return this;
    }

    public CraftItem setLore(final String... text) {
        this.meta.setLore(Chat.toColor(true, text));
        return this;
    }

    public ItemStack build() {
        return this.buildMeta().item;
    }

    private CraftItem buildMeta() {
        item.setItemMeta(meta);
        return this;
    }

    public static CraftItem fromConfiguration(final ConfigurationSection section) throws Exception {
        List<String> attributes = new ArrayList<>(section.getKeys(false));
        CraftItem item = new CraftItem();
        if (attributes.contains("glow") && section.isBoolean("glow")) item.setGlow(section.getBoolean("glow"));
        if (attributes.contains("material") && section.isString("material"))
            item.setType(Material.valueOf(section.getString("material")));
        else throw new IOException(section.getCurrentPath() + ".material value missing.");
        if (attributes.contains("durability") && section.isInt("durability"))
            item.setDurability((short) section.getInt("durability"));
        if (attributes.contains("amount") && section.isInt("amount")) item.setAmount(section.getInt("amount"));
        if (attributes.contains("display-name") && section.isString("display-name"))
            item.setDisplayName(section.getString("display-name"));
        if (attributes.contains("lore") && section.isList("lore")) item.setLore(section.getStringList("lore"));
        return item;
    }

    @Override
    public String toString() {
        return "CraftItem{" +
                "item=" + item +
                ", meta=" + meta +
                ", glow=" + glow +
                '}';
    }

}
