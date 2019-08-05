package me.marnic.bedwars.api.util;

import me.marnic.bedwars.api.log.BedWarsLogger;
import me.marnic.bedwars.mechanics.builder.BedWarsBuilder;
import me.marnic.bedwars.mechanics.bwgame.BedWarsMap;
import me.marnic.bedwars.mechanics.bwgame.BedWarsSpawner;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Util class to help building a bedwars map
 */
public class BWBuilderUtil {

    /**
     * Used to check if a BedWars map is in creation
     * @param player command executor
     * @return true if a map is in creation
     */
    public static boolean checkCreation(Player player) {
        boolean value = BedWarsBuilder.INSTANCE.getCurrentBuildingObject() != null;
        if (!value) {
            BedWarsLogger.warn(player, "Command could not be executed! No map is in creation!");
        }
        return value;
    }

    /**
     * Used to check if a BedWars map is in creation and a specified team exists
     * @param player command executor
     * @param teamName team name to check if exists
     * @return true if map is in creation and team exists
     */
    public static boolean checkCreationAndTeam(Player player, String teamName) {
        boolean valueMap = BedWarsBuilder.INSTANCE.getCurrentBuildingObject() != null;
        if (!valueMap) {
            BedWarsLogger.warn(player, "Command could not be executed! No map is in creation!");
            return valueMap;
        }

        ChatColor chatColor = BWBuilderUtil.chatColorFromString(teamName);
        BedWarsMap map = BedWarsBuilder.INSTANCE.getCurrentBuildingObject();

        if (chatColor != null && map.getTeams().containsKey(chatColor)) {
            return true;
        } else {
            BedWarsLogger.warn(player, "Command could not be executed! Team does not exist!");
            return false;
        }
    }

    /**
     * Used to check if spawner material exists
     * @param player command executor
     * @param material spawner material name
     * @return true if enum with specified name exists
     */
    public static boolean checkMaterial(Player player, String material) {
        boolean value = materialsFromString(material) != null;

        if (!value) {
            BedWarsLogger.warn(player, "Material " + material + " is not a valid material!");
        }

        return value;
    }

    /**
     * Used to get ChatColor from string
     * @param color name of the color
     * @return ChatColor from string
     */
    public static ChatColor chatColorFromString(String color) {
        try {
            ChatColor chatColor = ChatColor.valueOf(color);
            return chatColor;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Used to get SpawnerMaterials from name
     * @param material material name
     * @return SpawnerMaterials from name
     */
    public static BedWarsSpawner.SpawnerMaterials materialsFromString(String material) {
        try {
            BedWarsSpawner.SpawnerMaterials materials = BedWarsSpawner.SpawnerMaterials.valueOf(material);
            return materials;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
