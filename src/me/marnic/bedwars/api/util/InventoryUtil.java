package me.marnic.bedwars.api.util;

import me.marnic.bedwars.mechanics.bwgame.BedWarsSpawner;
import me.marnic.bedwars.mechanics.ingame.shop.ShopHandler;
import me.marnic.bedwars.mechanics.ingame.shop.ShopInventory;
import me.marnic.bedwars.mechanics.ingame.shop.ShopItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/*
 * Copyright (c) 04.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * A Util class to help with inventory stuff
 * Special: ShopInventory
 */
public class InventoryUtil {

    /**
     * Used to create a ShopInventory wrapper around an inventory from given parameters
     * @param startIndex slotId of the start of the shopItemStacks
     * @param stacks list of ShopItemStacks
     * @param defaultItemStack the default ItemStack that is used to fill the whole inventory
     * @param name inventory name and simultaneously handler id
     *             WARNING: Should have "BedWars Shop" in name
     * @return ShopInventory wrapper around Bukkit inventory
     */
    public static ShopInventory createFrom(int startIndex, List<ShopItemStack> stacks, ShopItemStack defaultItemStack, String name) {
        Inventory inv = Bukkit.createInventory(null, 27, name);
        ShopItemStack[] stacksList = new ShopItemStack[27];
        Arrays.fill(stacksList, defaultItemStack);

        for (int i = 0; i < stacks.size(); i++) {
            stacksList[startIndex + i] = stacks.get(i);
        }

        inv.setContents(stacksList);

        ShopInventory shopInventory = new ShopInventory(inv);

        shopInventory.getClickableStacks().addAll(stacks);

        ShopHandler.INSTANCE.getInvsMap().put(name, shopInventory);

        return shopInventory;
    }

    /**
     * Used to create a ShopInventory wrapper around an inventory from given parameters with back button to main inventory
     * @param startIndex slotId of the start of the shopItemStacks
     * @param stacks list of ShopItemStacks
     * @param defaultItemStack the default ItemStack that is used to fill the whole inventory
     * @param name inventory name and simultaneously handler id
     *             WARNING: Should have "BedWars Shop" in name
     * @param main main ShopInventory used for back button
     * @return ShopInventory wrapper around Bukkit inventory
     */
    public static ShopInventory createFrom(int startIndex, List<ShopItemStack> stacks, ShopItemStack defaultItemStack, String name, ShopInventory main) {
        Inventory inv = Bukkit.createInventory(null, 27, name);
        ItemStack[] stacksList = new ItemStack[27];
        Arrays.fill(stacksList, defaultItemStack);

        for (int i = 0; i < stacks.size(); i++) {
            stacksList[startIndex + i] = stacks.get(i);
            stacks.get(i).runOnAdd(stacksList, startIndex + i);
        }

        inv.setContents(stacksList);

        ShopInventory shopInventory = new ShopInventory(inv, main);

        shopInventory.getClickableStacks().addAll(stacks);

        ShopHandler.INSTANCE.getInvsMap().put(name, shopInventory);

        return shopInventory;
    }

    /**
     * Returns price ItemStack of specified material and count
     * @param materials SpawnerMaterials enum
     * @param count amount of ItemStack
     * @return price ItemStack
     */
    public static ItemStack price(BedWarsSpawner.SpawnerMaterials materials, int count) {
        switch (materials) {
            case CLAY:
                return new ItemStack(Material.BRICK, count);
            case IRON:
                return new ItemStack(Material.IRON_INGOT, count);
            case GOLD:
                return new ItemStack(Material.GOLD_INGOT, count);
            default:
                return null;
        }
    }

    /**
     * Used to handle transaction in shop
     * @param player customer
     * @param shopItemStack ItemStack to buy
     * @return true if transaction is successful
     */
    public static boolean pay(Player player, ShopItemStack shopItemStack) {

        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack != null) {
                if (stack.getType() == shopItemStack.getPrice().getType()) {
                    if (stack.getAmount() >= shopItemStack.getPrice().getAmount()) {
                        stack.setAmount(stack.getAmount() - shopItemStack.getPrice().getAmount());
                        player.getInventory().addItem(new ItemStack(shopItemStack));
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
