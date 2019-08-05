package me.marnic.bedwars.mechanics.bwgame;

import me.marnic.bedwars.api.bwobject.IFinishable;
import me.marnic.bedwars.api.util.InventoryUtil;
import me.marnic.bedwars.api.util.NameObjectPair;
import me.marnic.bedwars.api.util.ObjectsUtil;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

public class BedWarsSpawner implements IFinishable, ConfigurationSerializable {

    private SpawnerMaterials material;
    private BedWarsTeam team;
    private Location spawnerPos;
    private int spawnTime;
    private ItemStack drop;

    /**
     *
     * @param material SpawnerMaterials enum
     * @param team team spawner belongs to
     * @param spawnerPos spawn position
     */
    public BedWarsSpawner(SpawnerMaterials material, BedWarsTeam team, Location spawnerPos) {
        this.material = material;
        this.team = team;
        this.spawnerPos = spawnerPos;
        switch (material) {
            case CLAY:
                spawnTime = 1;
                this.drop = InventoryUtil.price(SpawnerMaterials.CLAY, 1);
                break;
            case GOLD:
                spawnTime = 20;
                this.drop = InventoryUtil.price(SpawnerMaterials.GOLD, 1);
                break;
            case IRON:
                spawnTime = 10;
                this.drop = InventoryUtil.price(SpawnerMaterials.IRON, 1);
                break;
        }
    }

    /**
     * Used for deserialization
     * @param map
     */
    public BedWarsSpawner(Map<String, Object> map) {
        this.material = SpawnerMaterials.valueOf((String) map.get("material"));
        this.team = null;
        this.spawnerPos = (Location) map.get("spawnerPos");
        switch (material) {
            case CLAY:
                spawnTime = 1;
                this.drop = InventoryUtil.price(SpawnerMaterials.CLAY, 1);
                break;
            case GOLD:
                spawnTime = 20;
                this.drop = InventoryUtil.price(SpawnerMaterials.GOLD, 1);
                break;
            case IRON:
                spawnTime = 10;
                this.drop = InventoryUtil.price(SpawnerMaterials.IRON, 1);
                break;
        }
    }

    public void setTeam(BedWarsTeam team) {
        this.team = team;
    }

    public SpawnerMaterials getMaterial() {
        return material;
    }

    public BedWarsTeam getTeam() {
        return team;
    }

    public Location getSpawnerPos() {
        return spawnerPos;
    }


    @Override
    public boolean isFinished() {
        return ObjectsUtil.nonNull(material, team, spawnerPos);
    }

    @Override
    public List<String> getNotFinished() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Spawner:");
        list.addAll(ObjectsUtil.nonNullOuts(new NameObjectPair("material", material), new NameObjectPair("team", team), new NameObjectPair("spawnerPos", spawnerPos)));
        return list;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("material", material.name());
        map.put("spawnerPos", spawnerPos);

        return map;
    }

    public enum SpawnerMaterials {
        CLAY, IRON, GOLD
    }

    /**
     * Retunrs spawntime in second intervals
     * @return spawntime
     */
    public int getSpawnTime() {
        return spawnTime;
    }

    public ItemStack getDrop() {
        return drop;
    }
}
