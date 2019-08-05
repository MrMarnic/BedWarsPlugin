package me.marnic.bedwars.mechanics.ingame.shop;

import me.marnic.bedwars.api.util.InventoryUtil;
import me.marnic.bedwars.mechanics.bwgame.BedWarsSpawner;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Arrays;
import java.util.HashMap;

/*
 * Copyright (c) 04.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Used to handle all Shop related stuff
 */
public class ShopHandler {
    public static ShopHandler INSTANCE = new ShopHandler();

    private HashMap<String, ShopInventory> invsMap = new HashMap<>();

    private ShopInventory mainInv;

    private ShopInventory blocksInv, pickaxeInv, swordsInv, armorInv, bowsInv, specialInv, healingInv;

    public void init() {
        this.mainInv = new ShopInventory(null);
        this.blocksInv = createBlocks();
        this.pickaxeInv = createPickaxesInv();
        this.swordsInv = createSwordsInv();
        this.armorInv = createArmorInv();
        this.bowsInv = createBowsInv();
        this.specialInv = createSpecialInv();
        this.healingInv = createHealingInv();
        this.mainInv.copyFrom(createDefault());
    }

    public void handleInteractEvent(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.VILLAGER) {
            e.setCancelled(true);
            e.getPlayer().openInventory(mainInv.getInv());
        }
    }

    public void handleInvClickEvent(InventoryClickEvent e) {
        if (invsMap.containsKey(e.getView().getTitle())) {
            invsMap.get(e.getView().getTitle()).handleClick(e.getCurrentItem().getType(), (Player) e.getWhoClicked());
        }
        e.setCancelled(true);
    }

    private ShopInventory createDefault() {
        ShopItemStack blocks = new ShopItemStack("Blocks", Material.SANDSTONE, blocksInv);
        ShopItemStack pickaxe = new ShopItemStack("Pickaxes", Material.STONE_PICKAXE, pickaxeInv);
        ShopItemStack swords = new ShopItemStack("Swords", Material.IRON_SWORD, swordsInv);
        ShopItemStack armor = new ShopItemStack("Armor", Material.CHAINMAIL_CHESTPLATE, armorInv);
        ShopItemStack bows = new ShopItemStack("Bows", Material.BOW, bowsInv);
        ShopItemStack special = new ShopItemStack("Specials", Material.TNT, specialInv);
        ShopItemStack potions = new ShopItemStack("Healing", Material.GOLDEN_APPLE, healingInv);
        return InventoryUtil.createFrom(10, Arrays.asList(blocks, pickaxe, swords, armor, bows, special, potions), new ShopItemStack(" ", Material.GLASS_PANE), "BedWars Shop");
    }

    private ShopInventory createBlocks() {

        ShopItemStack sandstone = new ShopItemStack(Material.SANDSTONE, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 1));
        ShopItemStack endstone = new ShopItemStack(Material.END_STONE, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 10));
        ShopItemStack ironBlock = new ShopItemStack(Material.IRON_BLOCK, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.IRON, 1));
        ShopItemStack wool = new ShopItemStack(Material.WHITE_WOOL, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 4));
        ShopItemStack glass = new ShopItemStack(Material.GLASS, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 5));

        return InventoryUtil.createFrom(11, Arrays.asList(sandstone, endstone, ironBlock, wool, glass), new ShopItemStack(" ", Material.GLASS_PANE), "BedWars Shop - Blocks", mainInv);
    }

    private ShopInventory createPickaxesInv() {

        ShopItemStack wood = new ShopItemStack(Material.WOODEN_PICKAXE, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 15));
        ShopItemStack stone = new ShopItemStack(Material.STONE_PICKAXE, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.IRON, 1));
        ShopItemStack stone_enchanted = new ShopItemStack(Material.STONE_PICKAXE, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.IRON, 3)).addEnchantmentNEW(Enchantment.DIG_SPEED, 1);
        ShopItemStack iron = new ShopItemStack(Material.IRON_PICKAXE, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.GOLD, 2));
        ShopItemStack iron_enchanted = new ShopItemStack(Material.IRON_PICKAXE, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.GOLD, 5)).addEnchantmentNEW(Enchantment.DIG_SPEED, 1);

        return InventoryUtil.createFrom(11, Arrays.asList(wood, stone, stone_enchanted, iron, iron_enchanted), new ShopItemStack(" ", Material.GLASS_PANE), "BedWars Shop - Pickaxes", mainInv);
    }

    private ShopInventory createSwordsInv() {

        ShopItemStack stick = new ShopItemStack(Material.STICK, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 10)).addEnchantmentNEW(Enchantment.KNOCKBACK, 1);
        ShopItemStack wood = new ShopItemStack(Material.WOODEN_SWORD, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 20));
        ShopItemStack wood_enchanted = new ShopItemStack(Material.WOODEN_SWORD, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 25)).addEnchantmentNEW(Enchantment.DAMAGE_ALL, 1);
        ShopItemStack stone = new ShopItemStack(Material.STONE_SWORD, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.IRON, 1));
        ShopItemStack iron_enchanted = new ShopItemStack(Material.IRON_SWORD, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.GOLD, 4));

        return InventoryUtil.createFrom(11, Arrays.asList(stick, wood, wood_enchanted, stone, iron_enchanted), new ShopItemStack(" ", Material.GLASS_PANE), "BedWars Shop - Swords", mainInv);
    }

    private ShopInventory createArmorInv() {

        ShopItemStack boots = new ShopItemStack(Material.LEATHER_BOOTS, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 1));
        ShopItemStack leggins = new ShopItemStack(Material.LEATHER_LEGGINGS, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 2));
        ShopItemStack helmet = new ShopItemStack(Material.LEATHER_HELMET, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 1));
        ShopItemStack chest = new ShopItemStack(Material.LEATHER_CHESTPLATE, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 4));
        ShopItemStack chest_chain = new ShopItemStack(Material.CHAINMAIL_CHESTPLATE, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.IRON, 2));
        ShopItemStack chest_chain_2 = new ShopItemStack(Material.CHAINMAIL_CHESTPLATE, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.IRON, 5)).addEnchantmentNEW(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ShopItemStack chest_iron = new ShopItemStack(Material.IRON_CHESTPLATE, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.GOLD, 2));

        return InventoryUtil.createFrom(10, Arrays.asList(boots, leggins, helmet, chest, chest_chain, chest_chain_2, chest_iron), new ShopItemStack(" ", Material.GLASS_PANE), "BedWars Shop - Armor", mainInv);
    }

    private ShopInventory createBowsInv() {

        ShopItemStack bow_1 = new ShopItemStack(Material.BOW, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.GOLD, 1));
        ShopItemStack bow_2 = new ShopItemStack(Material.BOW, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.GOLD, 5)).addEnchantmentNEW(Enchantment.ARROW_DAMAGE, 1);
        ShopItemStack bow_3 = new ShopItemStack(Material.BOW, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.GOLD, 9)).addEnchantmentNEW(Enchantment.ARROW_DAMAGE, 2).addEnchantmentNEW(Enchantment.ARROW_KNOCKBACK, 1);
        ShopItemStack arrow1 = new ShopItemStack(Material.ARROW, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.IRON, 5)).setAmountNEW(10);
        ShopItemStack arrow2 = new ShopItemStack(Material.ARROW, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.GOLD, 1)).setAmountNEW(32);

        return InventoryUtil.createFrom(12, Arrays.asList(bow_1, bow_2, bow_3, arrow1, arrow2), new ShopItemStack(" ", Material.GLASS_PANE), "BedWars Shop - Bows", mainInv);
    }

    private ShopInventory createSpecialInv() {

        ShopItemStack tnt = new ShopItemStack(Material.TNT, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.IRON, 2));
        ShopItemStack cobweb = new ShopItemStack(Material.COBWEB, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.CLAY, 15));
        ShopItemStack saving_platfrom = new ShopItemStack("Saving Platform", Material.SLIME_BALL, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.GOLD, 7));
        ShopItemStack ender_pearl = new ShopItemStack(Material.ENDER_PEARL, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.GOLD, 10));
        ShopItemStack home = new ShopItemStack("Home Teleporter", Material.BLAZE_ROD, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.IRON, 5));

        return InventoryUtil.createFrom(11, Arrays.asList(tnt, cobweb, saving_platfrom, ender_pearl, home), new ShopItemStack(" ", Material.GLASS_PANE), "BedWars Shop - Specials", mainInv);
    }

    private ShopInventory createHealingInv() {

        ShopItemStack golden_apple = new ShopItemStack(Material.GOLDEN_APPLE, InventoryUtil.price(BedWarsSpawner.SpawnerMaterials.IRON, 5));

        return InventoryUtil.createFrom(13, Arrays.asList(golden_apple), new ShopItemStack(" ", Material.GLASS_PANE), "BedWars Shop - Healing", mainInv);
    }


    public HashMap<String, ShopInventory> getInvsMap() {
        return invsMap;
    }
}
