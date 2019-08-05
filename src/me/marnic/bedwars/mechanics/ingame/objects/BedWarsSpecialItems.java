package me.marnic.bedwars.mechanics.ingame.objects;

import me.marnic.bedwars.mechanics.bwgame.BedWarsTeam;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

/*
 * Copyright (c) 05.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Functions of special items
 */
public class BedWarsSpecialItems {

    public static void handleTeleporter(Player player, BedWarsTeam team) {
        player.teleport(team.getSpawnLocation());
    }

    public static void handlePlatform(PlayerInteractEvent e) {

        World world = e.getPlayer().getWorld();

        Location start = e.getPlayer().getLocation().clone();
        start.subtract(0, 2, 0);


        placeBlock(start, Material.SLIME_BLOCK, world);
        placeBlock(new Location(world, start.getX() + 1, start.getY(), start.getZ()), Material.SLIME_BLOCK, world);
        placeBlock(new Location(world, start.getX() - 1, start.getY(), start.getZ()), Material.SLIME_BLOCK, world);
        placeBlock(new Location(world, start.getX(), start.getY(), start.getZ() + 1), Material.SLIME_BLOCK, world);
        placeBlock(new Location(world, start.getX(), start.getY(), start.getZ() - 1), Material.SLIME_BLOCK, world);
    }

    private static void placeBlock(Location where, Material material, World world) {
        if (world.getBlockAt(where).getType() == Material.AIR) {
            world.getBlockAt(where).setType(material);
        }
    }
}
