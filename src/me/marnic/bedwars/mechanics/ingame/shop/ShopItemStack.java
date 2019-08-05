package me.marnic.bedwars.mechanics.ingame.shop;

import me.marnic.bedwars.api.log.BedWarsLogger;
import me.marnic.bedwars.api.util.InventoryUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/*
 * Copyright (c) 04.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Better ItemStack alternative
 */
public class ShopItemStack extends ItemStack {

    private ItemStack price;

    /**
     * Used to create ItemStack from material and set display name
     * @param name display name
     * @param material Material of ItemStack
     */
    public ShopItemStack(String name, Material material) {
        super(material);
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(name);
        setItemMeta(meta);
    }

    /**
     * Used to create ItemStack from material and set display name with inventory opening functionality
     * @param name display name
     * @param material Material of ItemStack
     * @param toOpen inventory to open on click
     */
    public ShopItemStack(String name, Material material, ShopInventory toOpen) {
        super(material);
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(name);
        setItemMeta(meta);
        setOnClick(player -> player.openInventory(toOpen.getInv()));
    }

    /**
     * Used to create ItemStack from material and set display name with trading functionality
     * @param name display name
     * @param material Material of ItemStack
     * @param price ItemStack price for transaction
     */
    public ShopItemStack(String name, Material material, ItemStack price) {
        this(name, material);
        this.price = price;

        setOnClick(player -> {
            if (!InventoryUtil.pay(player, this)) {
                BedWarsLogger.log(player, "You have not the required materials");
            }
        });

        setOnAdd((inv, slot) -> {
            inv[slot + 9] = (price);
        });
    }

    /**
     * Used to create ItemStack from material with trading functionality
     * @param material Material of ItemStack
     * @param price ItemStack price for transaction
     */
    public ShopItemStack(Material material, ItemStack price) {
        this(material);
        this.price = price;

        setOnClick(player -> {
            if (!InventoryUtil.pay(player, this)) {
                BedWarsLogger.log(player, "You have not the required materials");
            }
        });

        setOnAdd((inv, slot) -> {
            inv[slot + 9] = (price);
        });
    }

    public ShopItemStack(Material type) {
        super(type);
    }

    private Consumer<Player> onClick;
    private BiConsumer<ItemStack[], Integer> onAdd;

    /**
     * Used to set action when click in inventory
     * @param runnable action
     * @return it self
     */
    public ShopItemStack setOnClick(Consumer<Player> runnable) {
        this.onClick = runnable;
        return this;
    }

    /**
     * Used to set action when added to the inventory
     * @param runnable action
     * @return it self
     */
    public ShopItemStack setOnAdd(BiConsumer<ItemStack[], Integer> runnable) {
        this.onAdd = runnable;
        return this;
    }

    /**
     * Just like addEnchantment but it returns it self
     * @return it self
     */
    public ShopItemStack addEnchantmentNEW(Enchantment enchantment, int level) {
        ItemMeta meta = getItemMeta();
        meta.addEnchant(enchantment, level, true);
        setItemMeta(meta);
        return this;
    }

    /**
     * Just like addEnchantment but it returns it self
     * @return it self
     */
    public ShopItemStack setAmountNEW(int amount) {
        setAmount(amount);
        return this;
    }

    public void runOnClick(Player player) {
        if (onClick != null) {
            onClick.accept(player);
        }
    }

    public void runOnAdd(ItemStack[] inventory, int slot) {
        if (onAdd != null) {
            onAdd.accept(inventory, slot);
        }
    }

    public ItemStack getPrice() {
        return price;
    }
}
