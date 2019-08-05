package me.marnic.bedwars.mechanics.builder;

import me.marnic.bedwars.api.util.BWBuilderUtil;
import me.marnic.bedwars.api.util.WorldUtil;
import me.marnic.bedwars.main.BedWars;
import me.marnic.bedwars.mechanics.bwgame.BedWarsMap;
import me.marnic.bedwars.mechanics.bwgame.BedWarsSpawner;
import me.marnic.bedwars.mechanics.bwgame.BedWarsTeam;
import me.marnic.bedwars.mechanics.ingame.BedWarsInGameHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Used to build and configure of a BedWars map
 */
public class BedWarsBuilder {
    public static BedWarsBuilder INSTANCE = new BedWarsBuilder();

    private BedWarsMap map;
    private BedPlacer placer;

    /**
     * Used to check if bedwars map exists in config
     * @param config
     */
    public void checkConfig(FileConfiguration config) {
        if (config.get("bw.map") != null) {
            BedWarsInGameHandler.INSTANCE.load(config);
        }
    }

    /**
     * Used to start configuring of a new bedwarps map called @mapName
     * @param mapName name of map
     */
    public void startCreating(String mapName) {
        this.map = new BedWarsMap();
        this.map.setName(mapName);
    }

    public void setGameWorld(World world) {
        if (map != null) {
            this.map.setGameWorld(world);
        }
    }

    public void setLobbyWorld(World world) {
        if (map != null) {
            this.map.setLobbyWorld(world);
        }
    }

    public void setLobbySpawn(Location location) {
        if (map != null) {
            map.setLobbySpawn(location);
        }
    }


    public void createTeam(ChatColor chatColor) {
        if (map != null) {
            BedWarsTeam team = new BedWarsTeam();
            team.setChatColor(chatColor);
            map.getTeams().put(chatColor, team);
        }
    }

    public void setTeamPlayerCount(String teamColor, int size) {
        if (map != null) {
            ChatColor color = BWBuilderUtil.chatColorFromString(teamColor);
            BedWarsTeam team = map.getTeams().get(color);

            if (team != null) {
                team.setPlayerCount(size);
            }
        }
    }

    public void setTeamSpawnPos(String teamColor, Location spawn) {
        if (map != null) {
            ChatColor color = BWBuilderUtil.chatColorFromString(teamColor);
            BedWarsTeam team = map.getTeams().get(color);

            if (team != null) {
                team.setSpawnLocation(spawn);
            }
        }
    }

    public void setTeamBedPos(String teamColor, Location bed, Block block) {
        if (map != null) {
            ChatColor color = BWBuilderUtil.chatColorFromString(teamColor);
            BedWarsTeam team = map.getTeams().get(color);

            if (team != null) {
                team.setBedBlockPos(bed);

                BlockFace face = ((Bed) block.getBlockData()).getFacing();
                team.setBedBlockPos2(block.getRelative(face).getLocation());
            }
        }
    }

    public void addTeamSpawner(String teamColor, String material, Location location) {
        if (map != null) {
            ChatColor color = BWBuilderUtil.chatColorFromString(teamColor);
            BedWarsTeam team = map.getTeams().get(color);
            BedWarsSpawner.SpawnerMaterials materials = BWBuilderUtil.materialsFromString(material);

            if (team != null && materials != null) {
                team.getSpawners().add(new BedWarsSpawner(materials, team, location));
            }
        }
    }

    /**
     * Used to save BedWars map and worlds to load on startup
     */
    public void saveMap() {
        BedWars.INSTANCE.getConfig().set("bw.map", map);
        List<String> worldsToLoad = Arrays.asList(map.getGameWorld().getName(), map.getLobbyWorld().getName());
        WorldUtil.createSaveFile(worldsToLoad);
        BedWars.INSTANCE.saveConfig();
    }

    public boolean finishMap() {
        return map.isFinished();
    }

    public BedWarsMap getCurrentBuildingObject() {
        return map;
    }

    public BedPlacer getPlacer() {
        return placer;
    }

    /**
     * Used to set location of team bed
     * @param teamName
     */
    public void setPlacer(String teamName) {

        if (teamName == null) {
            this.placer = null;
            return;
        }

        if (map != null) {
            BedWarsTeam team = map.getTeams().get(BWBuilderUtil.chatColorFromString(teamName));
            this.placer = new BedPlacer(team);
        }
    }

    public class BedPlacer {
        private BedWarsTeam team;

        public BedPlacer(BedWarsTeam team) {
            this.team = team;
        }

        public BedWarsTeam getTeam() {
            return team;
        }
    }
}
