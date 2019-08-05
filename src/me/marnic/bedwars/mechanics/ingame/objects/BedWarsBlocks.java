package me.marnic.bedwars.mechanics.ingame.objects;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Copyright (c) 04.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * All blocks that can be broken in BedWars (not cancelled by an event)
 */
public class BedWarsBlocks {
    public static ArrayList<Material> MATERIALS = new ArrayList<>();

    static {
        MATERIALS.addAll(Arrays.asList(Material.RED_BED, Material.SANDSTONE, Material.IRON_BLOCK, Material.END_STONE, Material.WHITE_WOOL, Material.GLASS, Material.COBWEB, Material.TNT));
    }
}
