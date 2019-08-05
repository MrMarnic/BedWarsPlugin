package me.marnic.bedwars.mechanics.ingame.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

/*
 * Copyright (c) 04.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Wrapper around the bukkit Inventory
 */
public class ShopInventory {
    private ArrayList<ShopItemStack> clickableStacks = new ArrayList<>();
    private Inventory inv;

    /**
     * Used to create inventory from Bukkit inventory
     * @param inv Bukkit inventory
     */
    public ShopInventory(Inventory inv) {
        this.inv = inv;
    }

    /**
     * Used to create wrapper around Bukkit inventory with back button to main inventory
     * @param inv Bukkit inventory
     * @param main main ShopInventory
     */
    public ShopInventory(Inventory inv, ShopInventory main) {
        this(inv);
        ShopItemStack shopItemStack = new ShopItemStack("Back", Material.REDSTONE).setOnClick(player -> player.openInventory(main.getInv()));
        inv.setItem(18, shopItemStack);
        clickableStacks.add(shopItemStack);
    }

    public void handleClick(Material material, Player player) {
        for (ShopItemStack shopItemStack : clickableStacks) {
            if (shopItemStack.getType() == material) {
                shopItemStack.runOnClick(player);
            }
        }
    }

    public Inventory getInv() {
        return inv;
    }

    public ArrayList<ShopItemStack> getClickableStacks() {
        return clickableStacks;
    }

    public void copyFrom(ShopInventory inventory) {
        this.inv = inventory.getInv();
        this.clickableStacks = inventory.getClickableStacks();
    }
}
